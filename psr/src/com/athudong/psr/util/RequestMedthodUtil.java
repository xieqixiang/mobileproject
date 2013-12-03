package com.athudong.psr.util;

import java.util.HashMap;
import android.content.Context;

/**
 * 请求的各种方法所需的参数
 */
public class RequestMedthodUtil {
	private SharedPreUtil sharedPreUtil;
	
	public RequestMedthodUtil(Context context){
		sharedPreUtil = new SharedPreUtil(context);
	}
	/**
	 * 车位搜索
	 * @param account 注册号
	 * @param logNo 系统登录号
	 * @param cityName 所在城市
	 * @param dest 目的地
	 * @param longNow 经度
	 * @param latiNow 纬度
	 * @param btime 预订停车开始时间
	 * @param etime 预订停车结束时间
	 * @return hash生成XML需要。
	 */
	public HashMap<String,String> parkingSpaceSearch(String cityName,String dest,String longNow,String latiNow,String btime,String etime){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Search");
		requestMap.put("class","SearchSpace");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("cityName",cityName);
		requestMap.put("dest",dest);
		requestMap.put("longNow",longNow);
		requestMap.put("latiNow",latiNow);
		requestMap.put("btime",btime);
		requestMap.put("etime",etime);
		return requestMap;
	}
	
	/**
	 * 用户注册
	 * @param email 邮箱地址
	 * @param nickName 昵称
	 * @param password 密码
	 * @param name 真实姓名
	 * @param Idname 证件名称
	 * @param idno 证件号
	 * @param mobileNo 手机号
	 * @param carNo 车牌号
	 * @param payType 支付方式
	 * @param paycmpn 后台支付银行
	 * @param rent 是否有车位出租
	 * @return HashMap生成XML需要。
	 */
	public HashMap<String,String> register(String email,String nickName,String password,String name,String Idname,String idno,String mobileNo,String carNo,String payType,String paycmpn,String rent){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","New");
		requestMap.put("class","reg");
		requestMap.put("email","email");
		requestMap.put("nickname",nickName);
		requestMap.put("password",password);
		requestMap.put("name",name);
		requestMap.put("idName",Idname);
		requestMap.put("idno",idno);
		requestMap.put("mobileno", mobileNo);
		requestMap.put("carno",carNo);
		requestMap.put("paytype",payType);
		requestMap.put("paycmpn",paycmpn);
		requestMap.put("rent",rent);
		return requestMap;
	}
	
	/**
	 * 登录
	 * @param email 邮箱地址
	 * @param nickname 昵称
	 * @param password 密码
	 * @param account 注册号
	 * @return HashMap生成XML需要
	 */
	public HashMap<String,String> login(String email,String nickname,String password){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Login");
		requestMap.put("class","reg");
		requestMap.put("email",email);
		requestMap.put("nickname",nickname);
		requestMap.put("password",password);
		requestMap.put("account",sharedPreUtil.registAccount());
		return requestMap;
	}
	
	/**
	 * 修改注册信息
	 * @param accout 注册号
	 * @param logNo 系统登录号
	 * @param email 邮箱地址
	 * @param nickName 昵称
	 * @param password 密码
	 * @param name 真名
	 * @param Idname 证件名称
	 * @param idno 证件号
	 * @param mobileNo 手机号
	 * @param carNo 车牌号
	 * @param payType 支付方式
	 * @param paycmpn 后台支付银行
	 * @param rent 是否有车位出租
	 * @return HashMap生成XML需要
	 */
	public HashMap<String,String> upDateInfo(String email,String nickName,String password,String name,String Idname,String idno,String mobileNo,String carNo,String payType,String paycmpn,String rent){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Update");
		requestMap.put("class","reg");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("email",email);
		requestMap.put("nickname",nickName);
		requestMap.put("password",password );
		requestMap.put("name",name);
		requestMap.put("Idname",Idname);
		requestMap.put("idno",idno);
		requestMap.put("mobileno",mobileNo);
		requestMap.put("carno",carNo);
		requestMap.put("paytype",payType);
		requestMap.put("paycmpn",paycmpn);
		requestMap.put("rent",rent);
		return requestMap;
	}
	
	/**
	 * @param account 注册号
	 * @param logNo 系统登录号
	 * @param city 所在城市
	 * @param dist 所在区
	 */
	public HashMap<String,String> getAllParkings(String city,String dist){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Update");
		requestMap.put("class","reg");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("city",city);
		requestMap.put("dist",dist);
		return requestMap;
	}
	
