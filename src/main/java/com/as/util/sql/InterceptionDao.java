package com.as.util.sql;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.as.entity.Page;
import com.as.entity.business.Dict;
import com.as.entity.business.Dimentions;
import com.as.entity.system.Department;
import com.as.entity.system.Dictionaries;
import com.as.entity.system.Menu;
import com.as.entity.system.Role;
import com.as.entity.system.User;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.bean.setGet;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Aspect
@Component
public class InterceptionDao {

    @Pointcut("execution(* com.as.dao..*(..))")
    public void dao() {
    }

    @SuppressWarnings("unchecked")
    @Around(value = "dao()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object proceed = null;
        try {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object O = args[i];
                if (O != null && !StringUtil.isEmpty(O.toString())) {
                    if (O.getClass().toString().indexOf("String") > 0) {
                    } else if (O.getClass().toString().indexOf("PageData") > 0) {
                        PageData o = (PageData) O;
                        for (Object key : o.keySet()) {
                            if (o.get(key).getClass().toString().indexOf("String") > 0) {
                                o.put(key, StringEscapeUtils.escapeSql(o.get(key).toString()));
                            }
                        }
                    } else if (O.getClass().toString().indexOf("java.util.Map") > 0) {
                        Map<Object, Object> o = (Map<Object, Object>) O;
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
                        Class classType = O.getClass();
                        Object objectCopy = (classType.getConstructor(new Class[]{})).newInstance(new Object[]{});
                        //获得所有的成员变量...
                        Field fields[] = classType.getDeclaredFields();
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


//		private Object changValue(Object _obj, String flag) throws Exception{
//		    //基本类型不作操作
//		    if(_obj instanceof Map){
//		        changeMapValue(_obj,flag);
//		    }else if(_obj instanceof List){
//		        @SuppressWarnings("unchecked")
//		        List<Object> list = (List<Object>) _obj;
//		        for (Object obj : list) {
//		            if(obj instanceof Map){
//		                changeMapValue(_obj,flag);
//		            }else{
//		                changObjectValue(_obj,flag);
//		            }
//		        }
//		    }else{
//		        changObjectValue(_obj,flag);
//		    }
//		    return _obj;
//		}
//		/**
//		 * 
//		 * @Description(功能描述)    :  当对象为Map 修改key的值 
//		 */
//		@SuppressWarnings("unchecked")
//		private Object changeMapValue(Object _obj, String flag) throws Exception{
//		    Map<String,Object> map = (Map<String,Object>) _obj;
//		    if(map.containsKey(HANDLE_FIELD_NAME)){
//		        Object fieldValue = map.get(HANDLE_FIELD_NAME);
//		        String afterValue = crypto(fieldValue, flag);
//		        if(!ObjectIsNullUtil.isNullOrEmpty(afterValue)){
//		            map.put(HANDLE_FIELD_NAME, afterValue);
//		        }
//		    }
//		    return _obj;
//		}
//		
//		/***
//		 * @Description(功能描述)    :  修改Object对象field的值
//		 */
//		private Object changObjectValue(Object _obj, String flag) throws Exception{
//		    Class<?> resultClz = _obj.getClass();
//		    Field[] fieldInfo = resultClz.getDeclaredFields(); //获取class里的所有字段  父类字段获取不到    注：如果出现加密解密失败 请先查看idno是否在父类中
//		    for (Field field : fieldInfo) {
//		        if(HANDLE_FIELD_NAME.equals(field.getName())){
//		            field.setAccessible(true); //成员变量为private,故必须进行此操
//		            Object fieldValue = field.get(_obj);
//		            String afterValue = crypto(fieldValue, flag);
//		            if(!ObjectIsNullUtil.isNullOrEmpty(afterValue)){
//		                field.set(_obj, afterValue);
//		            }
//		            break;
//		        }
//		    }
//		    return _obj;
//		}
//		
//		/***
//		 * @Description(功能描述)    :  加密操作
//		 */
//		private String crypto(Object value,String flag) throws Exception{
//		    if(ObjectIsNullUtil.isNullOrEmpty(value)){
//		        return null;
//		    }
//		    //加密操作；加密之前先去查询一下数据库 有没有 如果没有 则insert
//		    if (ENCRYPT_FLAG.equals(flag)) { 
//		        String encodeValue = Encryption.encode(SECRET_KEY, value.toString()); //加密
//		        ThemisCryptoIdno idnoDomain = new ThemisCryptoIdno();
//		        idnoDomain.setCryptoIdno(encodeValue);
//		        idnoDomain = themisCryptoIdnoService.selectOneByObject(idnoDomain);
//		        if(ObjectIsNullUtil.isNullOrEmpty(idnoDomain)){ //若空 则生成 seq 然后入库  返回seq
//		            String tr_date = DateOperation.convertToDateStr2(DateOperation.currentTimeMills());
//		            //获取有流水号
//		            String inextValDual=netxValDualService.findIdNoSeq();
//		            //组流水：日期+ 7位流水   其他表储存的就是这个idnoSeq
//		            String idnoSeq=(tr_date+DateOperation.fill(inextValDual, '0', 7, true)).replaceAll("-", ""); 
//		            idnoDomain = new ThemisCryptoIdno();
//		            idnoDomain.setOptTime(DateOperation.convertToDateStr1(DateOperation.currentTimeMills()));
//		            idnoDomain.setCryptoIdno(encodeValue);
//		            idnoDomain.setCryptoSeq(idnoSeq);
//		            themisCryptoIdnoService.insert(idnoDomain);
//		            return idnoSeq;
//		        }else{ //不为空 直接返回seq
//		            return idnoDomain.getCryptoSeq();
//		        }
//		    }else{ //解密操作 通过seq 查询 然后解密返回明文
//		        ThemisCryptoIdno idnoDomain = new ThemisCryptoIdno();
//		        idnoDomain.setCryptoSeq(value.toString());
//		        idnoDomain = themisCryptoIdnoService.selectOneByObject(idnoDomain);
//		        if(!ObjectIsNullUtil.isNullOrEmpty(idnoDomain)){
//		            return Encryption.decodeValue(SECRET_KEY, idnoDomain.getCryptoIdno()); //解密
//		        }
//		    }
//		    return null;
//		}

}