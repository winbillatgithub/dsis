package com.as.util.sql;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.as.entity.Page;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.bean.setGet;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;

@Aspect
@Component
public class Interception {

    @Pointcut("execution(* com.as.service.business..*(..))")
    public void pointCut() {
    }

    /**
     * * 用于拦截指定内容，记录用户的操作
     */
    @SuppressWarnings("unchecked")
    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object proceed = null;
        try {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Signature Signature = joinPoint.getSignature();
                String MODULE_NAME = Signature.toString();
                Object O = args[i];
                if (O != null && !StringUtil.isEmpty(O.toString())) {
                    if (O.getClass().toString().indexOf("String") > 0) {
                        if (MODULE_NAME.indexOf("CommonService") >= 0 || MODULE_NAME.indexOf("LogsService") >= 0) {
                            break;
                        }
                        args[i] = StringEscapeUtils.escapeSql(O.toString());
                    } else if (O.getClass().toString().indexOf("PageData") > 0) {
                        PageData o = (PageData) O;
                        for (Object key : o.keySet()) {
                            if (o.get(key).getClass().toString().indexOf("String") > 0) {
                                o.put(key, StringEscapeUtils.escapeSql(o.get(key).toString()));
                            }
                        }
                    } else if (O.getClass().toString().indexOf("Page") > 0) {
                        Page o1 = (Page) O;
                        PageData o = o1.getPd();
                        for (Object key : o.keySet()) {
                            if (o.get(key).getClass().toString().indexOf("String") > 0) {
                                o.put(key, StringEscapeUtils.escapeSql(o.get(key).toString()));
                            }
                        }
                    } else if (O.getClass().toString().indexOf("com.as.entity") > 0 || O.getClass().toString().indexOf("com.as.util") > 0) {
                        //获得所有的成员变量...
                        Field fields[] = O.getClass().getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            Field field = fields[j];
                            String fieldName = field.getName();
                            if (field.getClass().toString().indexOf("String") > 0)
                                setGet.invokeSet(O.getClass(), fieldName, StringEscapeUtils.escapeSql(setGet.invokeGet(O.getClass(), fieldName).toString()));
                        }
                        args[i] = O;
                    }
                }
            }
            proceed = joinPoint.proceed(args);
        } catch (JSONException e) {
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable e1) {
            }
        } catch (NullPointerException e) {
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable e1) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proceed;
    }


}
