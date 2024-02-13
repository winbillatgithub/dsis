package com.as.controller.system.login;

import com.as.controller.base.BaseController;
import com.as.entity.system.Department;
import com.as.entity.system.Menu;
import com.as.entity.system.Role;
import com.as.entity.system.User;
import com.as.service.business.cellrule.CellRuleManager;
import com.as.service.system.appuser.AppuserManager;
import com.as.service.system.buttonrights.ButtonrightsManager;
import com.as.service.system.department.DepartmentManager;
import com.as.service.system.fhbutton.FhbuttonManager;
import com.as.service.system.menu.MenuManager;
import com.as.service.system.role.RoleManager;
import com.as.service.system.user.UserManager;
import com.as.util.AppUtil;
import com.as.util.Const;
import com.as.util.DateUtil;
import com.as.util.Jurisdiction;
import com.as.util.Logger;
import com.as.util.PageData;
import com.as.util.RightsHelper;
import com.as.util.StringUtil;
import com.as.util.Tools;
import com.as.util.UuidUtil;
import com.as.util.mail.SendEmailUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 总入口
 *
 * @author as 修改日期：2015/11/2
 */
@Controller
public class LoginController extends BaseController {
    public static Logger logger = Logger.getLogger(LoginController.class);

    @Resource(name = "userService")
    private UserManager userService;
    @Resource(name = "menuService")
    private MenuManager menuService;
    @Resource(name = "roleService")
    private RoleManager roleService;
    @Resource(name = "buttonrightsService")
    private ButtonrightsManager buttonrightsService;
    @Resource(name = "fhbuttonService")
    private FhbuttonManager fhbuttonService;
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "departmentService")
    private DepartmentManager departmentService;
    @Resource(name = "cellruleService")
    private CellRuleManager cellruleService;

    private static boolean bVerifyCode = false;

    // 指标,列表模板,模板指标, 测试功能450-453（动态）, 规则库管理,系统管理,用户管理,日志管理114-115（动态）
    // 0:表示管理员有增删改查权限,其它为menuid表示管理员有访问该菜单的权限
    private static final String DEFAULT_RIGHTS1 = "0,51,131,";
    private static final String DEFAULT_RIGHTS2 = ",105,106,107,108,109,100,130,1,2,36,37,38,49,40,41,114,115";

    /**
     * 访问登录页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login_toLogin")
    public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = this.getModelAndView();
        // 设置数据
        PageData pd = this.getPageData();
        pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); // 读取系统名称
        mv.setViewName("/system/index/login");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 请求登录，验证用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login_login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object login() throws Exception {
        PageData pd = this.getPageData();
        Map map = doLogin(pd, false);
        return AppUtil.returnObject(new PageData(), map);
    }

    private Map<String, String> doLogin(PageData pd, boolean autoLogin) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "";
        String CORP_ID = (String) pd.get("CORP_ID");
        if (Tools.isEmpty(CORP_ID)) {
            logger.error("No parameter of CORP_ID!");
            CORP_ID = getCorpIdFromEnv();// "29fb9c3d04034e34aa1484c81bf06165"; // 小牛卡夫
            pd.put("CORP_ID", CORP_ID);
        }
        String KEYDATA[] = pd.getString("KEYDATA").replaceAll("qq609281718fh", "").replaceAll("QQ609281718fh", "")
                .split(",as,");
        if (null != KEYDATA && KEYDATA.length >= 2) {
            Session session = Jurisdiction.getSession();
            String sessionCode = (String) session.getAttribute(Const.SESSION_SECURITY_CODE); // 获取session中的验证码
            String code = "";
            if (bVerifyCode && KEYDATA.length >= 3) {
                code = KEYDATA[2];
            }

            // 必须为某个公司的用户登录，除非是admin用户
            if (Tools.isEmpty(CORP_ID) && !"admin".equals(KEYDATA[0])) {
                logger.error("组织ID为空,非admin用户!");
                errInfo = "nullgid"; // 无效的组织ID
            } else if (bVerifyCode && Tools.isEmpty(code)) {// 判断效验码
                logger.error("验证码为空!");
                errInfo = "nullcode"; // 效验码为空
            } else {
                // admin用户不受corp_id限制
                if ("admin".equals(KEYDATA[0])) {
                    pd.remove("CORP_ID");
                }
                String USERNAME = KEYDATA[0]; // 登录过来的用户名
                String PASSWORD = KEYDATA[1]; // 登录过来的密码
                if (bVerifyCode) {
                    if (Tools.notEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)) { // 判断登录验证码
                        errInfo = doSession(pd, USERNAME, PASSWORD);
                    } else {
                        logger.error("验证码输入有误!");
                        errInfo = "codeerror"; // 验证码输入有误
                    }
                } else {
                    errInfo = doSession(pd, USERNAME, PASSWORD);
                }
                if (Tools.isEmpty(errInfo)) {
                    errInfo = "success"; // 验证成功
                    logBefore(logger, USERNAME + "登录系统");
                }
            }
        } else {
            errInfo = "error"; // 缺少参数
        }
        map.put("result", errInfo);
        return map;
    }

    private String doSession(PageData pd, String USERNAME, String PASSWORD) throws Exception {
        String errInfo = "";

        String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString(); // 密码加密
        pd.put("USERNAME", USERNAME);
        pd.put("PASSWORD", passwd);
        pd = userService.getUserByNameAndPwd(pd); // 根据用户名和密码去读取用户信息
        if (pd != null) {
            // 判断用户是否在当前组织
            pd.put("LAST_LOGIN", DateUtil.getTime().toString());
            userService.updateLastLogin(pd);
            User user = new User();
            user.setUSER_ID(pd.getString("USER_ID"));
            user.setUSERNAME(pd.getString("USERNAME"));
            user.setPASSWORD(pd.getString("PASSWORD"));
            user.setNAME(pd.getString("NAME"));
            user.setRIGHTS(pd.getString("RIGHTS"));
            user.setROLE_ID(pd.getString("ROLE_ID"));
            user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
            user.setIP(pd.getString("IP"));
            user.setSTATUS(pd.getString("STATUS"));
            user.setCORP_ID(pd.getString("CORP_ID"));
            if ("admin".equals(USERNAME)) {
                user.setCORP_NAME("");
                user.setTOP_CORP_ID("0");
                user.setTOP_CORP_BIANMA("peixiwulian"); // 设定总公司编码
            } else {
                pd.put("CORP_ID", pd.getString("CORP_ID"));
                PageData dep = departmentService.findById(pd);
                user.setCORP_NAME(dep.getString("NAME"));
                Department top = departmentService.findTopParentIdById(pd);
                user.setTOP_CORP_ID(top.getCORP_ID()); // 设定总公司ID
                user.setTOP_CORP_BIANMA(top.getBIANMA()); // 设定总公司编码
                user.setENCRYPT_CODE(top.getBIANMA()); // 默认设定加密密钥为公司编码，TODO:后期需要更改
            }
            // shiro加入身份验证
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD);
            try {
                subject.login(token);
            } catch (AuthenticationException e) {
                errInfo = "身份验证失败！";
            }

            Session session = subject.getSession();
            session.setAttribute(Const.SESSION_USER, user); // 把用户信息放session中
            session.removeAttribute(Const.SESSION_SECURITY_CODE); // 清除登录验证码的session
            session.setAttribute(Const.SESSION_USERNAME, USERNAME); // 放入用户名到session
            // 现在放在session后, /main/index就放不进去了
//            List<Menu> allmenuList = new ArrayList<Menu>();
//            session.setAttribute(USERNAME + Const.SESSION_allmenuList, allmenuList);//菜单权限放入session中


        } else {
            errInfo = "usererror"; // 用户名或密码有误
            logBefore(logger, USERNAME + "登录系统密码或用户名错误");
        }
        return errInfo;
    }

    /**
     * 判断公司是否已存在
     *
     * @param gid
     * @return true/false
     * @throws Exception
     */
    public boolean companyExists(String gid) throws Exception {
        PageData pd = new PageData();
        pd.put("CORP_ID", gid);
        PageData dep = departmentService.findById(pd);
        if (dep.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 访问系统首页
     *
     * @param changeMenu：切换菜单参数
     * @return
     */
    @RequestMapping(value = "/main/{changeMenu}")
    @SuppressWarnings("unchecked")
    public ModelAndView login_index(@PathVariable("changeMenu") String changeMenu) {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        try {
            Session session = Jurisdiction.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER); // 读取session中的用户信息(单独用户信息)
            if (user != null) {
                User userr = (User) session.getAttribute(Const.SESSION_USERROL); // 读取session中的用户信息(含角色信息)
                if (null == userr) {
                    user = userService.getUserAndRoleById(user.getUSER_ID()); // 通过用户ID读取用户信息和角色信息
                    session.setAttribute(Const.SESSION_USERROL, user); // 存入session
                } else {
                    user = userr;
                }
                String USERNAME = user.getUSERNAME();
                Role role = user.getRole(); // 获取用户角色
                String roleRights = role != null ? role.getRIGHTS() : ""; // 角色权限(菜单权限)
                session.setAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS, roleRights); // 将角色权限存入session
                session.setAttribute(Const.SESSION_USERNAME, USERNAME); // 放入用户名到session
                List<Menu> allmenuList = new ArrayList<Menu>();
                if (null == session.getAttribute(USERNAME + Const.SESSION_allmenuList)) {
                    pd.put("PARENT_ID", "0");
                    allmenuList = menuService.listAllMenuQx(pd); // 获取所有菜单
                    if (Tools.notEmpty(roleRights)) {
                        allmenuList = this.readMenu(allmenuList, roleRights); // 根据角色权限获取本权限的菜单列表
                    }
                    session.setAttribute(USERNAME + Const.SESSION_allmenuList, allmenuList);// 菜单权限放入session中
                } else {
                    allmenuList = (List<Menu>) session.getAttribute(USERNAME + Const.SESSION_allmenuList);
                }
                // 切换菜单处理=====start
                List<Menu> menuList = new ArrayList<Menu>();
                if (null == session.getAttribute(USERNAME + Const.SESSION_menuList) || ("yes".equals(changeMenu))) {
                    List<Menu> menuList1 = new ArrayList<Menu>();
                    List<Menu> menuList2 = new ArrayList<Menu>();
                    // 拆分菜单
                    for (int i = 0; i < allmenuList.size(); i++) {
                        Menu menu = allmenuList.get(i);
                        if ("1".equals(menu.getMENU_TYPE())) {
                            menuList1.add(menu);
                        } else {
                            menuList2.add(menu);
                        }
                    }
                    session.removeAttribute(USERNAME + Const.SESSION_menuList);
                    if ("2".equals(session.getAttribute("changeMenu"))) {
                        session.setAttribute(USERNAME + Const.SESSION_menuList, menuList1);
                        session.removeAttribute("changeMenu");
                        session.setAttribute("changeMenu", "1");
                        menuList = menuList1;
                    } else {
                        session.setAttribute(USERNAME + Const.SESSION_menuList, menuList2);
                        session.removeAttribute("changeMenu");
                        session.setAttribute("changeMenu", "2");
                        menuList = menuList2;
                    }
                } else {
                    menuList = (List<Menu>) session.getAttribute(USERNAME + Const.SESSION_menuList);
                }
                // 切换菜单处理=====end
                if (null == session.getAttribute(USERNAME + Const.SESSION_QX)) {
                    session.setAttribute(USERNAME + Const.SESSION_QX, this.getUQX(USERNAME)); // 按钮权限放到session中
                }
                this.getRemortIP(USERNAME); // 更新登录IP
                mv.setViewName("/system/index/main");
                mv.addObject("user", user);
                mv.addObject("menuList", menuList);
            } else {
                mv.setViewName("/system/index/login");// session失效后跳转登录页面
            }
        } catch (Exception e) {
            mv.setViewName("/system/index/login");
            logger.error(e.getMessage(), e);
        }
        pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); // 读取系统名称
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 根据角色权限获取本权限的菜单列表(递归处理)
     *
     * @param menuList：传入的总菜单
     * @param roleRights：加密的权限字符串
     * @return
     */
    public List<Menu> readMenu(List<Menu> menuList, String roleRights) {
        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setHasMenu(RightsHelper.testRights(roleRights, menuList.get(i).getMENU_ID()));
            if (menuList.get(i).isHasMenu()) { // 判断是否有此菜单权限
                this.readMenu(menuList.get(i).getSubMenu(), roleRights);// 是：继续排查其子菜单
            } else {
                menuList.remove(i);
                i--;
            }
        }
        return menuList;
    }

    /**
     * 进入tab标签
     *
     * @return
     */
    @RequestMapping(value = "/tab")
    public String tab() {
        return "system/index/tab";
    }

    /**
     * 进入首页后的默认页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login_default")
    public ModelAndView defaultPage() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd.put("userCount", Integer.parseInt(userService.getUserCount("").get("userCount").toString()) - 1); // 系统用户数
        pd.put("appUserCount", Integer.parseInt(appuserService.getAppUserCount("").get("appUserCount").toString())); // 会员数
        mv.addObject("pd", pd);
        mv.setViewName("/system/index/default");
        return mv;
    }

    /**
     * 用户注销
     *
     * @return
     */
    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        String USERNAME = Jurisdiction.getUsername(); // 当前登录的用户名
        logBefore(logger, USERNAME + "退出系统");
