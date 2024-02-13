package com.as.service.business.tables.impl;

import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.service.business.tables.TablesManager;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 模板维护
 * 创建人：antispy
 * 创建时间：2016-09-09
 * @version
 */
@Service("tablesService")
public class TablesService implements TablesManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("TablesMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("TablesMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("TablesMapper.edit", pd);
	}

	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateStatus(PageData pd)throws Exception{
		dao.update("TablesMapper.updateStatus", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TablesMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("TablesMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TablesMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("TablesMapper.deleteAll", ArrayDATA_IDS);
	}

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TablesMapper.findByBianma", pd);
	}

	/**创建物理表
	 * @param pd
	 * @throws Exception
	 */
	public void createTable(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("TablesMapper.createTable", pd);
	}
	
	/**rename物理表
	 * @param pd
	 * @throws Exception
	 */
	public void renameTable(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("TablesMapper.renameTable", pd);
	}

	/**删除物理表
	 * @param pd
	 * @throws Exception
	 */
	public void dropTable(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("TablesMapper.dropTable", pd);
	}
	
}

