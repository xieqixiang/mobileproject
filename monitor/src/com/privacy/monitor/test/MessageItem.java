package com.privacy.monitor.test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 实现Serializable接口，方便数据的传输
 * 在后面需要调用Message.obj = MessageItem的对象来传输
 * 那么就必须实现此接口
 */
public class MessageItem implements Parcelable {
	
	//短信ID
	private int id;
	
	//短信类型  1是接收到的 2是发出去的
	private int type;
	
	//短信协议，短信\彩信
	private int protocol;
	
	//发送时间
	private long date;
	
	//手机号
	private String phone;
	
	//内容
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
