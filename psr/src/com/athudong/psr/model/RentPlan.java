package com.athudong.psr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 放租方案
 * 
 * @author 谢启祥
 */
public class RentPlan implements Parcelable {

	private String strId;
	private String strStartTime;
	private String strStopTime;
	private String strpPrice;

	/** get 车位编号 */
	public String getStrId() {
		return strId;
	}

	/** set 车位编号 */
	public void setStrId(String strId) {
		this.strId = strId;
	}

	/** get 放租时间 */
	public String getStrStartTime() {
		return strStartTime;
	}

	/** set 放租时间 */
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}

	/** get 止租时间 */
	public String getStrStopTime() {
		return strStopTime;
	}

	/** set 止租时间 */
	public void setStrStopTime(String strStopTime) {
		this.strStopTime = strStopTime;
	}

	/** get 放租小时/单价 */
	public String getStrpPrice() {
		return strpPrice;
	}

	/** set 放租小时/单价 */
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
