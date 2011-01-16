package android.buriokande;


import java.util.Calendar;

import android.buriokande.DMDBHelper;
import android.buriokande.DMTgtControl;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.util.Log;
import android.content.*;
import android.widget.*;
import android.database.Cursor; //db操作用　跡で分離
import android.database.sqlite.SQLiteDatabase;

public class DMWidgetProvider extends AppWidgetProvider {
	private DMDBHelper helper;
	private String Tgt ;
	/** Called when the activity is first created. */
    @Override
    public void onEnabled(Context context) {
    	super.onEnabled(context);
    	Log.v("DM","onEnabled");
    }
    @Override
    public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appwidgetlds){
       
    	Log.v("DM","OnUpdate");
    	super.onUpdate(context,appWidgetManager,appwidgetlds);
        // ホームスクリーンウィジェットのイベント処理を担当するサービスの起動
        Intent intent = new Intent(context, DMWidgetService.class);
        context.startService(intent);
        DMTgtControl  Tgtcon = new DMTgtControl();
        Tgt = Tgtcon.GetTgt(context, appWidgetManager);
		RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.main);
		remoteView.setTextViewText(R.id.target, Tgt);
		Log.v("DM","Tgt:" + Tgt);
        ComponentName thisWidget = new ComponentName(context, DMWidgetProvider.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteView);
        

    }
    @Override
    public void onDeleted(Context context,int[] appWidgettlds){
    	Log.v("DM","onDeleted");
    	super.onDeleted(context,appWidgettlds);
    	
    }
    public void onDisabled(Context context){
    	Log.v("DM","onDisabled");
    	super.onDisabled(context);
    }
    public void onReceive(Context context,Intent intent){
    	Log.v("DM","onReceive");
    	super.onReceive(context,intent);
    }
}