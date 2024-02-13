package com.as.util.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.util.JSONPObject;

import com.as.entity.system.Department;

/**
 * 接口参数校验
 * 
 * @author: as qq:609281718 修改日期：2015/11/2
 */
public class setGet {
	/**    
	 
	 * java反射bean的get方法    
	 
	 *     
	 
	 * @param objectClass    
	 
	 * @param fieldName    
	 
	 * @return    
	 
	 */       
	  
	@SuppressWarnings("unchecked")       
	  
	public static Method getGetMethod(Class objectClass, String fieldName) {       
	  
	    StringBuffer sb = new StringBuffer();       
	  
	    sb.append("get");       
		  if(fieldName!=null) {
		    sb.append(fieldName.substring(0, 1).toUpperCase());       
		  
		    sb.append(fieldName.substring(1));       
		  }
	    try {       
	  
	        return objectClass.getMethod(sb.toString());       
	  
	    } catch (Exception e) {       
	  
	    }       
	  
	    return null;       
	  
	}       
	  
	       
	  
	/**    
	 
	 * java反射bean的set方法    
	 
	 *     
	 
	 * @param objectClass    
	 
	 * @param fieldName    
	 
	 * @return    
	 
	 */       
	  
	@SuppressWarnings("unchecked")       
	  
	public static Method getSetMethod(Class objectClass, String fieldName) {       
	  
	    try {       
	  
	        Class[] parameterTypes = new Class[1];       
	  
	        Field field = objectClass.getDeclaredField(fieldName);       
	  
	        parameterTypes[0] = field.getType();       
	  
	        StringBuffer sb = new StringBuffer();       
	  
	        sb.append("set");       
	  
	        sb.append(fieldName.substring(0, 1).toUpperCase());       
	  
	        sb.append(fieldName.substring(1));       
	  
	        Method method = objectClass.getMethod(sb.toString(), parameterTypes);       
	  
	        return method;       
	  
	    } catch (Exception e) {       
	  
	        e.printStackTrace();       
	  
	    }       
	  
	    return null;       
	  
	}       
	  
	       
	  
	/**    
	 
	 * 执行set方法    
	 
	 *     
	 
	 * @param o执行对象    
	 
	 * @param fieldName属性    
	 
	 * @param value值    
	 
	 */       
	  
	public static void invokeSet(Object o, String fieldName, Object value) {       
	  
	    Method method = getSetMethod(o.getClass(), fieldName);       
	  
	    try {       
	  
	        method.invoke(o, new Object[] { value });       
	  
	    } catch (Exception e) {       
	  
	        e.printStackTrace();       
	  
	    }       
	  
	}       
	  
	       
	  
	/**    
	 
	 * 执行get方法    
	 
	 *     
	 
	 * @param o执行对象    
	 
	 * @param fieldName属性    
	 
	 */       
	  
	public static Object invokeGet(Object o, String fieldName) {       
	  
	    Method method = getGetMethod(o.getClass(), fieldName);       
	  
	    try {       
	  
	        return method.invoke(o, new Object[0]);       
	  
	    } catch (Exception e) {       
	  
	        e.printStackTrace();       
	  
	    }       
	  
	    return null;       
	  
	}  
	
	
	 public Object bean(Object object) throws Exception
	    {
	        //获得object 的class类
	        Class classType = object.getClass();
	        System.out.println("Class:"+classType.getName());
	        Object objectCopy = (classType.getConstructor(new Class[]{})).newInstance(new Object[]{});
	        //获得所有的成员变量...
	        Field fields[] = classType.getDeclaredFields();
	        for(int i = 0; i < fields.length; i++)
	        {
	            Field field = fields[i];
	            String fieldName = field.getName();
	            String firstLetter = fieldName.substring(0, 1).toUpperCase();
	            //获得对应的getXxxxx方法的名子。。。
	            String getMethodName = "get" + firstLetter + fieldName.substring(1);
	            System.out.println(getMethodName);
	            //获得对应的setXxxxx方法的名子......
	            String setMethodName = "set" + firstLetter + fieldName.substring(1);
	            System.out.println(setMethodName);
	        }
	        return objectCopy;
	    }
	
	public static void main(String[] args) throws Exception {
		Department  customer = new Department();
	        
		Department coutomerCopy = (Department)new setGet().bean(customer);
	}
	
	
}