	/**
	 * 车位主增加车位资料，未完成，需要提交什么文件不清晰
	 * @param parkno 停车场编号
	 * @param spacename 车位名称
	 * @param rentBDate 起租日期
	 * @param rentEDate 止租日期
	 * @param payeeCmpn 收款银行/公司
	 * @param payeeNo 收款账号
	 */
	public HashMap<String,String> addParking(String parkno,String spacename,String rentBDate,String rentEDate,String payeeCmpn,String payeeNo){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","New");
		requestMap.put("class","space");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("parkno",parkno);
		requestMap.put("spacename",spacename);
		requestMap.put("rentBDate",rentBDate);
		requestMap.put("rentEDate",rentEDate);
		requestMap.put("payeeCmpn",payeeCmpn);
		requestMap.put("payeeNo",payeeNo);
		return requestMap;
	}
	
	/**
	 * 获取自己在指定停车场所有的车位资料
	 * @param parkno 停车场编号
	 * @return
	 */
	public HashMap<String,String> getMyParking(String parkno){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Select");
		requestMap.put("class","space");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("parkno",parkno);
		return requestMap;
	}
	
	/**
	 * 车位主修改车位资料(未完成，有问题,需要画线)
	 * @param spaceNo 车位内部标识号
	 * @param spaceName 车位名称
	 * @param rentBDate 起租日期
	 * @param rentEDate 止租日期
	 * @param payeeCmpn 收款银行/公司
	 * @param payeeNo 收款账号
	 * @param status 当前状态
	 * @return 
	 */
	public HashMap<String,String> updateParkingInfo(String spaceNo,String spaceName,String rentBDate,String rentEDate,String payeeCmpn,String payeeNo,String status){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Update");
		requestMap.put("class","space");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("spaceno",spaceNo);
		requestMap.put("spacename",spaceName);
		requestMap.put("rentBDate",rentBDate);
		requestMap.put("rentEDate",rentEDate);
		requestMap.put("payeeCmpn",payeeCmpn);
		requestMap.put("payeeNo",payeeNo);
		requestMap.put("status",status);
		return requestMap;
	}
	
	/**
	 * 车位主修改放租车位方案(XML格式有错误)
	 * @param spaceNo
	 * @param rentBTime
	 * @param rentETime
	 */
	public HashMap<String,String> updateRentParkingInfo(String spaceNo,String rentBTime,String rentETime){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","New");
		requestMap.put("class","SpaceRent");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("spaceNo",spaceNo);
		requestMap.put("rentBTime",rentBTime);
		requestMap.put("rentETime",rentETime);
		return requestMap;
	}
	
	/**
	 * 车位预订
	 * @param spaceNo 车位编号
	 * @param mobileNo 本次交易手机号
	 * @param carNo 本次交易车牌号
	 * @param btime 预订停车开始时间
	 * @param etime 预订停车结束时间
	 */
	public HashMap<String,String> parkingSpaceReserve(String spaceNo,String mobileNo,String carNo,String btime,String etime){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","New");
		requestMap.put("class","Deal");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("spaceNo",spaceNo);
		requestMap.put("mobileNo",mobileNo);
		requestMap.put("carNo",carNo);
		requestMap.put("btime",btime);
		requestMap.put("etime",etime);
		return requestMap;
	}
	
	
	/**
	 * 历史车位预订查询
	 * @return
	 */
	public HashMap<String,String> historyBook(){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","Select");
		requestMap.put("class","Deal");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
	
		return requestMap;
	}
	
	/**
	 * 车位主查询车位出租详情
	 * @param btime 开始时间
	 * @param etime 结束时间
	 */
	public HashMap<String,String> profitTable(String btime,String etime){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","SelectRent");
		requestMap.put("class","Report");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("btime",btime);
		requestMap.put("etime",etime);
		return requestMap;
	}
	
	/**
	 * 用户忘记密码
	 */
	public HashMap<String,String> forget(){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","action");
		requestMap.put("class","reg");
		requestMap.put("account",sharedPreUtil.registAccount());
		requestMap.put("logNo",sharedPreUtil.logNum());
		requestMap.put("email",sharedPreUtil.email());
		return requestMap;
	}
	
	/**
	 * 用户在线心态检测
	 */
	public HashMap<String,String> onlineCheck(){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","UpdHB");
		requestMap.put("class","HeartBeat");
		requestMap.put("logNo",sharedPreUtil.logNum());
		return requestMap;
	}
}
