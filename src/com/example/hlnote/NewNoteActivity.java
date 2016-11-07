package com.example.hlnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

public class NewNoteActivity extends Activity {
	private EditText note;
	private String newNote;
	private String newNoteTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_detail);
		ImageView back_list = (ImageView) findViewById(R.id.title_back);
		ImageView save_note = (ImageView) findViewById(R.id.title_save);
		note = (EditText) findViewById(R.id.the_note);
		back_list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				newNote = note.getText().toString();
				if (!TextUtils.isEmpty(newNote)) {
					save();
				} else {
					finish();
				}
			}
		});
		save_note.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				newNote = note.getText().toString();
				if (!TextUtils.isEmpty(newNote)) {
					save();
				} else {
					finish();
				}
			}
		});

	}

	public void onBackPressed() {
		newNote = note.getText().toString();
		if (!TextUtils.isEmpty(newNote)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					NewNoteActivity.this);
			dialog.setTitle("保存");
			dialog.setMessage("是否保存新笔记？？");
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
		if (newNote.length() > 30) {
			newNoteTitle = newNote.substring(0, 30);
		} else {
			newNoteTitle = newNote;
		}
		Intent intent = new Intent();
		intent.putExtra("new_note_title", newNoteTitle);
		intent.putExtra("new_note", newNote);
		setResult(RESULT_OK, intent);
		finish();
	}
}
