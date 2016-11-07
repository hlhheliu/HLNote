package com.example.hlnote;

import java.util.ArrayList;
import java.util.List;
import com.example.hlnote.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener, OnClickListener {
	private List<NoteL> CnoteList;
	private NoteAdapter adapter;
	private ListView notesList;
	private boolean boxShowed;
	private ImageView new_note;
	private LinearLayout bottomBar;
	private MyDatabaseHelper dbHelper;
	private Cursor cursor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		bottomBar = (LinearLayout) findViewById(R.id.bottom_bar);
		ImageView exit = (ImageView) findViewById(R.id.title_exit);
		exit.setOnClickListener(this);
		new_note = (ImageView) findViewById(R.id.new_note);
		new_note.setOnClickListener(this);
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		Button delete = (Button) findViewById(R.id.delete);
		delete.setOnClickListener(this);
		Button selAll = (Button) findViewById(R.id.select_all);
		selAll.setOnClickListener(this);
		Button reSel = (Button) findViewById(R.id.re_select);
		reSel.setOnClickListener(this);
		dbHelper = new MyDatabaseHelper(this, "Note.db", null, 1);
		notesList = (ListView) findViewById(R.id.notes_list);
		notesList.setOnItemClickListener(this);
		notesList.setOnItemLongClickListener(this);
		refreshList();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_note:
			Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.title_exit:
			onBackPressed();
			break;
		case R.id.select_all:
			for (int i = 0; i < CnoteList.size(); i++) {
				NoteL noteL = CnoteList.get(i);
				noteL.setChosen(true);
				CnoteList.set(i, noteL);
			}
			refreshList2();
			break;
		case R.id.re_select:
			for (int i = 0; i < CnoteList.size(); i++) {
				NoteL noteL = CnoteList.get(i);
				if (noteL.isChosen()) {
					noteL.setChosen(false);
				} else {
					noteL.setChosen(true);
				}
				CnoteList.set(i, noteL);
			}
			refreshList2();
			break;
		case R.id.cancel:
			unShowBox();
			break;
		case R.id.delete:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle("确认");
			dialog.setMessage("删除这些笔记？");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							List<Integer> _ids = new ArrayList<Integer>();
							for (int i = 0; i < CnoteList.size(); i++) {
								if (CnoteList.get(i).isChosen()) {
									_ids.add(CnoteList.get(i).get_id());
								}
							}
							if (_ids.size() != 0) {
								MyDatabaseHelper.deleteData(
										dbHelper.getWritableDatabase(), _ids);
								refreshList();
								unShowBox();
							}
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			dialog.show();
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				MyDatabaseHelper.insertData(dbHelper.getWritableDatabase(),
						data.getStringExtra("new_note_title"),
						data.getStringExtra("new_note"));
				refreshList();
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				refreshList();
			}
		default:
			break;
		}
	}

	public void refreshList() {
		CnoteList = new ArrayList<NoteL>();
		cursor = dbHelper.getWritableDatabase().rawQuery(
				"select _id,title from note order by _id", null);
		while (cursor.moveToNext()) {
			CnoteList.add(new NoteL(false, false, cursor.getInt(0), cursor
					.getString(1)));
		}
		if (CnoteList.size() == 0) {
			MyDatabaseHelper.insertData(dbHelper.getWritableDatabase(),
					"New a note here~", "");
			cursor = dbHelper.getWritableDatabase().rawQuery(
					"select _id,title from note order by _id", null);
			cursor.moveToFirst();
			CnoteList.add(new NoteL(false, false, cursor.getInt(0), cursor
					.getString(1)));
		}
		refreshList2();
	}

	public void refreshList2() {
		adapter = new NoteAdapter(MainActivity.this, R.layout.note_item,
				CnoteList);
		notesList.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (new_note.isClickable()) {
			int modifyId = CnoteList.get(position).get_id();
			Intent intent = new Intent(MainActivity.this,
					ModifyNoteActivity.class);
			intent.putExtra("modifyId", modifyId);
			startActivityForResult(intent, 2);
		} else {
			NoteL noteL = CnoteList.get(position);
			if (!noteL.isChosen()) {
				noteL.setChosen(true);
			} else {
				noteL.setChosen(false);
			}
			CnoteList.set(position, noteL);
			refreshList2();
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		showBox();
		return true;
	}

	public void showBox() {
		for (int i = 0; i < CnoteList.size(); i++) {
			NoteL noteL = CnoteList.get(i);
			noteL.setShowBox(true);
			CnoteList.set(i, noteL);
		}
		boxShowed = true;
		refreshList2();
		new_note.setClickable(false);
		bottomBar.setVisibility(View.VISIBLE);
	}

	public void unShowBox() {
		for (int i = 0; i < CnoteList.size(); i++) {
			NoteL noteL = CnoteList.get(i);
			noteL.setShowBox(false);
			noteL.setChosen(false);
			CnoteList.set(i, noteL);
		}
		boxShowed = false;
		refreshList2();
		bottomBar.setVisibility(View.INVISIBLE);
		new_note.setClickable(true);
	}

	public void onBackPressed() {
		if (boxShowed) {
			unShowBox();
		} else {
			if (dbHelper != null) {
				dbHelper.close();
			}
			finish();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
