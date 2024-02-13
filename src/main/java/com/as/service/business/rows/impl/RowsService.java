package com.as.service.business.rows.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.service.business.rows.RowsManager;

/** 
 * 说明： 模板指标对照表
 * 创建人：antispy
 * 创建时间：2016-09-09
 * @version
 */
@Service("rowsService")
public class RowsService implements RowsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("RowsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("RowsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("RowsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("RowsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RowsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RowsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("RowsMapper.deleteAll", ArrayDATA_IDS);
	}
	/**增加列
	 * @param pd
	 * @throws Exception
	 */
	public void addColumn(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("RowsMapper.addColumn", pd);
	}
	/**删除列
	 * @param pd
	 * @throws Exception
	 */
	public void dropColumn(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("RowsMapper.dropColumn", pd);
	}
	/**修改列
	 * @param pd
	 * @throws Exception
	 */
	public void changeColumn(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("RowsMapper.changeColumn", pd);
	}
	/**修改列
	 * @param pd
	 * @throws Exception
	 */
	public void modifyColumn(PageData pd)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		pd.put("SCHEMA", schema);
		dao.update("RowsMapper.modifyColumn", pd);
	}
}

