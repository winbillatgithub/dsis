package com.as.entity;

import java.util.HashMap;
import java.util.Map;

public class Constant {

	public static final Integer _SUCCESS_ = 1;
	public static final Integer _FAILED_= 0;

	public static final String _RESULT_ = "result";
	public static final String _REASON_ = "reason";

	public static final String _PATH_ = "path";

	public static final String _DELETE_SUCCESS_ = "删除成功!";
	public static final String _UPDATE_SUCCESS_ = "更新成功!";
	public static final String _SAVE_SUCCESS_ = "保存成功!";
	public static final String _SAVE_FILE_INFO_FAILED_ = "保存文件信息失败!";
	public static final String _SAVE_META_DATA_FAILED_ = "保存元数据信息失败!";
	public static final String _ADD_KEY_WORDS_FAILED_ = "动态添加字典失败!";

	public static final String _ERROR_OCCURED_ = "发生系统错误!";
	public static final String _NOT_FOUND_COURT_ = "没有找到法庭信息!";
	public static final String _NOT_FOUND_CASE_CAUSE_ = "没有找到案由信息!";
	public static final String _NOT_FOUND_CASE_TYPE_ = "没有找到案件类型信息!";

	/**
	 * 课程分类的每层最大分类个数
	 */
	public static final Integer _MAX_CATEGORY_NUM_ = 100;
	public static final Integer _COURSE_PAGESIZE_ = 4;
	public static final Integer _COURSE_PAGENUM_ = 5;
	/**
	 * 笔记
	 */
	public static final Integer _NOTE_PAGESIZE_ = 4;
	public static final Integer _NOTE_PAGENUM_ = 5;
	/**
	 * 最大目录/课时数
	 */
	public static final Integer _MAX_CATELOGS_ = 1000;
	/**
	 * 精确查找类型
	 */
	public static final String _CAUSE_ = "cause";
	public static final String _COURT_ = "court";
	public static final String _JUDGE_ = "judge";
	public static final String _LITIGANT_ = "litigant";
	public static final String _EXECUTOR_ = "executor";
	public static final String _EXECUTED_ = "executed";
	public static final String _THIRDPART_ = "thirdpart";
	public static final String _LAWYER_ = "lawyer";
	public static final String _LAWFIRM_ = "lawfirm";
	public static final String _LEGALPERSON_ = "legalPerson";

	/**
	 * 重定向Url，必须和urlrewrite.xml中的同步
	 */
	// 首页
    public static final String _url_index_ = "index.jsp";
    // 加入课程
    public static final String _url_joinCourse_ = "/portal/course/progress/join";
    // 我的课程进度
    public static final String _url_myCourse_ = "/portal/course/progress/index";
    // 典型案例
    public static final String _url_center_ = "/portal/mycenter/studyCenter/index";
    // 案由查找
    public static final String _url_cause_ = "/portal/searchfile/searchFile/cause";
    // 当事人查找
    public static final String _url_litigant_ = "/portal/searchfile/searchFile/litigant";
    // 法院、法官查找
    public static final String _url_court_ = "/portal/searchfile/searchFile/court";
    // 新闻列表页面
	public static final String _url_newsList_ = "/portal/news/newsList/index";
	// 新闻页明细页面
	public static final String _url_newsDetail_ = "/portal/news/newsDetail/index";
	// 登陆
	public static final String _url_login_ = "/portal/login/login/loginIndex";
	// 注册
	public static final String _url_register_ = "/portal/login/register/registerIndex";
	// 联系我们  
	public static final String _url_contactus_ = "/portal/contact/contactus/contactusIndex";
	// 自助服务 
	public static final String _url_member_ = "/portal/user/selfservice/personalInfo";
	public static final String _url_member_edit_ = "/portal/user/selfservice/editInfo";
	public static final String _url_password_edit_ = "/portal/user/selfservice/editPassword";
	public static final String _url_logout_ = "/portal/user/selfservice/logout";
	/**
	 * 验证码
	 */
	public static final String _OPERATION_CHECK_CODE = "operationCheckCode";
	
	
	/**
	 * cookie key
	 */
	public static final String _COOKIE_KEY = "COOKIEID";
	
