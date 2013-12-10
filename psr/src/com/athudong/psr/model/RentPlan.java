package com.athudong.psr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 放租方案
 * @author 谢启祥
 */
public class RentPlan implements Parcelable {

	private String strId;
	private String strDate;
	private String strStartTime;
	private String strStopTime;
	private String strpPrice;
	private String strRentStatus;
	private String strPlanType;

	/**get 方案类型*/
	public String getStrPlanType() {
		return strPlanType;
	}

	/**set 方案类型*/
	public void setStrPlanType(String strPlanType) {
		this.strPlanType = strPlanType;
	}

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

	/** get 日期 */
	public String getStrDate() {
		return strDate;
	}

	/** set 日期 */
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	/** get 放租状态 */
	public String getStrRentStatus() {
		return strRentStatus;
	}

	/** set 放租状态 */
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
