package com.athudong.psr.util;

import java.util.HashMap;
import android.content.Context;

/**
 * ����ĸ��ַ�������Ĳ���
 */
public class RequestMedthodUtil {
	private SharedPreUtil sharedPreUtil;
	
	public RequestMedthodUtil(Context context){
		sharedPreUtil = new SharedPreUtil(context);
	}
	/**
	 * ��λ����
	 * @param account ע���
	 * @param logNo ϵͳ��¼��
	 * @param cityName ���ڳ���
	 * @param dest Ŀ�ĵ�
	 * @param longNow ����
	 * @param latiNow γ��
	 * @param btime Ԥ��ͣ����ʼʱ��
	 * @param etime Ԥ��ͣ������ʱ��
	 * @return hash����XML��Ҫ��
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
	 * �û�ע��
	 * @param email �����ַ
	 * @param nickName �ǳ�
	 * @param password ����
	 * @param name ��ʵ����
	 * @param Idname ֤������
	 * @param idno ֤����
	 * @param mobileNo �ֻ���
	 * @param carNo ���ƺ�
	 * @param payType ֧����ʽ
	 * @param paycmpn ��̨֧������
	 * @param rent �Ƿ��г�λ����
	 * @return HashMap����XML��Ҫ��
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
	 * ��¼
	 * @param email �����ַ
	 * @param nickname �ǳ�
	 * @param password ����
	 * @param account ע���
	 * @return HashMap����XML��Ҫ
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
	 * �޸�ע����Ϣ
	 * @param accout ע���
	 * @param logNo ϵͳ��¼��
	 * @param email �����ַ
	 * @param nickName �ǳ�
	 * @param password ����
	 * @param name ����
	 * @param Idname ֤������
	 * @param idno ֤����
	 * @param mobileNo �ֻ���
	 * @param carNo ���ƺ�
	 * @param payType ֧����ʽ
	 * @param paycmpn ��̨֧������
	 * @param rent �Ƿ��г�λ����
	 * @return HashMap����XML��Ҫ
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
	 * @param account ע���
	 * @param logNo ϵͳ��¼��
	 * @param city ���ڳ���
	 * @param dist ������
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
	 * ��λ�����ӳ�λ���ϣ�δ��ɣ���Ҫ�ύʲô�ļ�������
	 * @param parkno ͣ�������
	 * @param spacename ��λ����
	 * @param rentBDate ��������
	 * @param rentEDate ֹ������
	 * @param payeeCmpn �տ�����/��˾
	 * @param payeeNo �տ��˺�
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
	 * ��ȡ�Լ���ָ��ͣ�������еĳ�λ����
	 * @param parkno ͣ�������
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
	 * ��λ���޸ĳ�λ����(δ��ɣ�������,��Ҫ����)
	 * @param spaceNo ��λ�ڲ���ʶ��
	 * @param spaceName ��λ����
	 * @param rentBDate ��������
	 * @param rentEDate ֹ������
	 * @param payeeCmpn �տ�����/��˾
	 * @param payeeNo �տ��˺�
	 * @param status ��ǰ״̬
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
	 * ��λ���޸ķ��⳵λ����(XML��ʽ�д���)
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
	 * ��λԤ��
	 * @param spaceNo ��λ���
	 * @param mobileNo ���ν����ֻ���
	 * @param carNo ���ν��׳��ƺ�
	 * @param btime Ԥ��ͣ����ʼʱ��
	 * @param etime Ԥ��ͣ������ʱ��
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
	 * ��ʷ��λԤ����ѯ
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
	 * ��λ����ѯ��λ��������
	 * @param btime ��ʼʱ��
	 * @param etime ����ʱ��
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
	 * �û���������
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
	 * �û�������̬���
	 */
	public HashMap<String,String> onlineCheck(){
		HashMap<String,String> requestMap = new HashMap<String, String>();
		requestMap.put("action","UpdHB");
		requestMap.put("class","HeartBeat");
		requestMap.put("logNo",sharedPreUtil.logNum());
		return requestMap;
	}
}
