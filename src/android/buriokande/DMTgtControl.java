package android.buriokande;
import java.util.Calendar;
import android.buriokande.DMDBHelper;
import android.appwidget.AppWidgetManager;
import android.util.Log;
import android.content.*;
import android.widget.*;
import android.database.Cursor; //db操作用　跡で分離
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.buriokande.GantData;

public class DMTgtControl{
	//現在日付を取得 
	final Calendar calendar = Calendar.getInstance();
	final int    year = calendar.get(Calendar.YEAR);
	final int     month = calendar.get(Calendar.MONTH);
	final int day = calendar.get(Calendar.DAY_OF_MONTH);
	//DB関係変数
	private DMDBHelper helper;
	private String Tgt ;
	private GantData[] gantdata;
	private Integer[][] Xaxis;
	public Integer[][] getXaxis(){
		return Xaxis;
	}
	public String  GetTgt(Context context ,AppWidgetManager appWidgetManager){
        Intent intent = new Intent(context, DMWidgetService.class);
        context.startService(intent);
        //目標テーブルより当日目標を取得 res20101216
        helper = new DMDBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();

		//kibdb
		String Ymd = Integer.toString(year*10000 + (month+1)*100 + day);
    	String sql = "select Naiyo from T_TgtNaiyo where ";
    	sql = sql + Ymd;

    	sql = sql + " BETWEEN SDATE AND EDATE";
		Cursor c2 = db.rawQuery(sql, null);
    	if (c2.isAfterLast()){
    		Tgt = "本日の目標はありません";
    	}else{
        	c2.moveToFirst();
        	String[] Stgt = new String[c2.getCount()];
    		Log.w("DM","cnt:" + Integer.toString(c2.getCount()));
    		Log.w("DM","cnt:" + Integer.toString(Stgt.length));
        	for (int i = 0; i < Stgt.length; i++) {
        		Stgt[i] = c2.getString(0);
        		c2.moveToNext();

        	}
        	Tgt = "";
        	for (int i = 0;i < Stgt.length;i++){
        		Tgt = Tgt + Stgt[i]; 
        		Tgt = Tgt + ",";
        	}
    	}
    	
    	c2.close();
    	db.close();

		RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.main);
		remoteView.setTextViewText(R.id.target, Tgt);
        ComponentName thisWidget = new ComponentName(context, DMWidgetProvider.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteView);
        Log.v("DM","TgtCon");
       	return Tgt;
	}
	/**
	 * 
	 * @param context
	 * @param appWidgetManager
	 * @param year
	 * @param month-notinternal
	 * @param day
	 * @return
	 */
	public GantData[] GetGantData(Context context ,AppWidgetManager appWidgetManager,
			Integer year ,Integer month ,Integer day){
        helper = new DMDBHelper(context);
        Integer chkflg = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Integer HaniS;
		Integer HaniE;
		Integer sicle = 10;
		Xaxis = Mkkigen(year ,month,day ,sicle);
		HaniS = Xaxis[0][0]*10000 + (Xaxis[1][0] + 1)*100 + Xaxis[2][0];
		HaniE = Xaxis[0][sicle - 1]*10000 + (Xaxis[1][sicle - 1]+1)*100 + Xaxis[2][sicle - 1];
    	String sql = "select Naiyo,SDate,EDate,Seq from T_TgtNaiyo where ";
    	String Hanni = " between " + Integer.toString(HaniS) + " and " +  Integer.toString(HaniE);
    	sql = sql + "(( SDate" + Hanni + ")";
    	sql = sql + " or ( EDate" + Hanni + "))";
 		Cursor c2 = db.rawQuery(sql, null);		
    	if (c2.isAfterLast()){
    		chkflg = 1;
    	}else{
        	c2.moveToFirst();
        	gantdata = new GantData[c2.getCount()];
        	
        	for (int i = 0; i < gantdata.length; i++) {

        		gantdata[i] = new GantData();

        		gantdata[i].Naiyo = c2.getString(0);
        		gantdata[i].Date1 = c2.getInt(1);
        		gantdata[i].Date2 = c2.getInt(2);
        		gantdata[i].Seq   = c2.getInt(3);
        		c2.moveToNext();
        	}
     	}
    	c2.close();
    	db.close();
    	if (chkflg == 0){
    		return gantdata;

    	}else {
    		return null;
    	}
		
	}
	public boolean TgtDel(Context con ,Integer seq ){
		boolean Success ;
		Success = false;
        //目標テーブルより当日目標を取得 res20101216
        helper = new DMDBHelper(con);
		SQLiteDatabase db = helper.getReadableDatabase();
		db.beginTransaction();
		try {
			SQLiteStatement stmt;					
			stmt = db.compileStatement("delete from T_TgtNaiyo where Seq = " + seq );

			stmt.execute();
			stmt.close();
			db.setTransactionSuccessful();

		} finally {
			
			db.endTransaction();
	
		}
		helper.close();
		db.close();
		Success = true;
		
		
		return Success;
	}
	private Integer[][] Mkkigen(Integer Styear ,Integer Stmonth ,Integer Stday,Integer cicle) {
		Integer[][] kigen = new Integer[3][cicle];
		
		Calendar Cal = Calendar.getInstance();
		Cal.set(Styear, Stmonth, Stday);
		for (int i = 0 ;i < cicle ;i++){

			kigen[0][i] = Cal.get(Calendar.YEAR);
			kigen[1][i] = Cal.get(Calendar.MONTH);
			kigen[2][i] = Cal.get(Calendar.DAY_OF_MONTH);
			Cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return kigen;
	}
	
}