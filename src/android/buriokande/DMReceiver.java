package android.buriokande;
    
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
import android.util.Log;
    
// テキストレシーバー
// ブロードキャストレシーバーは BroadcastReceiverクラスを継承して作る
// どのアクションのインテントを処理するかは、AndroidManifest.xmlのintent-filterタグに記述
public class DMReceiver extends BroadcastReceiver {
    // インテントの受信処理
    @Override
    public void onReceive(Context context, Intent intent) {
        // パラメータの取得
       // Bundle bundle = intent.getExtras();
       // String text = bundle.getString("TEXT");
        // showToast(context, text);
        Log.v("DM","RECEIVER");
        
        //AlarmView.classは実行したいActivityに変更してください。
        Intent ViewActivity = new Intent(context, testActivity.class);
        //Activityを新たに起動する時はこのフラグを立てないとActivityが起動しない。 
        ViewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(ViewActivity);
        

    }
    
   // private static void showToast(Context context, String text) {
   //     Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
   //     toast.show();
   // }
}
    