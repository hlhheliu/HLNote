package com.example.hlnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyNoteActivity extends Activity {
	private EditText note;
	private String theNote;
	private String modifiedNote;
	private MyDatabaseHelper dbHelper;
	private int _id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_detail);
		ImageView back_list = (ImageView) findViewById(R.id.title_back);
		ImageView save_note = (ImageView) findViewById(R.id.title_save);
		note = (EditText) findViewById(R.id.the_note);
		Intent intent = getIntent();
		_id = intent.getIntExtra("modifyId", 0);
		dbHelper = new MyDatabaseHelper(this, "Note.db", null, 1);
		Cursor cursor = dbHelper.getWritableDatabase().rawQuery(
				"select content from note where _id =?",
				new String[] { _id + "" });
		cursor.moveToFirst();
		theNote = cursor.getString(0);
		note.setText(theNote);
		note.setSelection(theNote.length());

		back_list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				modifiedNote = note.getText().toString();
				if (!theNote.equals(modifiedNote)
						&& !TextUtils.isEmpty(modifiedNote)) {
					save();
				} else {
					finish();
				}
			}
		});
		save_note.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				modifiedNote = note.getText().toString();
				if (!theNote.equals(modifiedNote)
						&& !TextUtils.isEmpty(modifiedNote)) {
					save();
				} else {
					finish();
				}
			}
		});
	}

	public void onBackPressed() {
		modifiedNote = note.getText().toString();
		if (!theNote.equals(modifiedNote) && !TextUtils.isEmpty(modifiedNote)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ModifyNoteActivity.this);
			dialog.setTitle("保存");
			dialog.setMessage("是否保存修改？？");
			dialog.setPositiveButton("保存",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							save();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			dialog.show();
		} else {
			finish();
		}
	}

	public void save() {
		modifiedNote = note.getText().toString();
		String modifiedNoteTitle;
		if (modifiedNote.length() > 30) {
			modifiedNoteTitle = modifiedNote.substring(0, 30);
		} else {
			modifiedNoteTitle = modifiedNote;
		}
		dbHelper.getWritableDatabase().execSQL(
				"update note set content =?,title=? where _id = ?",
				new String[] { modifiedNote, modifiedNoteTitle, _id + "" });
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
