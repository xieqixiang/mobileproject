package com.privacy.system.domain;

/**
 * 存放文件属性
 */
public class SoundFileInfo {

	public static final String COL_FILE_ID="id";
	public static final String COL_FILE_NAME = "filename";
	public static final String COL_FILE_SIZE = "filesize";
	public static final String COL_FILE_PATH = "filepath";
	public static final String COL_FILE_START_TIME = "starttime";
	public static final String COL_FILE_END_TIME = "endtime";
	
	private String id,fileName, fileSize, fileTime, filePath, startTime, endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileTime() {
		return fileTime;
	}

	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
