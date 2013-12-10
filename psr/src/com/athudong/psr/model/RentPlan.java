package com.athudong.psr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ���ⷽ��
 * @author л����
 */
public class RentPlan implements Parcelable {

	private String strId;
	private String strDate;
	private String strStartTime;
	private String strStopTime;
	private String strpPrice;
	private String strRentStatus;
	private String strPlanType;

	/**get ��������*/
	public String getStrPlanType() {
		return strPlanType;
	}

	/**set ��������*/
	public void setStrPlanType(String strPlanType) {
		this.strPlanType = strPlanType;
	}

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

	/** get ���� */
	public String getStrDate() {
		return strDate;
	}

	/** set ���� */
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	/** get ����״̬ */
	public String getStrRentStatus() {
		return strRentStatus;
	}

	/** set ����״̬ */
	public void setStrRentStatus(String strRentStatus) {
		this.strRentStatus = strRentStatus;
	}

	public RentPlan() {
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

	public RentPlan(String strId, String strDate, String strStartTime,
			String strStopTime, String strpPrice, String strRentStatus,
			String strPlanType) {
		this.strId = strId;
		this.strDate = strDate;
		this.strStartTime = strStartTime;
		this.strStopTime = strStopTime;
		this.strpPrice = strpPrice;
		this.strRentStatus = strRentStatus;
		this.strPlanType = strPlanType;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.strId);
		dest.writeString(this.strDate);
		dest.writeString(this.strStartTime);
		dest.writeString(this.strStopTime);
		dest.writeString(this.strpPrice);
		dest.writeString(this.strRentStatus);
		dest.writeString(this.strPlanType);
	}

	public RentPlan(Parcel parcel) {
		this.strId = parcel.readString();
		this.strDate = parcel.readString();
		this.strStartTime = parcel.readString();
		this.strStopTime = parcel.readString();
		this.strpPrice = parcel.readString();
		this.strRentStatus = parcel.readString();
		this.strPlanType = parcel.readString();
	}
}
