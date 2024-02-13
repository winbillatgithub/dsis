package com.as.service.system.logs.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.util.PageData;
import com.as.service.system.logs.LogsManager;

/** 
 * 说明： 电子日志
 * 创建人：antispy
 * 创建时间：2016-09-21
 * @version
 */
@Service("logsService")
public class LogsService implements LogsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("LogsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("LogsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("LogsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("LogsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("LogsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("LogsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		dao.delete("LogsMapper.deleteAll", pd);
	}

    /**全部删除
     * @param sql
     * @throws Exception
     */
    public void truncate(String sql) throws Exception{
        dao.delete("LogsMapper.truncate", sql);
    }
}

