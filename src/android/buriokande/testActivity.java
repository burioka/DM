package android.buriokande;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.Calendar;
import android.widget.Toast;
import android.content.*;
import android.appwidget.AppWidgetManager;
import android.buriokande.DMTgtControl;
import android.app.*;

public class testActivity extends Activity{

    final int DLG_ID_SDATE = 0;
    final int DLG_ID_EDATE = 1;
    final int DLG_ID_NAIYO = 2;
    final int DLG_ID_VALID = 0;
    final int DLG_ID_EMPTY = 3;
    final int DLG_ID_COMPARE = 4;
    final int DLG_ID_OVER = 5;
    //現在日付を取得
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH)  ;
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    int validchk = 0;
    int SYear  =0;  
    int SMonth =0; 
    int SDay   =0;
    int EYear  =0;  
    int EMonth =0; 
    int EDay   =0; 
    private TextView txtS;
    private TextView txtE;
    private EditText txedt;

    /** Called when the activity is first created. */
    //TEMP あとでviewidごとに分岐させる処理にする
	private DatePickerDialog.OnDateSetListener SDateSetListener = 
    new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view,int year ,int monthOfYear,int dayOfMonth){
			SYear = year;
			SMonth = monthOfYear;
			SDay  = dayOfMonth  ;
			txtS.setText(
				Integer.toString(SYear) + "/" 
				+ Integer.toString(SMonth + 1 ) + "/"
				+ Integer.toString(SDay) );

		}
	};

    
	private DatePickerDialog.OnDateSetListener EDateSetListener = 
    new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view,int year ,int monthOfYear,int dayOfMonth){
			EYear = year;
			EMonth = monthOfYear ;
			EDay  = dayOfMonth ;
			txtE.setText(
				Integer.toString(EYear) + "/" 
				+ Integer.toString(EMonth +1 ) + "/"
				+ Integer.toString(EDay) );
		}
	};

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	//android.os.Debug.waitForDebugger();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
		DMDBHelper helper = new DMDBHelper(this);
		final SQLiteDatabase db = helper.getReadableDatabase();
		final Context con = getApplicationContext(); 
        //テキストの処理を設定
        txtS = (TextView)findViewById(R.id.StrSdate);
        txtE = (TextView)findViewById(R.id.StrEdate);
        txedt = (EditText)findViewById(R.id.EditText01);
        //開始ボタンの処理を設定
        Button btnS = (Button)findViewById(R.id.BtnSDate);
        Button btnE = (Button)findViewById(R.id.BtnEDate);
        Button btnPst = (Button)findViewById(R.id.BtnPst);
        Button btnGant = (Button)findViewById(R.id.GantRep);
        btnS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DLG_ID_SDATE);				
			}
		});
        btnE.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DLG_ID_EDATE);				
			}
		});
        //投稿ボタン編集
        btnPst.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sb = txedt.getText().toString().trim();


				Integer wSDATE = SYear*10000 + (SMonth+1)*100 + SDay;
				Integer wEDATE = EYear*10000 + (EMonth+1)*100 + EDay;

				//データ整合チェック

				if (!("".equals(sb) ||  sb.equals(null)) &
					     (wSDATE >= 19700101 ) & 
					     (wEDATE >= 19700101 )){
					if(wSDATE <= wEDATE){
						if(sb.length() <= 10 ){
							validchk = DLG_ID_VALID;
						}else{
							validchk = DLG_ID_OVER;
						}
					}else{
						validchk = DLG_ID_COMPARE;
						
					}
					
				} else{
					validchk = DLG_ID_EMPTY;
				}
				//insertを開始
				if(validchk == DLG_ID_VALID){
					db.beginTransaction();
					try {
						SQLiteStatement stmt;					
						stmt = db.compileStatement("insert into T_TgtNaiyo(Sdate, Edate,Naiyo) values (?, ?,?);");
	
						stmt.bindLong(1, wSDATE);
						stmt.bindLong(2, wEDATE);
						stmt.bindString(3, sb.toString());
						
						stmt.executeInsert();
						stmt.close();
						db.setTransactionSuccessful();
	
					} finally {
						
						db.endTransaction();
						Toast.makeText(con, "登録完了しました", Toast.LENGTH_LONG).show();
						AppWidgetManager manager=AppWidgetManager.getInstance(con);
				        DMTgtControl Tgtcon = new DMTgtControl();
	
				        String Tgt ;
						Tgt = Tgtcon.GetTgt(con, manager);
						RemoteViews view= new RemoteViews(getPackageName(), R.layout.main);
						view.setTextViewText(R.id.target, Tgt);
						ComponentName widget=new ComponentName(
							"android.buriokande","android.buriokande.DMWidgetProvider");
						manager.updateAppWidget(widget,view);
						
					}
				}else{
					showDialog(validchk);
				}

			}
		});
        btnGant.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Activity 呼び出し
		        Intent ViewActivity = new Intent(con, DMGantMake.class);
		        //Activityを新たに起動する時はこのフラグを立てないとActivityが起動しない。 
		        ViewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        con.startActivity(ViewActivity);
				
			}
		});
        //btnE.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
		//		// TODO Auto-generated method stub
				
		//		showDialog(DLG_ID_EDATE);
		//	}
		//});
    }    
    //ダイアログ作成
    protected Dialog onCreateDialog(int id){
    	if(id == DLG_ID_SDATE){
	    	//日付ダイアログ_開始
	    	return new DatePickerDialog(this ,SDateSetListener,year,month,day){

	    	};
	    } else if(id == DLG_ID_EDATE){
	    	//日付ダイアログ_終了
	    	return new DatePickerDialog(this ,EDateSetListener,year,month,day){};
	    } else if(id == DLG_ID_EMPTY){
	    	//INVALIDダイアログ_終了
	    	return new AlertDialog.Builder(this).setTitle(R.string.empty).setNegativeButton("OK",
	    			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//処理なし
				}
			}).create();
	    } else if(id == DLG_ID_COMPARE){
	    	return new AlertDialog.Builder(this).setTitle(R.string.compare).setNegativeButton("OK",
	    			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//処理なし
				}
			}).create();
	    } else if (id == DLG_ID_OVER){
	    	return new AlertDialog.Builder(this).setTitle(R.string.over).setNegativeButton("OK",
	    			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//処理なし
				}
			}).create();
	    }
        
    	return null;    
    }
}