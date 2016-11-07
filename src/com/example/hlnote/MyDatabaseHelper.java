package com.example.hlnote;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	final String CREATE_TABLE = "create table note(_id integer primary key autoincrement,"
			+ "title nvarchar(30),content text)";

	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public static void insertData(SQLiteDatabase db, String title,
			String content) {
		db.execSQL("insert into note values(null,?,?)", new String[] { title,
				content });
	}

	public static void deleteData(SQLiteDatabase db, List<Integer> _ids) {
		String _idsString = "";
		for (int _id : _ids) {
			_idsString += _id + ",";
		}
		_idsString = _idsString.substring(0, _idsString.length() - 1);
		db.execSQL("delete from note where _id in (" + _idsString + ")");
	}
}
