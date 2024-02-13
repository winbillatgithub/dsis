package com.as.controller.files;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.as.controller.base.BaseController;
import com.as.entity.files.FileInfo;
import com.as.util.JsonHelper;

@Controller
@RequestMapping(value = "/admin/files/uploadedFileInfoController")
public class UploadedFileInfoController  extends BaseController {

	//@Resource(name = "fileInfoService")
	//private IFileInfoService service;

	@RequestMapping(value = "saveData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveData(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("saveData() ==>");

		//String pDate = request.getParameter("pDate");
		String inserted = request.getParameter("inserted");
		String updated = request.getParameter("updated");
		String deleted = request.getParameter("deleted");
		List<FileInfo> riInserted = null;
		List<FileInfo> riUpdated = null;
		List<FileInfo> riDeleted = null;

		try {
			if (inserted != null) {
				riInserted = JsonHelper.jsonToBeanList(String.valueOf(inserted), FileInfo.class);
				// 调用插入方法
				for (int index = 0; index < riInserted.size(); index++) {
					FileInfo ri = riInserted.get(index);
					//service.insertSelective(ri);
				}
			}
			if (updated != null) {
				riUpdated = JsonHelper.jsonToBeanList(String.valueOf(updated), FileInfo.class);
				// 调用更新方法
				for (int index = 0; index < riUpdated.size(); index++) {
					FileInfo ri = riUpdated.get(index);
					//service.updateByPrimaryKey(ri);
				}
			}
			if (deleted != null) {
				riDeleted = JsonHelper.jsonToBeanList(String.valueOf(deleted), FileInfo.class);
				// 调用删除方法
				for (int index = 0; index < riDeleted.size(); index++) {
					FileInfo ri = riDeleted.get(index);
					//service.deleteByPrimaryKey(ri.getId());
				}
			}
		} catch (Exception e) {
			return jsonResult(false, "错误信息:" + e.getMessage());
		}
		logger.debug("saveData() <==");
		return jsonResult(true, "保存成功");
	}

}