	public static final String _SESSION_KEY = "SESSIONID";
	
	/**
	 * 用户名的Session
	 */
	public static final String 	_SESSION_USERNAME = "sessionUser";
	
	/**
	 * 前台的用户名和密码能够记录的Session时间长度(默认30天)
	 */
	public static final int _SESSION_OUT_OF_DATE_TIME = 60 * 60 * 24 * 30;
	
	/**
	 * 用户名的Session
	 */
	public static final String 	_SESSION_CONTENT_PLAIN = "contentPlain";
	/**
	 * 用户名的Session
	 */
	public static final String 	_SESSION_CONTENT_HTML = "contentHtml";
	
	
	/**
	 * 各个系统对应cookie的Key值
	 */
	public static final String _COOKIE_FILESEARCH_USERNAME = "FileSearch_UserName";
	public static final String _COOKIE_FILESEARCH_PASSWORD= "FileSearch_Password";
	public static final Map<String, String> _COOKIEKEYS = new HashMap<String, String>();
	static{
		//_COOKIEKEYS.put("/admin", _ADMIN_SESSION_PRE_STR);//后台管理
		//_COOKIEKEYS.put("/portal", _PORTAL_SESSION_PRE_STR); //portal
		//_COOKIEKEYS.put("/FileSearch", "/FileSearch");	
	}
	
	/**
	 * 对称密钥
	 */
	public static final String RSA_MODULUS = "RSAModulus";
	
	/**
	 * 验证码
	 */
	public static final String OPERATION_CHECK_CODE = "operationCheckCode";
	
	/**
	 * 用户 session key
	 */
	public static final String USER_SESSION_KEY = "userSessionKey";
	
	/**
	 * redis session key
	 */
	public static final String REDIS_SESSION_KEY = "redisSessionKey";
	
	/**
	 * redis cache key
	 */
	public static final String REDIS_CACHE_KEY = "redisCacheKey";
	
	/**
	 * cookie key
	 */
	public static final String COOKIE_KEY = "SESSIONID";
	
	/**
	 * 持续监测上传间隔时间(秒为单位)
	 */
	public static int MONITOR_UPLOAD_TIME;
	
	/**
	 * 文件服务器域名地址
	 */
	public static String FASTDFS_FILE_PATH;
	
	/**
	 * 用户下发开关
	 */
	public static boolean PENSION_USER_DOWNLOAD = true; 
	
	/**
	 * AES对称加密算法 
	 */
	public static String AES = "jxWIGwIkI31rzk056/wsFNw1Z0O7d1nmbBRnIY7beEE=";
	                            
	/**
	 * 发送短信临时地址
	 */
	public static String TEMP_URL;
	
	/**
	 * 是否踢出
	 */
	public static boolean IS_KICKEDOUT = false;
	
	
	/**
	 * 查看数据的延迟时间
	 */
	public static final int MONITOR_DELAY_TIME = 20;
	
	/**
	 * 每页显示多少秒的数据
	 */
	public static final int MONITOR_TIME = 6;
	
	/**
	 * 删除标示
	 */
	public static final int IS_DELETE_0 = 0;//未删除
	public static final int IS_DELETE_1 = 1;//已删除
	
	/**
	 * 禁用标示
	 */
	public static final int IS_DISABLE_0 = 0;//未禁用
	public static final int IS_DISABLE_1 = 1;//已禁用
	
	/**机构类型：1 政府机构  */
	public static final int GOVERNMENT_ORG = 1;//政府机构
	/**机构类型：2 医疗机构  */
	public static final int MEDICAL_ORG = 2;//医疗机构
	/**机构类型：3 养老机构  */
	public static final int PENSION_ORG = 3;//养老机构
	/**机构类型：4 云平台 */
	public static final int SERVER = 4;//云平台
	/**机构类型：5 前置机  */
	public static final int CLIENT = 5;//前置机
	/**机构类型：6 个人门户  */
	public static final int PERSONAL = 6;//个人门户
	
