package com.server.aqordify;

public class Song {
	private String title, composer;
	private int recordedBy;
	
	public Song(String title, String composer, int recordedBy){
		this.title = title;
		this.composer = composer;
		this.recordedBy = recordedBy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public int getRecordedBy() {
		return recordedBy;
	}

	public void setRecordedBy(int recordedBy) {
		this.recordedBy = recordedBy;
	}
	
	
}
