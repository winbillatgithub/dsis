package com.as.service.system.department;

import java.util.List;

import com.as.entity.Page;
import com.as.entity.system.Department;
import com.as.util.PageData;

/** 
 * 说明： 组织机构接口类
 * 创建人：antispy
 * 创建时间：2015-12-16
 * @version
 */
public interface DepartmentManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	public List<PageData> datalistSubDepartmentPage(Page page)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**通过id获取父节点
	 * @param pd
	 * @throws Exception
	 */
	public Department findParentById(PageData pd)throws Exception;

	/**通过id获取顶级父节点
	 * @param pd
	 * @throws Exception
	 */
	public Department findTopParentIdById(PageData pd)throws Exception;

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception;

	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Department> listSubDepartmentByParentId(String parentId) throws Exception;

	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Department> listAllDepartment(String parentId) throws Exception;
	public List<Department> listAllDepartment(PageData pd) throws Exception;
	/**创建数据库
	 * @param pd
	 * @throws Exception
	 */
	public void createDatabase(PageData pd)throws Exception;
	/**删除数据库
	 * @param pd
	 * @throws Exception
	 */
	public void dropDatabase(PageData pd)throws Exception;
    /**创建物理表-日志表
     * @param pd
     * @throws Exception
     */
    public void createLogTable(PageData pd)throws Exception;
}

