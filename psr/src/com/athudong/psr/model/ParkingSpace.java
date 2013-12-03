package com.athudong.psr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车位
 */
public class ParkingSpace implements Parcelable{
	
	/**停车场编号 */
	private String strParkNo;
	/**车位编号*/
	private String strSpaceNo;
	/**停车场名*/
	private String strParkName;
	/**停车场地址*/
	private String strParkAddress;
	/**停车场类型*/
	private String strParkType;
	/**停车场宣传图片的URL地址*/
	private String strParkPhotoUrl;
	/**车位名称*/
	private String strSpaceName;
	/**单价*/
	private String strRentPrice;
	/**停车开始时间*/
	private String strStartTime;
	/**停车结束时间*/
	private String strStopTime;
	/**交易车牌号*/
	private String strCarNum;
	/**交易手机号*/
	private String strTraderPhone;
	/**结算金额*/
	private String strPayMent;
	
	
	public String getStrStartTime() {
		return strStartTime;
	}
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}
	public String getStrStopTime() {
		return strStopTime;
	}
	public void setStrStopTime(String strStopTime) {
		this.strStopTime = strStopTime;
	}
	public String getStrCarNum() {
		return strCarNum;
	}
	public void setStrCarNum(String strCarNum) {
		this.strCarNum = strCarNum;
	}
	public String getStrTraderPhone() {
		return strTraderPhone;
	}
	public void setStrTraderPhone(String strTraderPhone) {
		this.strTraderPhone = strTraderPhone;
	}
	public String getStrPayMent() {
		return strPayMent;
	}
	public void setStrPayMent(String strPayMent) {
		this.strPayMent = strPayMent;
	}
	public String getStrParkNo() {
		return strParkNo;
	}
	public void setStrParkNo(String strParkNo) {
		this.strParkNo = strParkNo;
	}
	public String getStrSpaceNo() {
		return strSpaceNo;
	}
	public void setStrSpaceNo(String strSpaceNo) {
		this.strSpaceNo = strSpaceNo;
	}
	public String getStrParkName() {
		return strParkName;
	}
	public void setStrParkName(String strParkName) {
		this.strParkName = strParkName;
	}
	public String getStrParkAddress() {
		return strParkAddress;
	}
	public void setStrParkAddress(String strParkAddress) {
		this.strParkAddress = strParkAddress;
	}
	public String getStrParkType() {
		return strParkType;
	}
	public void setStrParkType(String strParkType) {
		this.strParkType = strParkType;
	}

	public String getStrParkPhotoUrl() {
		return strParkPhotoUrl;
	}
	public void setStrParkPhotoUrl(String strParkPhotoUrl) {
		this.strParkPhotoUrl = strParkPhotoUrl;
	}
	public String getStrSpaceName() {
		return strSpaceName;
	}
	public void setStrSpaceName(String strSpaceName) {
		this.strSpaceName = strSpaceName;
	}
	public String getStrRentPrice() {
		return strRentPrice;
	}
	public void setStrRentPrice(String strRentPrice) {
		this.strRentPrice = strRentPrice;
	}
	
	public ParkingSpace(){}



	public ParkingSpace(String strParkNo, String strSpaceNo,
			String strParkName, String strParkAddress, String strParkType,
			String strParkPhotoUrl, String strSpaceName, String strRentPrice,
			 String strStartTime, String strStopTime,
			String strCarNum, String strTraderPhone, String strPayMent) {
		this.strParkNo = strParkNo;
		this.strSpaceNo = strSpaceNo;
		this.strParkName = strParkName;
		this.strParkAddress = strParkAddress;
		this.strParkType = strParkType;
		this.strParkPhotoUrl = strParkPhotoUrl;
		this.strSpaceName = strSpaceName;
		this.strRentPrice = strRentPrice;
		this.strStartTime = strStartTime;
		this.strStopTime = strStopTime;
		this.strCarNum = strCarNum;
		this.strTraderPhone = strTraderPhone;
		this.strPayMent = strPayMent;
	}



	public static final Parcelable.Creator<ParkingSpace> CREATOR = new Parcelable.Creator<ParkingSpace>() {

		@Override
		public ParkingSpace createFromParcel(Parcel source) {
			return new ParkingSpace(source);
		}

		@Override
		public ParkingSpace[] newArray(int size) {
			return new ParkingSpace[size];
		}
	};
	
	public ParkingSpace(Parcel parcel){
		this.strParkNo = parcel.readString();
		this.strSpaceNo = parcel.readString();
		this.strParkName = parcel.readString();
		this.strParkAddress = parcel.readString();
		this.strParkType = parcel.readString();
		this.strParkPhotoUrl = parcel.readString();
		this.strSpaceName = parcel.readString();
		this.strRentPrice = parcel.readString();
		this.strStartTime = parcel.readString();;
		this.strStopTime = parcel.readString();;
		this.strCarNum = parcel.readString();;
		this.strTraderPhone = parcel.readString();;
		this.strPayMent = parcel.readString();
		
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.strParkNo);
		dest.writeString(this.strSpaceNo);
		dest.writeString(this.strParkName);
		dest.writeString(this.strParkAddress);
		dest.writeString(this.strParkType);
		dest.writeString(this.strParkPhotoUrl);
		dest.writeString(this.strSpaceName);
		dest.writeString(this.strRentPrice);
		dest.writeString(this.strStartTime);
		dest.writeString(this.strStopTime );
		dest.writeString(this.strCarNum);
		dest.writeString(this.strTraderPhone);
		dest.writeString(this.strPayMent);
	}
}
