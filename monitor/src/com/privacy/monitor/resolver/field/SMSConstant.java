package com.privacy.monitor.resolver.field;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 短信表字段和提供者的uri
 */
public interface SMSConstant extends BaseColumns {

	// 内容地址
	public static final Uri CONTENT_URI = Uri.parse("content://sms");

	// //////////SMS数据库列名字段，其实还有很多，目前没用就不列举了
	public static final String ID = "_id";
	public static final String THREAD_ID = "thread_id";
	public static final String ADDRESS = "address";
	public static final String M_SIZE = "m_size";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String DATE_SENT = "date_sent";
	public static final String PROTOCOL = "protocol";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String REPLY_PATH_PRESENT = "replay_path_present";
	public static final String SUBJECT = "subject";
	public static final String BODY = "body";
	public static final String SERVICE_CENTER = "service_center";
	public static final String LOCKED = "locked";
	public static final String SIM_ID = "sim_id";
	public static final String ERROR_CODE = "error_code";
	public static final String SEEN = "seen";
	public static final String TYPE = "type";

	// //////////短信的状态
	public static final int MESSAGE_TYPE_ALL = 0; // 所有

	public static final int MESSAGE_TYPE_INBOX = 1; // 收件箱

	public static final int MESSAGE_TYPE_SENT = 2; // 已发送

	public static final int MESSAGE_TYPE_DRAFT = 3; // 草稿

	public static final int MESSAGE_TYPE_OUTBOX = 4; // 待发送

	public static final int MESSAGE_TYPE_FAILED = 5; // 发送失败 for failed outgoing
														// messages

	public static final int MESSAGE_TYPE_QUEUED = 6; // 定时发送 for messages to
														// send later

	public static final int PROTOCOL_SMS = 0;// SMS_PROTO

	public static final int PROTOCOL_MMS = 1;// MMS_PROTO
}
