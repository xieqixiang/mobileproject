package com.privacy.system.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 短信记录
 */
public class SMSRecord implements Parcelable {
	
	public static final String COL_ID = "id";
	public static final String COL_PHONE = "phone";
	public static final String COL_BODY = "sms_body";
	public static final String COL_SMS_TYPE = "sms_type";
	public static final String COL_DATE = "sms_date";
	public static final String COL_READ_STATUS = "sms_read_status";
	public static final String COL_SENT_STATUS = "sms_send_status";
	public static final String COL_UPLOAD_STATUS = "sms_upload_status";
	public static final String COL_NAME = "sms_name";
	
	private int id;
	
	/**手机号码*/
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
	
	/**姓名*/
	private String name;
	
	/**是否已上传到服务器*/
	private String uploadStatus;
	
	public SMSRecord(){};
	
	public SMSRecord(String address,String date,String date_send,String read,String type,String body,String name){
		this.phone = address;
		this.receiveDate = date;
		this.dateSent = date_send;
		this.readStatus = read;
		this.type = type;
		this.messageContent = body;
		this.name = name;
	}
	
	public SMSRecord(String address,String date,String date_send,String read,String type,String body){
		this.phone = address;
		this.receiveDate = date;
		this.dateSent = date_send;
		this.readStatus = read;
		this.type = type;
		this.messageContent = body;
	}
	
	public SMSRecord(Parcel parcel) {
		this.phone = parcel.readString();
		this.receiveDate = parcel.readString();
		this.dateSent = parcel.readString();
		this.readStatus = parcel.readString();
		this.type = parcel.readString();
		this.messageContent = parcel.readString();
		this.name = parcel.readString();
	}
	
	public SMSRecord(String phone, String date, String read,
			String type, String body) {
		this.phone = phone;
		this.dateSent = date;
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

	@Override
	public int describeContents() {
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.phone);
		dest.writeString(this.receiveDate);
		dest.writeString(this.dateSent);
		dest.writeString(this.readStatus);
		dest.writeString(this.type);
		dest.writeString(this.messageContent);
		dest.writeString(this.name);
	}
	
	public static final Parcelable.Creator<SMSRecord> CREATOR = new Parcelable.Creator<SMSRecord>() {

		@Override
		public SMSRecord createFromParcel(Parcel source) {

			return new SMSRecord(source);
		}

		@Override
		public SMSRecord[] newArray(int size) {
			return new SMSRecord[size];
		}
	};
	
}
