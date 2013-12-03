package com.athudong.psr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ���ⷽ��
 * 
 * @author л����
 */
public class RentPlan implements Parcelable {

	private String strId;
	private String strStartTime;
	private String strStopTime;
	private String strpPrice;

	/** get ��λ��� */
	public String getStrId() {
		return strId;
	}

	/** set ��λ��� */
	public void setStrId(String strId) {
		this.strId = strId;
	}

	/** get ����ʱ�� */
	public String getStrStartTime() {
		return strStartTime;
	}

	/** set ����ʱ�� */
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}

	/** get ֹ��ʱ�� */
	public String getStrStopTime() {
		return strStopTime;
	}

	/** set ֹ��ʱ�� */
	public void setStrStopTime(String strStopTime) {
		this.strStopTime = strStopTime;
	}

	/** get ����Сʱ/���� */
	public String getStrpPrice() {
		return strpPrice;
	}

	/** set ����Сʱ/���� */
	public void setStrpPrice(String strpPrice) {
		this.strpPrice = strpPrice;
	}

	public RentPlan() {
	}

	public RentPlan(String strId, String strStartTime, String strStopTime,
			String strpPrice) {
		this.strId = strId;
		this.strStartTime = strStartTime;
		this.strStopTime = strStopTime;
		this.strpPrice = strpPrice;
	}

	public static final Parcelable.Creator<RentPlan> CREATOR = new Parcelable.Creator<RentPlan>() {

		@Override
		public RentPlan createFromParcel(Parcel source) {

			return new RentPlan(source);
		}

		@Override
		public RentPlan[] newArray(int size) {
			return new RentPlan[size];
		}
	};

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.strId);
		dest.writeString(this.strStartTime);
		dest.writeString(this.strStopTime);
		dest.writeString(this.strpPrice);
	}

	public RentPlan(Parcel parcel) {
		this.strId = parcel.readString();
		this.strStartTime = parcel.readString();
		this.strStopTime = parcel.readString();
		this.strpPrice = parcel.readString();
	}
}
