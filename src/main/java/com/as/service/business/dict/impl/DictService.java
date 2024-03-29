package com.as.service.business.dict.impl;

import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.entity.business.Dict;
import com.as.service.business.dict.DictManager;
import com.as.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 用户自定义编码
 * 创建人：antispy
 * 创建时间：2015-12-16
 * @version
 */
@Service("dictService")
public class DictService implements DictManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DictMapper.save", pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DictMapper.delete", pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DictMapper.edit", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DictMapper.datalistPage", page);
	}
    public List<PageData> listAll(PageData page)throws Exception{
        return (List<PageData>)dao.findForList("DictMapper.datalist", page);
    }
    public List<PageData> listAllForControls(PageData page)throws Exception{
        return (List<PageData>)dao.findForList("DictMapper.datalistcontrols", page);
    }
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DictMapper.findById", pd);
	}

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DictMapper.findByBianma", pd);
	}

	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dict> listSubDictByParentId(String parentId) throws Exception {
		return (List<Dict>) dao.findForList("DictMapper.listSubDictByParentId", parentId);
	}

    /**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dict> listAllDict(String parentId) throws Exception {
		List<Dict> dictList = this.listSubDictByParentId(parentId);
		for(Dict dict : dictList){
			dict.setTreeurl("dict/list.do?DICT_ID="+dict.getDICT_ID());
			dict.setSubDict(this.listAllDict(dict.getDICT_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}

	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DictMapper.findFromTbs", pd);
	}

    /**通过id获取子编码的最大编号
     * @param pd
     * @throws Exception
     */
    public PageData findMaxSortNo(PageData pd)throws Exception {
        return (PageData)dao.findForObject("DictMapper.findMaxSortNo", pd);
    }
}

