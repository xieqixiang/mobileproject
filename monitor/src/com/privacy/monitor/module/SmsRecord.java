package com.privacy.monitor.module;

/**
 * ���ż�¼
 */
public class SmsRecord {
	
	/**���ն��ŵĺ���*/
	private String phone;
	
	/**��������*/
	private String messageContent;
	
	/**��������(����/����)*/
	private String messageType;
	
	/**����ʱ��*/
	private String receiveDate;
	
	/**���ŷ���ʱ��*/
	private String dateSent;
	
	/**���Ͷ��ŵĺ��������*/
	private String phoneAddress;
	
	/**��ȡ״̬��0��δ����1���Ѷ���*/
	private String readStatus;

	/**���ŷ���״̬(Ĭ����-1,������32,����ʧ��64,���ʹ�0(�������ʹﱨ��))*/
	private String sentStatus;
	
	/**�Ƿ���ɾ��(1δɾ��0��ɾ)*/
	private String deleteStatus;
	
	/**1Ϊ���ն��ţ�2Ϊ���Ͷ���*/
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
