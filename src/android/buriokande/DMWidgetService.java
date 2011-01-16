package android.buriokande;
//import java.util.*;

import android.app.*;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class DMWidgetService extends Service{
	private static final String ACTION_BTNCLICK =
		"android.buriokande.ACTION_BTNCLICK";
	@Override
	public void onStart(Intent intent , int startId){

		super.onStart(intent, startId);
		RemoteViews view= new RemoteViews(getPackageName(), R.layout.main);
		Intent newintent = new Intent();
		newintent.setAction(ACTION_BTNCLICK);
		PendingIntent pending=PendingIntent.getService(this,0,newintent,0);
		view.setOnClickPendingIntent(R.id.button1,pending);
		Log.v("DM","ONST");
		if (ACTION_BTNCLICK.equals(intent.getAction())){
			btnClicked(view);
			Log.v("DM","BTNCLICK");


		}
		AppWidgetManager manager=AppWidgetManager.getInstance(this);
		ComponentName widget=new ComponentName(
			"android.buriokande","android.buriokande.DMWidgetProvider");
		manager.updateAppWidget(widget,view);
		Log.v("DM","SETWIDG");		
	}
	@Override
	public IBinder onBind(Intent intent){
		Log.v("DM","BTNBIL");
		return null;
		
	}

	public void btnClicked(RemoteViews view) {
		Log.v("DM","SETSTR");
		//res
		Intent Views = new Intent();
		Views.setAction("android.buriokande.ViewAction.VIEW");
		sendBroadcast(Views);
		//view.setTextViewText(R.id.target, "aso");
	}
}