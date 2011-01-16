package android.buriokande;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.content.Context;
import android.util.Log;
class DMDBHelper extends SQLiteOpenHelper {
	
	public DMDBHelper(Context context) {
		super(context, "DMDB.sqlite", null, 1);
		Log.w("DM","Mkconstr");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w("DM","Mkdbstart");
		db.beginTransaction();
		try {
			SQLiteStatement stmt;
			db.execSQL("create table T_TgtNaiyo (Sdate Integer , Edate Integer ,Naiyo text not null,Seq integer primary key);");
			stmt = db.compileStatement("insert into T_TgtNaiyo(Sdate,Edate,Naiyo) values (?, ?,?);");
			

			stmt.bindLong(1, 19990401);
			stmt.bindLong(2, 29999999);
			stmt.bindString(3, "テストデータ");
			stmt.executeInsert();
			db.setTransactionSuccessful();
			Log.w("DM","Mkdbsuccess");
		} finally {
			db.endTransaction();
			Log.w("DM","Mkdbeend");
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}