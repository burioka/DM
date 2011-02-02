package android.buriokande;
import java.util.Calendar;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.util.Log;
import android.buriokande.DMTgtControl;
import android.content.*;
import android.widget.*;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.Resources;
public class DMGantMake extends Activity{	
	/**
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @param hoho.os.bundle
	 *　@author BURIOKA
	 */
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT; 
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT; 
    private CheckBox[] checkBox ;
	public void onCreate(Bundle savedInstanceState){
		//android.os.Debug.waitForDebugger(); 
		super.onCreate(savedInstanceState);
        //setContentView(R.layout.gant);
		final Context con = getApplicationContext();
		DMTgtControl Tgtcon = new DMTgtControl();
		AppWidgetManager manager=AppWidgetManager.getInstance(con);
		
        GantData[] gantdata = Tgtcon.GetGantData(con, manager, year, month , day);

        //GantData[] gantdata = Tgtcon.GetGantData(con, manager, 2010, 11 , 20);
		
        Integer[][] Xaxis = Tgtcon.getXaxis(); 
        Log.v("DMXaxis",Integer.toString(Xaxis[0].length));
        Mklayout(con,gantdata,Xaxis);
        

	}
	public void Mklayout(Context con,GantData[] Gant ,Integer[][] Xaxis){
		TableLayout tableLayout = new TableLayout(con);
		tableLayout.setColumnStretchable(0, true); 
		setContentView(tableLayout);
		
		TableRow[] tableRow1 = MktitleLay(con,Xaxis);

		tableLayout.addView(tableRow1[0], new TableLayout.LayoutParams(FP, WC));
		tableLayout.addView(tableRow1[1], new TableLayout.LayoutParams(FP, WC));
		tableLayout.addView(tableRow1[2], new TableLayout.LayoutParams(FP, WC));
		if (MkTgtLay(con,Gant,Xaxis) != null){
			TableRow[] tableRow2 = MkTgtLay(con,Gant,Xaxis);
			for (int i=0;i<tableRow2.length;i++){
				tableLayout.addView(tableRow2[i], new TableLayout.LayoutParams(FP, WC));
			}
		}
		TableRow Btn1 = MkDelBtn(con,Gant);
		tableLayout.addView(Btn1);
		
	}
	private TableRow[] MktitleLay(Context con,Integer[][] Xaxis ){
		TableRow[] tablerow = new TableRow[3];
		TextView text1;
		int i_max = Xaxis[0].length;
		final Integer txtwdth = 25;
		final Integer titleCol = R.color.wyellow;
	
        TableRow.LayoutParams rowLayout1 = new TableRow.LayoutParams();
        rowLayout1.setMargins(2, 2, 2, 2);
        TableRow.LayoutParams rowLayout2 = new TableRow.LayoutParams();

        TableRow.LayoutParams rowLayout3 = new TableRow.LayoutParams();
        TableRow.LayoutParams rowLayoutCkbx = new TableRow.LayoutParams();
        rowLayout3.setMargins(2, 2, 2, 2);
		//tablerow配列の初期化
        for (int i = 0;i<3;i++){
			tablerow[i] =  new TableRow(con);
		}
        //月を表示用に変換
        for(int i =0;i<i_max;i++){
        	Xaxis[1][i]= Xaxis[1][i]+1;
        }
        //年月日の文字列を挿入
        TextView textnen = new TextView(con);
        TextView textgetu = new TextView(con);
        TextView textniti = new TextView(con);
        rowLayout2.span = 2;
        rowLayout2.setMargins(2, 2, 2, 2);
        textnen.setText("年(西暦)");
        textnen.setBackgroundColor(getColorId(R.color.wblue));
        textgetu.setText("月");
        textgetu.setBackgroundColor(getColorId(R.color.wblue));
        textniti.setText("日");
        textniti.setBackgroundColor(getColorId(R.color.wblue));
        tablerow[0].addView(textnen,rowLayout2);
        tablerow[1].addView(textgetu,rowLayout2);
        tablerow[2].addView(textniti,rowLayout2);
        
        Integer temp1 = 0;
        int k = 0;
		text1 = new TextView(con);
		for (int i=0;i<3;i++){
        	temp1 = Xaxis[i][0];
        	k=0;
        	
        	for (int j=0;j<i_max+1;j++){
       			if(j<i_max){
	        		if(temp1.equals(Xaxis[i][j])){
	       				k = k+1;
	        		}else{
	        			text1 = new TextView(con);
	        			text1.setText(Integer.toString(temp1));
	        			text1.setWidth(txtwdth);
	        			text1.setBackgroundColor(getColorId(titleCol));
	        	        if (k==1){
	        	        	tablerow[i].addView(text1);
	        	        }else if (k>=2){
		       				rowLayout1.span = k;
		       				//tablerow.addView(text1,rowLayout1);    
		       				tablerow[i].addView(text1,rowLayout1);       	        	
	        	        }
	        			k=1;
	        			temp1 = Xaxis[i][j]; 
	        			text1 = null;
	        		}
       			}else {
       				text1 = new TextView(con);
       				text1.setText(Integer.toString(temp1));
       				text1.setWidth(txtwdth);
       				text1.setBackgroundColor(getColorId(titleCol));
        	        if (k==1){
        	        	tablerow[i].addView(text1);
        	        }else if (k>=2){
	       				rowLayout1.span = k;
	       				
	        			//tablerow.addView(text1,rowLayout1);    
	       				tablerow[i].addView(text1,rowLayout1); 
        	        }
        	        text1 = null;
       			}
	        }
        }
		
        return tablerow;
	}
	private TableRow[] MkTgtLay(Context con,GantData[] Gant ,Integer[][] Xaxis){
		int i_maxG ;
		int i_maxX = Xaxis[0].length;
		if(Gant == null){
			i_maxG = 0;
			return null;
		}else{
			i_maxG = Gant.length;
			TableRow[] tablerow = new TableRow[i_maxG];
			checkBox = new CheckBox[i_maxG];
			
			Integer[] temp1 = new Integer[i_maxX];
	        TextView Circle;
	        TextView Space ;
	        TextView Naiyo ;

	         
			//月を表示用に変換
	        for(int i =0;i<i_maxX;i++){
	        	Xaxis[1][i]= Xaxis[1][i];
	        	temp1[i] = Xaxis[0][i]*10000 +  Xaxis[1][i]*100 + Xaxis[2][i];
	        }
			//tablerow配列の初期化
	        for (int i = 0;i<i_maxG;i++){
				tablerow[i] =  new TableRow(con);
			}
	
			for(int i = 0;i<i_maxG;i++){
				Naiyo = new TextView(con);
				checkBox[i]  = new CheckBox(con);
				checkBox[i].setWidth(7);
				checkBox[i].setHeight(7);
				tablerow[i].addView(checkBox[i]);
				Naiyo.setText(Gant[i].Naiyo);
				tablerow[i].addView(Naiyo);
				Naiyo =null;
				
				for(int j=0 ;j<i_maxX;j++ ){
					if(Gant[i].Date1 <= temp1[j]){
						if (Gant[i].Date2>= temp1[j]){
					        Circle = new TextView(con);
					        Circle.setText("○");
					        Circle.setBackgroundColor(getColorId(R.color.wred));
							tablerow[i].addView(Circle);
							Circle = null;
						}else{
							Space = new TextView(con);
							Space.setText("×");
							tablerow[i].addView(Space);
							Space = null;
						}
					}else {
						Space = new TextView(con);
						Space.setText("×");
						tablerow[i].addView(Space);
						Space = null;
					}
				}
			}
			return tablerow;
		}
		

		
	}
	private TableRow MkDelBtn(Context con, GantData[] gant){
		Button Btn1 = new Button(con);
		int iw_max = 0;
		if (gant == null){
			 
		}else{
			iw_max = gant.length;
		}
		final int  i_max = iw_max;
		TableRow tablerow = new TableRow(con);
		Btn1.setText("削除");
		TableRow.LayoutParams rowLayout2 = new TableRow.LayoutParams();
		rowLayout2.span = 3;
		final GantData[] gant1 = gant;
		final Context con1 = con;
		final DMTgtControl Tgtcon = new DMTgtControl();
		Btn1.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				for(int i = 0;i < i_max;i++){
					if (checkBox[i].isChecked()){
						Tgtcon.TgtDel(con1, gant1[i].Seq);
					}
				}
			}		
		});
		tablerow.addView(Btn1,rowLayout2);
		return tablerow;
	}
	private int getColorId(int wcolor){
		int id  = 0;
		Resources res = getResources();
		id = res.getColor(wcolor);
		return id;
	}
}