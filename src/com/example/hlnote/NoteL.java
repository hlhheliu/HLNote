package com.example.hlnote;

public class NoteL {
	private boolean chosen;
	private boolean showBox;
	private int _id;
	private String noteTitle;

	public NoteL(boolean chosen, boolean showBox, int _id, String noteTitle) {
		super();
		this.chosen = chosen;
		this.showBox = showBox;
		this._id = _id;
		this.noteTitle = noteTitle;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public boolean isShowBox() {
		return showBox;
	}

	public void setShowBox(boolean showBox) {
		this.showBox = showBox;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

}