	/**
	 * 养老机构与医疗机构关联状态
	 */
	public static final int NO_RELATION = 0; //未关联
	public static final int PENSION_TO_MEDICAL = 1;//待关联，养老机构发起的申请
	public static final int MEDICAL_TO_PENSION = 2;//待关联，医疗机构发起的申请
	public static final int HAS_RELATION = 3;//已关联
	public static final int REJECT_RELATION = 4;//拒绝关联
	public static final int REMOVE_RELATION = 5;//解除关联
	
	/**用户关注状态：0 未关注  */
	public static final int USER_NO_RELATION = 0;//未关注
	/**用户关注状态：1 已关注  */
	public static final int USER_ALREADY_RELATION = 1;//已关注
	/**用户关注状态：2 已申请  */
	public static final int USER_APPLY_RELATION = 2;//已申请
	/**用户关注状态：3 申请被拒绝 */
	public static final int USER_REJECTED_RELATION = 3;//申请被拒绝
	
	/**用户关系：父母 */
	public static final int USER_RELATION_PARENT = 1;//父母
	/**用户关系：配偶 */
	public static final int USER_RELATION_SPOUSE = 2;//配偶
	/**用户关系：兄弟姐妹 */
	public static final int USER_RELATION_SIBLING = 3;//兄弟姐妹
	/**用户关系：子女 */
	public static final int USER_RELATION_CHILDREN = 4;//子女
	
	/**
	 * 养老机构用户类型，用来区别是管理员，还是会员，还是养老用户
	 */
	public static final int USER_TYPE = 3;
	
	/**功能位置标示：1 云平台后台 */
	public static final int ACTION_SIGN_1 = 1;//云平台后台
	/**功能位置标示：2 前置机 */
	public static final int ACTION_SIGN_2 = 2;//前置机
	/**功能位置标示：3 个人门户 */
	public static final int ACTION_SIGN_3 = 3;//个人门户
	/**功能位置标示：4 政府门户 */
	public static final int ACTION_SIGN_4 = 4;//政府门户
	/**功能位置标示：5 医疗门户 */
	public static final int ACTION_SIGN_5 = 5;//医疗门户
	/**功能位置标示：6 养老门户 */
	public static final int ACTION_SIGN_6 = 6;//养老门户

	
	/**数据标志-无**/
	public static final int MARK_0 = 0;
	/**数据标志-有**/
	public static final int MARK_1 = 1;
	
	/**
	 * 默认密码“1234abcd”
	 */
	public static final String DEAFULT_PASSWORD = "1234abcd";
	
	/**
	 * 公告状态
	 * **/
	/**新建**/
	public static final int NOTICE_NEW=1;//新建
	/**待审批**/
	public static final int NOTICE_APPROVALPENDING =2;//待审批
	/**审批通过**/
	public static final int NOTICE_APPROVALPASS=3;//审批通过
	/**审批未通过**/
	public static final int NOTICE_APPROVALREJECT=4;//审批未通过
	/**已发布**/
	public static final int NOTICE_RELEASE=5;//已发布
	/**驳回修改后的状态**/
	public static final int NOTICE_REJECTMODIFY=6;//驳回修改后的状态
	
	//公告栏目状态
	public static final int COLUMN_DISABLE=0;//可用
	public static final int COLUMN_ISDISABLE=1;//不可用
	
	//前置机信息状态
	public static final int FRONTEND_AFFIRM=0;//未确认
	public static final int FRONTEND_ISAFFIRM=1;//已经确认
	
	//前置机信息是否配对状态
	public static final int FRONTEND_AFFIRM_MID=1;//未确认
	public static final int FRONTEND_ISAFFIRM_MID=2;//已经确认
	
	//用户组织关系_用户类型
	/**用户组织关系_用户类型：1 养老用户 */
	public static final int PENSION_USER = 1;
	/**用户组织关系_用户类型：2 机构工作人员 */
	public static final int ORG_WORKER = 2;
	
