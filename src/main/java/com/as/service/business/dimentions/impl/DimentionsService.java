package com.as.service.business.dimentions.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.entity.business.Dimentions;
import com.as.util.PageData;
import com.as.service.business.dimentions.DimentionsManager;

/** 
 * 说明： 数据字典
 * 创建人：antispy
 * 创建时间：2015-12-16
 * @version
 */
@Service("dimentionsService")
public class DimentionsService implements DimentionsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DimentionsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DimentionsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DimentionsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DimentionsMapper.datalistPage", page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DimentionsMapper.findById", pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DimentionsMapper.findByBianma", pd);
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dimentions> listSubDictByParentId(PageData pd) throws Exception {
		return (List<Dimentions>) dao.findForList("DimentionsMapper.listSubDictByParentId", pd);
	}
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dimentions> listAllDict(PageData pd) throws Exception {
		List<Dimentions> dictList = this.listSubDictByParentId(pd);
		for(Dimentions dict : dictList){
			dict.setTreeurl("dimentions/list.do?DIMENTIONS_ID="+dict.getDIMENTIONS_ID());
			pd.put("PARENT_ID", dict.getDIMENTIONS_ID());
			dict.setSubDict(this.listAllDict(pd));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}

	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DimentionsMapper.findFromTbs", pd);
	}

	public List<Dimentions> listSubDictByParentId(String parentId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

