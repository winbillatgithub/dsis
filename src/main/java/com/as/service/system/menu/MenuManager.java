package com.as.service.system.menu;

import java.util.List;

import com.as.entity.system.Menu;
import com.as.util.PageData;


/**说明：MenuService 菜单处理接口
 * @author as 313596790
 */
public interface MenuManager {

	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listSubMenuByParentId(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception;

	/**
	 * @param menu
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception;

	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMaxId(PageData pd) throws Exception;

	/**
	 * @param MENU_ID
	 * @throws Exception
	 */
	public void deleteMenuById(String MENU_ID) throws Exception;

	/**
	 * @param menu
	 * @throws Exception
	 */
	public void edit(Menu menu) throws Exception;

	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData editicon(PageData pd) throws Exception;

	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenu(PageData pd) throws Exception;

	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuQx(PageData pd) throws Exception;

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
                             String menuOrder, String corpId) throws Exception;
}