//        Session session = Jurisdiction.getSession(); // 以下清除session缓存
//        session.removeAttribute(Const.SESSION_USER);
//        session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
//        session.removeAttribute(USERNAME + Const.SESSION_allmenuList);
//        session.removeAttribute(USERNAME + Const.SESSION_menuList);
//        session.removeAttribute(USERNAME + Const.SESSION_QX);
//        session.removeAttribute(Const.SESSION_userpds);
//        session.removeAttribute(Const.SESSION_USERNAME);
//        session.removeAttribute(Const.SESSION_USERROL);
//        session.removeAttribute("changeMenu");


        // shiro销毁登录
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("msg", pd.getString("msg"));
        pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); // 读取系统名称
        ModelAndView mv = this.getModelAndView();
//        mv.setViewName("redirect:/");
        mv.setView(new RedirectView("/", false, true));
//        mv.setViewName("/system/index/login");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 获取用户权限
     *
     * @param USERNAME
     * @return
     */
    public Map<String, String> getUQX(String USERNAME) {
        PageData pd = new PageData();
        Map<String, String> map = new HashMap<String, String>();
        try {
            pd.put(Const.SESSION_USERNAME, USERNAME);
            // Fixed bug:使用当前公司组织ID来查询，防止TooManyResultsException
            pd.put("CORP_ID", Jurisdiction.getCorpId());
            pd.put("ROLE_ID", userService.findByUsername(pd).get("ROLE_ID").toString());// 获取角色ID
            pd = roleService.findObjectById(pd); // 获取角色信息
            /**
             * 将cha_qx, del_qx, add_qx, edit_qx进行
             * RightsHelper.testRights(xxx, 0)  :  因为Menu表1已占用,所以0表示有增删改查权限,其他数字menuid表示菜单访问权限
             * 如果包含则设置为1, 否则为0
             */
            map.put("add", RightsHelper.testRights(pd.getString("ADD_QX"), 0) ? "1" : "0"); // 增
            map.put("del", RightsHelper.testRights(pd.getString("DEL_QX"), 0) ? "1" : "0"); // 删
            map.put("edit", RightsHelper.testRights(pd.getString("EDIT_QX"), 0) ? "1" : "0"); // 改
            map.put("cha", RightsHelper.testRights(pd.getString("CHA_QX"), 0) ? "1" : "0"); // 查

            List<PageData> buttonQXnamelist = new ArrayList<PageData>();
            if ("admin".equals(USERNAME)) {
                buttonQXnamelist = fhbuttonService.listAll(pd); // admin用户拥有所有按钮权限
            } else {
                buttonQXnamelist = buttonrightsService.listAllBrAndQxname(pd); // 此角色拥有的按钮权限标识列表
            }
            for (int i = 0; i < buttonQXnamelist.size(); i++) {
                map.put(buttonQXnamelist.get(i).getString("QX_NAME"), "1"); // 按钮权限
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return map;
    }

    /**
     * 更新登录用户的IP
     *
     * @param USERNAME
     * @throws Exception
     */
    public void getRemortIP(String USERNAME) throws Exception {
        PageData pd = new PageData();
        String ip = getRemoteIpAddress();
        pd.put("USERNAME", USERNAME);
        pd.put("IP", ip);
        userService.saveIP(pd);
    }

    private String getRemoteIpAddress() {
        HttpServletRequest request = this.getRequest();
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        return ip;
    }

    /**
     * 判断编码是否存在
     *
     * @return
     */
    @RequestMapping(value = "/bianma")
    @ResponseBody
    public Object hasBianma() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = this.getPageData();
        String bianma = pd.getString("BIANMA");
        bianma = bianma.toLowerCase();
        pd.put("BIANMA", bianma);
        /*
         * asdb  系统数据库
         * admin 系统管理员
         * information_schema  mysql附属数据库
         * mysql  mysql附属数据库
         * performance_schema mysql附属数据库
         */
        if ("asdb".equals(bianma) ||
                "admin".equals(bianma) ||
                "information_schema".equals(bianma) ||
                "mysql".equals(bianma) ||
                "performance_schema".equals(bianma)) {
            // 不能为系统数据库名称
            errInfo = "error";
        } else {
            try {
                pd.put("CORP_ID", Jurisdiction.getTopCorpId());
                if (departmentService.findByBianma(pd) != null) {
                    errInfo = "error";
                }
            } catch (Exception e) {
                errInfo = "error";
                logger.error(e.toString(), e);
            }
        }
        map.put("result", errInfo); // 返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 新用户注册
     *
     * @return
     */
    @RequestMapping(value = "/register")
    @ResponseBody
    public ModelAndView register() {
//        logBefore(logger, Jurisdiction.getUsername() + "/register");
        ModelAndView mv = this.getModelAndView();
        // generate top corp id
        String currentTime = Tools.date2Str(new Date());
        String corpId = UuidUtil.get32UUID();
        PageData pd = getPageData();
        String corpCnName = pd.getString("company_cn_name");
        String mailAddress = pd.getString("mail_address");
        String phoneNumber = pd.getString("phone_number");
        String contactId = pd.getString("contact_id");
        String bianma = pd.getString("BIANMA");
        logBefore(logger, "用户注册" + pd.toString());
        try {
            // step1 注册用户, 机构等信息, 初始化用户数据库
            if (departmentService.findByBianma(pd) != null) {
                log(logger, "用户注册,编码已存在:" + bianma);
                mv.setViewName("/save_result");
                return mv;
            }
            pd.put("CORP_ID", corpId);
            pd.put("NAME", corpCnName);
            pd.put("NAME_EN", bianma);
            pd.put("PARENT_ID", "0");  // top corp id
            pd.put("HEADMAN", contactId);
            pd.put("TEL", phoneNumber);
            pd.put("FUNCTIONS", "总部");
            pd.put("ADDRESS", mailAddress);
            pd.put("BZ", "");
            pd.put("STOP", "0");
            pd.put("STATUS", "1");
            pd.put("CREATE_DATE", currentTime);
            pd.put("MODIFY_DATE", currentTime);
            pd.put("OPERATOR", "admin");
            departmentService.save(pd);
            // sys_menu
            // 动态插入测试功能菜单,需要返回menuid,用于增加权限
            String defaultRights = DEFAULT_RIGHTS1;
            defaultRights = addTestMenuItems(defaultRights, corpId);
            defaultRights += DEFAULT_RIGHTS2;
            // 动态插入日志菜单, bug fixed: 否则会出现大量重复的日志菜单
            // 日志菜单不需要每次创建，每个公司的url动态变化即可
//            defaultRights = addLogMenuItems(defaultRights, corpId);
            BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(defaultRights));//用菜单ID做权处理
            logger.info("Default rights:" + defaultRights + ";bigint:" + rights);
            // role info
            PageData pdRole = new PageData();
            pdRole.put("ROLE_ID", UuidUtil.get32UUID());
            pdRole.put("ROLE_NAME", corpCnName + "管理员");
            pdRole.put("RIGHTS", rights.toString());
            pdRole.put("PARENT_ID", "1");              // 普通管理员必须是1, 只有超级管理员是0
            pdRole.put("ADD_QX", rights.toString());
            pdRole.put("DEL_QX", rights.toString());
            pdRole.put("EDIT_QX", rights.toString());
            pdRole.put("CHA_QX", rights.toString());
            pdRole.put("CORP_ID", corpId);
            roleService.add(pdRole);
            // user info
            String passwd = new SimpleHash("SHA-1", bianma, "abcd1234").toString();    //密码加密
            PageData pdUser = new PageData();
            pdUser.put("USER_ID", UuidUtil.get32UUID());
            pdUser.put("USERNAME", bianma);
            pdUser.put("PASSWORD", passwd);  // default password
            pdUser.put("NAME", corpCnName + "管理员");
            pdUser.put("ROLE_ID", pdRole.getString("ROLE_ID"));
            pdUser.put("LAST_LOGIN", currentTime);
            pdUser.put("IP", getRemoteIpAddress());
            pdUser.put("STATUS", "0");  // normal
            pdUser.put("BZ", "");
            pdUser.put("SKIN", "");
            pdUser.put("EMAIL", "");
            pdUser.put("NUMBER", bianma);
            pdUser.put("PHONE", "");
            pdUser.put("CORP_ID", corpId);
            pdUser.put("CREATE_DATE", currentTime);
            pdUser.put("MODIFY_DATE", currentTime);
            pdUser.put("OPERATOR", "admin");
            userService.saveU(pdUser);
            //
            // default cell rules
            saveDefaultCellRules(corpId);
            //
            // database info + sys_logs table
            PageData pdDb = new PageData();
            pdDb.put("DATABASE_NAME", bianma);
            departmentService.createDatabase(pdDb);
            pdDb.put("SCHEMA", bianma);
            pdDb.put("TABLE_NAME_EN", "sys_logs");
            departmentService.createLogTable(pdDb);
            // step2 模拟登录
            String errInfo = doSession(pd, bianma, "abcd1234");
            if (StringUtil.strIsNotEmpty(errInfo)) {
                throw new Exception("用户模拟登录失败: " + bianma);
            }
            doLoadMenu();
            // step3 加入nginx路由
            addProxy(bianma, corpId);
            // step4 进入导航页面
            String domain = this.getRequest().getScheme() + "://" + bianma + ".aiinfosys.cn/";
//            mv.setViewName("redirect:/nav/my_forms?domain=" + domain);   //redirect to navigation
            mv.setView(new RedirectView("/nav/my_forms?domain=" + domain, false, true));
            // Initial OK
            pd.put("RESULT", "0");
            if (!StringUtil.isEmpty(mailAddress)) {
                try {
                    Properties prop = PropertiesLoaderUtils.loadAllProperties("context.properties");// 生产环境
                    SendEmailUtil send = new SendEmailUtil(
                            mailAddress, prop.getProperty("e.aiinfosys"),
                            "感谢您使用蚍蜉智慧云, 赶快在云上打造您的企业应用吧.<br/><br/>" +
                                    "您的" + prop.getProperty("e.aiinfosys") +
                                    "已注册成功, 独立域名为:https://" + bianma + ".aiinfosys.cn<br/>账号:" +
                                    bianma + "<br/>密码:abcd1234<br/>请尽快登录修改初始密码, 谢谢!", "");
                    send.send();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Properties prop = PropertiesLoaderUtils.loadAllProperties("context.properties");// 生产环境
                    SendEmailUtil send = new SendEmailUtil(
                            "support@aiinfosys.cn", "support@aiinfosys.cn",
                            "有新用户注册蚍蜉智慧云.<br/><br/>" +
                                    "用户邮箱：" + prop.getProperty("e.aiinfosys") + "<br/>" +
                                    "用户域名:https://" + bianma + ".aiinfosys.cn<br/>" +
                                    "账号:" + bianma + "<br/>" +
                                    "初始密码:abcd1234", "");
                    send.send();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            log(logger, "用户注册" + e.getLocalizedMessage());
            // Initial NG
            pd.put("RESULT", "1");
        }
        mv.addObject("pd", pd);
        return mv;
    }

    private void saveDefaultCellRules(String corpId) {
        PageData pd = new PageData();
        try {
            pd.put("CORP_ID", corpId);
            pd.put("STATUS", "1");

            pd.put("CELLRULE_ID", this.get32UUID());    //主键
            pd.put("NAME", "小数1到10位");
            pd.put("SORT_NO", 1);
            pd.put("RULES", "^[0-9\\.]{1,10}$");
            cellruleService.save(pd);
            pd.put("CELLRULE_ID", this.get32UUID());    //主键
            pd.put("NAME", "中英数字1-10个");
            pd.put("SORT_NO", 2);
            pd.put("RULES", "^[\\u4e00-\\u9fa5_a-zA-Z0-9]{1,10}$");
            cellruleService.save(pd);
            pd.put("CELLRULE_ID", this.get32UUID());    //主键
            pd.put("NAME", "英数字1-10位");
            pd.put("SORT_NO", 3);
            pd.put("RULES", "^[a-zA-Z0-9]{1,10}$");
            cellruleService.save(pd);
            pd.put("CELLRULE_ID", this.get32UUID());    //主键
            pd.put("NAME", "日期时间格式yyyy-MM-dd hh:mm:ss");
            pd.put("SORT_NO", 4);
            pd.put("RULES", "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$");
            cellruleService.save(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLoadMenu() throws Exception {
        Session session = Jurisdiction.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER); // 读取session中的用户信息(单独用户信息)
        if (user == null) {
            return;
        }
        User userRole = (User) session.getAttribute(Const.SESSION_USERROL); // 读取session中的用户信息(含角色信息)
        if (null == userRole) {
            user = userService.getUserAndRoleById(user.getUSER_ID()); // 通过用户ID读取用户信息和角色信息
            session.setAttribute(Const.SESSION_USERROL, user); // 存入session
        } else {
            user = userRole;
        }
        String USERNAME = user.getUSERNAME();
        Role role = user.getRole(); // 获取用户角色
        String roleRights = role != null ? role.getRIGHTS() : ""; // 角色权限(菜单权限)
        session.setAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS, roleRights); // 将角色权限存入session
        session.setAttribute(Const.SESSION_USERNAME, USERNAME); // 放入用户名到session
        List<Menu> allmenuList = null;
        if (null == session.getAttribute(USERNAME + Const.SESSION_allmenuList)) {
            PageData pd = new PageData();
            pd.put("PARENT_ID", "0");
            allmenuList = menuService.listAllMenuQx(pd); // 获取所有菜单
            if (Tools.notEmpty(roleRights)) {
                allmenuList = this.readMenu(allmenuList, roleRights); // 根据角色权限获取本权限的菜单列表
            }
            session.setAttribute(USERNAME + Const.SESSION_allmenuList, allmenuList);// 菜单权限放入session中
        } else {
            allmenuList = (List<Menu>) session.getAttribute(USERNAME + Const.SESSION_allmenuList);
        }
        // 切换菜单处理=====start

        // 切换菜单处理=====end
        if (null == session.getAttribute(USERNAME + Const.SESSION_QX)) {
            session.setAttribute(USERNAME + Const.SESSION_QX, this.getUQX(USERNAME)); // 按钮权限放到session中
        }
    }

    private String addTestMenuItems(String defaultRights, String corpId) throws Exception {
        Integer pId = menuService.addMenuItem("业务管理(可修改)", "#", "2", "1", "0", "0", corpId);
        defaultRights += pId;
        defaultRights += ",";
        int menuId = menuService.addMenuItem("测试功能1", "#", "1", "1", pId.toString(), "1", corpId);
        defaultRights += menuId;
        defaultRights += ",";
        menuId = menuService.addMenuItem("测试功能2", "#", "1", "1", pId.toString(), "2", corpId);
        defaultRights += menuId;
        defaultRights += ",";
        menuId = menuService.addMenuItem("测试功能3", "#", "1", "1", pId.toString(), "3", corpId);
        defaultRights += menuId;
        return defaultRights;
    }

    private String addLogMenuItems(String defaultRights, String corpId) throws Exception {

        Integer pId = menuService.addMenuItem("日志管理", "#", "2", "1", "0", "99", corpId);
        defaultRights += pId;
        defaultRights += ",";
        int menuId = menuService.addMenuItem("操作日志", "logs/list.do", "1", "1", pId.toString(), "1", corpId);
        defaultRights += menuId;

        return defaultRights;
    }

    private boolean addProxy(String bianma, String corpId) throws Exception {
        try {
            String newShell = "cd /usr/local/nginx/vhost && sh new_domain.sh " + bianma + " " + corpId;
            logger.info("addProxy() ==>Running nginx:" + newShell);
            String[] cmd = new String[]{"/bin/sh", "-c", newShell};
            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            logger.info("addProxy() returns" + result);

            int exitVal = ps.waitFor();
            logger.info("addProxy() Process exitValue: " + exitVal);
            return true;
        } catch (Exception e) {
            logger.error("----error-----");
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String args[]) {
//        String defaultRights = DEFAULT_RIGHTS1;
//        defaultRights += "687,688,689";
//        defaultRights += DEFAULT_RIGHTS2;
//        BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(defaultRights));//用菜单ID做权处理
//        System.out.println(rights.toString());
        List<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        List<String> list1 = new LinkedList<String>();
        list1.addAll(list);
        list1.add(2, "1");
        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i));
        }
    }
}
