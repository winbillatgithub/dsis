package com.as.interceptor;

import com.as.entity.system.User;
import com.as.util.Const;
import com.as.util.Encrypt;
import com.as.util.Jurisdiction;
import org.apache.shiro.session.Session;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
* 类名称：登录过滤，权限验证
* 类描述： 
* @author antispy
* 作者单位： 
* 联系方式：
* 创建时间：2015年11月2日
* @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{

    private static String ANONYMOUS_USER_ID = "anon_id";
    private static String ANONYMOUS_USER_NAME = "anon_user_name";
    private static String ANONYMOUS_NAME = "anon_name";

    private int NOT_PUB = 0;
    private int PUB_ACCESS_ALLOWED = 1;
    private int PUB_ACCESS_DENIED = 2;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String path = request.getServletPath();
		if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
			return true;
        } else {
            int status = isValidPublicAccess(request, path);
            if (status == PUB_ACCESS_ALLOWED) {
                return true;
            } else if (status == PUB_ACCESS_DENIED) {
                response.getWriter().write("No access to the URL, please:https://www.aiinfosys.cn");
                return false;
            }
			User user = (User)Jurisdiction.getSession().getAttribute(Const.SESSION_USER);
			if (user != null) {
				path = path.substring(1, path.length());
				boolean b = Jurisdiction.hasJurisdiction(path); //访问权限校验
				if (!b) {
					response.sendRedirect(request.getContextPath() + Const.LOGIN);
				}
				return b;
			} else {
				//登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
			}
		}
	}

    /**
     * 公开访问合法性判断
     * @param request
     * @param path
     * @return int
     * 0: not /pub
     * 1: /pub access allowed
     * 2: /pub access denied
     */
	private int isValidPublicAccess(HttpServletRequest request, String path) {
		if (path.startsWith("/pub/")) {
			String tablesId = request.getParameter("TABLES_ID");
			if (StringUtils.isEmpty(tablesId)) {
				return PUB_ACCESS_DENIED;
			}
            String corpId = request.getParameter("CORP_ID");
            if (StringUtils.isEmpty(corpId)) {
                return PUB_ACCESS_DENIED;
            }
            String code = request.getParameter("code");
            if (StringUtils.isEmpty(code)) {
                return PUB_ACCESS_DENIED;
            }
			String token = request.getParameter("token");
			if (StringUtils.isEmpty(token)) {
				return PUB_ACCESS_DENIED;
			}
			String uri = "";
			if ("/pub/card.do".equals(path)) {
				uri = path + "?formEditFlag=preview&TABLES_ID=" + tablesId + "&CORP_ID=" + corpId + "&code=" + code;
			} else if ("/pub/list.do".equals(path)) {
				uri = path + "?TABLES_ID=" + tablesId + "&CORP_ID=" + corpId + "&code=" + code;
			} else {
                return PUB_ACCESS_DENIED;
            }
            if (token.equals(Encrypt.EncryptUri(uri))) {
                Session session = Jurisdiction.getSession();
                User user = new User();
                user.setUSER_ID(ANONYMOUS_USER_ID);
                user.setUSERNAME(ANONYMOUS_USER_NAME);
                user.setNAME(ANONYMOUS_NAME);
                user.setCORP_ID(corpId);
                user.setTOP_CORP_ID(corpId); // 设定总公司编码
                user.setTOP_CORP_BIANMA(code);
                user.setENCRYPT_CODE(code);
                session.setAttribute(Const.SESSION_USER, user);
                session.setAttribute(Const.SESSION_USERNAME, user.getUSERNAME()); // 放入用户名到session
                return PUB_ACCESS_ALLOWED;
            } else {
                return PUB_ACCESS_DENIED;
            }
		}
		return NOT_PUB;
	}
}
