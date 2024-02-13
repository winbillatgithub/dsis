package com.as.util.log;

import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.as.service.business.common.CommonManager;
import com.as.util.Jurisdiction;
import com.as.util.StringUtil;
import com.as.util.Tools;
import com.as.util.UuidUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;


@Aspect
@Component
public class LogInterceptor {
	@Resource(name = "commonService")
	public CommonManager commonService;

//	@Pointcut("execution(* com.as.service..save*(..)) or execution(* com.as.service..insert*(..)) or execution(* com.as.service..update*(..)) or execution(* com.as.service..delete*(..))")
	@Pointcut("execution(* com.as.service.business..*(..))")
	public void pointCut() {
	}

	/**
	 ** 记录用户的操作
	 */
	@Before(value = "pointCut()") // ||pointCut1()||pointCut2()||pointCut3()
	public void doBefore(JoinPoint joinPoint) {
		try {
			String OPERATOR = Jurisdiction.getUsername();
			String schema = Jurisdiction.getTopCorpBianma();
			String CORP_ID = Jurisdiction.getTopCorpId();

			String SERVICE_NAME = joinPoint.getSignature().getName();
			if (SERVICE_NAME.length() > 100) {
				SERVICE_NAME = SERVICE_NAME.substring(0, 100);
			}
			Signature Signature =joinPoint.getSignature();
			String MODULE_NAME=Signature.toString().substring(Signature.toString().indexOf(".impl.")+6,Signature.toString().indexOf(SERVICE_NAME)-1);
			if (MODULE_NAME.length() > 100) {
				MODULE_NAME = MODULE_NAME.substring(0, 100);
			}
			String OPT_TYPE = "1";
			if (joinPoint.getSignature().getName().indexOf("save") >= 0
					|| joinPoint.getSignature().getName().indexOf("insert") >= 0) {
				OPT_TYPE = "2";
			} else if (joinPoint.getSignature().getName().indexOf("update") >= 0) {
				OPT_TYPE = "3";
			} else if (joinPoint.getSignature().getName().indexOf("delete") >= 0) {
				OPT_TYPE = "4";
			} else if (joinPoint.getSignature().getName().indexOf("log")>= 0) {
				return;
			}
			StringBuffer REMARK = new StringBuffer();
			// REMARK.append(Signature);
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				REMARK.append(joinPoint.getArgs()[i].toString());
			}
			String REMARKto = REMARK.toString();
			if (REMARKto.length() > 255) {
				REMARKto = REMARKto.substring(0, 255);
			}
			String sql = "INSERT INTO " + schema
					+ ".sys_logs (LOGS_ID,OPT_TYPE,LOG_LEVEL,CORP_ID,SERVICE_NAME,MODULE_NAME,REMARK,OPERATOR,CREATE_TIME,UPDATE_TIME) VALUES "
					+ "	('" + UuidUtil.get32UUID() + "','" + OPT_TYPE + "','" + 1 + "','" + CORP_ID + "','"
					+ SERVICE_NAME + "','" + MODULE_NAME + "','" + REMARKto + "','" + OPERATOR + "','"
					+ Tools.date2Str(new Date()) + "','" + Tools.date2Str(new Date()) + "')";
//			commonService.log(sql);
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	
	
	/**
	 ** 用于拦截指定内容，记录用户的操作
	 */
//	@Around(value = "pointCut()")
	public void doAround(ProceedingJoinPoint joinPoint) {
	}
	
	
	@After(value = "pointCut()")
	public void doAfter(JoinPoint joinPoint) {
	}
	public static boolean sql_inj(String str) {
		String inj_str = "'|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";
//		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";  
		String[] inj_stra = inj_str.split("\\|");

		for (int i = 0; i < inj_stra.length; i++) {

			if (str.indexOf(inj_stra[i]) >= 0) {
				return true;
			}
		}

		return false;

	}


}
