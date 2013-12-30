package com.privacy.monitor.test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ʵ��Serializable�ӿڣ��������ݵĴ���
 * �ں�����Ҫ����Message.obj = MessageItem�Ķ���������
 * ��ô�ͱ���ʵ�ִ˽ӿ�
 */
public class MessageItem implements Parcelable {
	
	//����ID
	private int id;
	
	//��������  1�ǽ��յ��� 2�Ƿ���ȥ��
	private int type;
	
	//����Э�飬����\����
	private int protocol;
	
	//����ʱ��
	private long date;
	
	//�ֻ���
	private String phone;
	
	//����
	private String body;
	
	public MessageItem(){}
	
	public MessageItem(int id,int type,int protocol,long date,String phone,String body)  
    {  
        this.id=id;  
        this.type=type;  
        this.protocol=protocol;  
        this.date=date;  
        this.phone=phone;  
        this.body=body;  
    }

	public MessageItem(Parcel parcel) {
		this.id=parcel.readInt();  
        this.type=parcel.readInt();  
        this.protocol=parcel.readInt();  
        this.date=parcel.readLong();  
        this.phone=parcel.readString();  
        this.body=parcel.readString();  
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}  
	
	public String toString()  
    {  
        return "id="+id+",type="+type+",protocol="+protocol+",phone="+phone+",body="+body;  
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.type);
		dest.writeInt(this.protocol);
		dest.writeLong(this.date);
		dest.writeString(this.phone);
		dest.writeString(this.body);
	}  
	
	public static final Parcelable.Creator<MessageItem> CREATOR = new Parcelable.Creator<MessageItem>() {

		@Override
		public MessageItem createFromParcel(Parcel source) {

			return new MessageItem(source);
		}

		@Override
		public MessageItem[] newArray(int size) {
			return new MessageItem[size];
		}
	};
}
