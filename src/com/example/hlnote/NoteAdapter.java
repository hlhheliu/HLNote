package com.example.hlnote;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class NoteAdapter extends ArrayAdapter<NoteL> {
	private int resourceId;

	public NoteAdapter(Context context, int textViewResourceId,
			List<NoteL> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		NoteL noteL = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.note_checked = (CheckBox) view
					.findViewById(R.id.note_checked);
			viewHolder.note_title = (TextView) view
					.findViewById(R.id.note_title);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.note_title.setText(noteL.getNoteTitle());
		if (noteL.isShowBox())
			viewHolder.note_checked.setVisibility(View.VISIBLE);
		viewHolder.note_checked.setChecked(noteL.isChosen());
		return view;
	}

	class ViewHolder {
		CheckBox note_checked;
		TextView note_title;
	}

}
