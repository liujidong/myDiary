package com.eoeAndroid.SQLite;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDbAdapter {
	private static final String TAG = "DiaryDbAdapter";
	
	private static final String DATABASE_NAME = "database";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE = "diary";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_CREATED = "created";

	private DatabaseHelper mDbHelper;//1
	private SQLiteDatabase mDb;//2

	private static final String DATABASE_CREATE = "create table IF NOT EXISTS "+DATABASE_TABLE+" ("+KEY_ROWID+" integer primary key autoincrement, "
			+ KEY_TITLE+" text not null, "+KEY_BODY+" text not null, "+KEY_CREATED+" text not null);";

	private final Context mCtx;//3

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			Log.i(TAG+":createDB=", DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//db.execSQL("DROP TABLE IF EXISTS diary");
			//onCreate(db);
		}
	}

	public DiaryDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public DiaryDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void closeclose() {
		mDbHelper.close();
	}

	public long createDiary(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		
		initialValues.put(KEY_CREATED, getCreatedStr());
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteDiary(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor getAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED }, null, null, null, null, null);
	}

	public Cursor getDiary(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED }, KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateDiary(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);

		args.put(KEY_CREATED, getCreatedStr());

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	private String getCreatedStr(){
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "年"
				+ calendar.get(Calendar.MONTH) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日"
				+ calendar.get(Calendar.HOUR_OF_DAY) + "时"
				+ calendar.get(Calendar.MINUTE) + "分";
		return created;
	}
}
