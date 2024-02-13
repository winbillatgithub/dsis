package com.as.service.system.department.impl;

import java.util.List;

import javax.annotation.Resource;

import com.as.util.Jurisdiction;
import org.springframework.stereotype.Service;

import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.entity.system.Department;
import com.as.util.PageData;
import com.as.service.system.department.DepartmentManager;

/** 
 * 说明： 组织机构
 * 创建人：antispy
 * 创建时间：2015-12-16
 * @version
 */
@Service("departmentService")
public class DepartmentService implements DepartmentManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DepartmentMapper.save", pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DepartmentMapper.delete", pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DepartmentMapper.edit", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DepartmentMapper.datalistPage", page);
	}

	/**列表-只列出第一层子公司
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> datalistSubDepartmentPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DepartmentMapper.datalistSubDepartmentPage", page);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DepartmentMapper.findById", pd);
	}

	/**通过id获取父节点
	 * @param pd
	 * @throws Exception
	 */
	public Department findParentById(PageData pd)throws Exception{
		return (Department)dao.findForObject("DepartmentMapper.findParentById", pd);
	}

	/**通过id获取顶级父节点
	 * @param pd
	 * @throws Exception
	 */
	public Department findTopParentIdById(PageData pd)throws Exception{
		String corpId = pd.getString("CORP_ID");
		Department departTop = null;
		// 最多5级
		for (int index = 0; index < 5; index ++) {
			Department depart = findParentById(pd);
			if ("0".equals(depart.getCORP_ID())) {
				break;
			}
			corpId = depart.getCORP_ID();
			pd.put("CORP_ID", depart.getCORP_ID());
		}
		pd.put("CORP_ID", corpId);
		PageData pdCorp = this.findById(pd);
		departTop = new Department();
		departTop.setCORP_ID(pdCorp.getString("CORP_ID"));
		departTop.setBIANMA(pdCorp.getString("BIANMA"));
		return departTop;
	}

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DepartmentMapper.findByBianma", pd);
	}

	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Department> listSubDepartmentByParentId(String parentId) throws Exception {
		return (List<Department>) dao.findForList("DepartmentMapper.listSubDepartmentByParentId", parentId);
	}

	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Department> listAllDepartment(String parentId) throws Exception {
		List<Department> departmentList = this.listSubDepartmentByParentId(parentId);
		for(Department depar : departmentList){
			depar.setTreeurl("department/list.do?CORP_ID="+depar.getCORP_ID());
			depar.setSubDepartment(this.listAllDepartment(depar.getCORP_ID()));
			depar.setTarget("treeFrame");
		}
		return departmentList;
	}
	public List<Department> listAllDepartment(PageData pd) throws Exception {
		return (List<Department>) dao.findForList("DepartmentMapper.listAllDepartment", pd);
	}

	/**创建数据库
	 * @param pd
	 * @throws Exception
	 */
	public void createDatabase(PageData pd)throws Exception {
		dao.update("DepartmentMapper.createDatabase", pd);
	}
	
	/**删除数据库
	 * @param pd
	 * @throws Exception
	 */
	public void dropDatabase(PageData pd)throws Exception {
		dao.update("DepartmentMapper.dropDatabase", pd);
	}

    /**创建物理表
     * @param pd
     * @throws Exception
     */
    public void createLogTable(PageData pd)throws Exception {
        dao.update("DepartmentMapper.createLogTable", pd);
    }
}

