package com.as.service.system.menu.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.as.dao.DaoSupport;
import com.as.entity.system.Menu;
import com.as.service.system.menu.MenuManager;
import com.as.util.PageData;

/** 
 * 类名称：MenuService 菜单处理
 * 创建人：antispy qq  3  1 3 5 9 6 7 9 0[青苔]
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("menuService")
public class MenuService implements MenuManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 通过ID获取其子一级菜单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listSubMenuByParentId(PageData pd) throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listSubMenuByParentId", pd);
	}
	
	/**
	 * 通过菜单ID获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("MenuMapper.getMenuById", pd);
	}
	
	/**
	 * 新增菜单
	 * @param menu
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception {
		dao.save("MenuMapper.insertMenu", menu);
	}
	
	/**
	 * 取最大ID
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMaxId(PageData pd) throws Exception {
		return (PageData) dao.findForObject("MenuMapper.findMaxId", pd);
	}
	
	/**
	 * 删除菜单
	 * @param MENU_ID
	 * @throws Exception
	 */
	public void deleteMenuById(String MENU_ID) throws Exception {
		dao.save("MenuMapper.deleteMenuById", MENU_ID);
	}
	
	/**
	 * 编辑
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public void edit(Menu menu) throws Exception {
		 dao.update("MenuMapper.updateMenu", menu);
	}
	
	/**
	 * 保存菜单图标 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData editicon(PageData pd) throws Exception {
		return (PageData)dao.findForObject("MenuMapper.editicon", pd);
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单管理)(递归处理)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenu(PageData pd) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(pd);
		for(Menu menu : menuList){
			menu.setMENU_URL("/menu/toEdit.do?MENU_ID="+menu.getMENU_ID());
			pd.put("PARENT_ID", menu.getMENU_ID());
			menu.setSubMenu(this.listAllMenu(pd));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}

	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单列表)(递归处理)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuQx(PageData pd) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(pd);
		for(Menu menu : menuList){
			pd.put("PARENT_ID", menu.getMENU_ID());
			menu.setSubMenu(this.listAllMenuQx(pd));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}

    /**
     * 增加菜单
     * @param menuName
     * @param menuUrl
     * @param menuType
     * @param menuState
     * @param parentId
     * @param menuOrder
     * @param corpId
     * @return
     * @throws Exception
     */
    public int addMenuItem(String menuName, String menuUrl, String menuType, String menuState, String parentId,
                           String menuOrder, String corpId) throws Exception {
        PageData pd = new PageData();
        int parentMenuId = Integer.parseInt(this.findMaxId(pd).get("MID").toString());
        parentMenuId += 1;
        Menu menu = new Menu();
        menu.setMENU_NAME(menuName);
        menu.setMENU_ID(String.valueOf(parentMenuId));
        menu.setMENU_URL(menuUrl);
        menu.setMENU_TYPE(menuType);
        menu.setMENU_STATE(menuState);
        menu.setPARENT_ID(parentId);
        menu.setMENU_ORDER(menuOrder);
        menu.setMENU_ICON("menu-icon fa fa-leaf black");//默认菜单图标
        menu.setCORP_ID(corpId);
        this.saveMenu(menu); //保存菜单
        return parentMenuId;
    }
}
