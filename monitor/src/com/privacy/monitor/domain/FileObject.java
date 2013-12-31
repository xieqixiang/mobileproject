package com.privacy.monitor.domain;

/**
 * 存放文件属性
 */
public class FileObject {
	
private String fileName, fileSize, fileTime, filePath;
	
	public FileObject(){
		
	}

	public FileObject(String fileName, String fileSize, String fileTime, String filePath) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileTime = fileTime;
		this.filePath = filePath;
	}

	public String getFielName() {
		return fileName;
	}

	public void setFielName(String fielName) {
		this.fileName = fielName;
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