	/**
	 * 登陆门户标志
	 */
	/**个人门户登陆 **/
	public static final int PORTALTYPE_PERSONAL = 1;
	/**养老门户登陆**/
	public static final int PORTALTYPE_PENSION = 2;
	/**医疗门户登陆**/
	public static final int PORTALTYPE_MEDICAL = 3;
	/**政府门户登陆**/
	public static final int PORTALTYPE_GOVERNMENT = 4;
	
	/**
	 * 各个门户对应的主页面
	 */
	public static final Map<Integer, String> MAINPAGES = new HashMap<Integer, String>();
	static{
		MAINPAGES.put(PORTALTYPE_PERSONAL, "portal/PersonalMain");//个人门户主页面
		MAINPAGES.put(PORTALTYPE_PENSION, "portal/PensionMain");//养老门户主页面
		MAINPAGES.put(PORTALTYPE_MEDICAL, "portal/MedicalMain");//医疗门户主页面
		MAINPAGES.put(PORTALTYPE_GOVERNMENT, "portal/GovMain");//政府门户主页面
	}
	
	/**
	 * 各个门户对应的域名地址
	 */
	public static final Map<String, String> URLPATHS = new HashMap<String, String>();
	static{
		URLPATHS.put("/HealthCare", "../../../");//门户首页
		URLPATHS.put("/person", "../../../portal/person.jsp");//个人门户
		URLPATHS.put("/org", "../../../portal/org.jsp");//养老门户
		URLPATHS.put("/hospital", "../../../portal/hospital.jsp");//医疗门户
		URLPATHS.put("/gov", "../../../portal/gov.jsp");//政府门户
		URLPATHS.put("/mgmt", "../../../admin/");//后台管理
	}
	
	/**
	 * 各个系统对应cookie的Key值
	 */
	public static final Map<String, String> COOKIEKEYS = new HashMap<String, String>();
	static{
		COOKIEKEYS.put("/person", "PERSONSESSIONID");//个人门户
		COOKIEKEYS.put("/org", "ORGSESSIONID");//养老门户
		COOKIEKEYS.put("/hospital", "HOSPITALSESSIONID");//医疗门户
		COOKIEKEYS.put("/gov", "GOVSESSIONID");//政府门户
		COOKIEKEYS.put("/mgmt", "MGMTSESSIONID");//后台管理
	}

	/**
	 * 健康问卷状态
	 */
	/** 历史数据 **/
	public static final int HEALTHPROBLEM_HISTORY = 9;
	/** 个人门户登陆后操作的草稿 **/
	public static final int HEALTHPROBLEM_PERSONAL = 1;
	/** 养老门户登陆后操作的草稿 **/
	public static final int HEALTHPROBLEM_PENSION = 2;


	/**
	 * 问卷的问题类型
	 */
	public static final int SINGLECHOICE = 1; // 单选题
	public static final int MULTIPLECHOICE = 2; // 多选题
	public static final int ESSAY = 3; // 问答题

	//文件上传状态
	/**文件状态：已上传 */
	public static final String FILE_UPLOAD_ALREADY = "1"; // 已上传
	/**文件状态：导入成功 */
	public static final String FILE_IMPORT_SUCCESS = "2"; // 导入成功
	/**文件状态：导入失败 */
	public static final String FILE_IMPORT_FAILURE = "3"; // 导入失败

	/**
	 * 前置机状态
	 */
	public static final int FRONT_STATUS_QY = 1; // 启用状态
	public static final int FRONT_STATUS_JY = 2; // 禁用状态
	public static final int FRONT_STATUS_WX = 3; // 维修状态
	public static final int FRONT_STATUS_BF = 4; // 报废状态

	/**
	 * 监护类型
	 */
	public static final int EXAMINATION_COTINUE = 0; // 持续监护
	public static final int EXAMINATION_ONE = 1; // 体检
	
	/**
	 * 管辖范围
	 */
	public static final String COUNTRY_SCOPE = "1"; //全国
	public static final String PROVINCE_SCOPE = "2"; //省
	public static final String CITY_SCOPE = "3"; //市
	public static final String DIVISION_SCOPE = "4"; //县
}
