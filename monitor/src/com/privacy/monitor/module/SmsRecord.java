package com.privacy.monitor.module;

/**
 * 短信记录
 */
public class SmsRecord {
	
	/**接收短信的号码*/
	private String phone;
	
	/**短信内容*/
	private String messageContent;
	
	/**短信类型(短信/彩信)*/
	private String messageType;
	
	/**接收时间*/
	private String receiveDate;
	
	/**短信发送时间*/
	private String dateSent;
	
	/**发送短信的号码归属地*/
	private String phoneAddress;
	
	/**读取状态（0表未读，1表已读）*/
	private String readStatus;

	/**短信发送状态(默认是-1,待发送32,发送失败64,已送达0(开启了送达报告))*/
	private String sentStatus;
	
	/**是否已删除(1未删，0已删)*/
	private String deleteStatus;
	
	/**1为接收短信，2为发送短信*/
	private String type;
	
	public SmsRecord(String address,String date,String date_send,String read,String type,String body){
		this.phone = address;
		this.receiveDate = date;
		this.dateSent = date_send;
		this.readStatus = read;
		this.type = type;
		this.messageContent = body;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	
	public String getPhoneAddress() {
		return phoneAddress;
	}

	public void setPhoneAddress(String phoneAddress) {
		this.phoneAddress = phoneAddress;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getDateSent() {
		return dateSent;
	}

	public void setDateSent(String dateSent) {
		this.dateSent = dateSent;
	}

	public String getSentStatus() {
		return sentStatus;
	}

	public void setSentStatus(String sentStatus) {
		this.sentStatus = sentStatus;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
