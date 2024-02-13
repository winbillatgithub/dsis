package com.as.controller.files;


import com.as.controller.base.BaseController;
import com.as.entity.Constant;
import com.as.entity.files.FileInfo;
import com.as.entity.system.User;
import com.as.service.business.files.FileInfoManager;
import com.as.service.business.template.TemplateManager;
import com.as.util.Const;
import com.as.util.DocConverter;
import com.as.util.FastDFSHelper;
import com.as.util.FileHelper;
import com.as.util.JsonHelper;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.UuidUtil;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/files")
public class FilesUploadController  extends BaseController {

	// Uploaded file path
	private String uploadFilePath = null;

	// this will store uploaded files
	@Resource(name = "fileInfoService")
	private FileInfoManager fileInfoService;
	@Resource(name="templateService")
	public TemplateManager templateService;

	@RequestMapping(value = "fileView")
	public ModelAndView returnFileView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/admin/files/files_upload");

		// uploadFilePath = this.getContextValueByKey("upload_path");

		return modelAndView;
	}

	/***************************************************
	 * URL: /upload
	 * doPost(): upload the files and other parameters
	 ****************************************************/
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException, Exception {
		BaseController.logger.debug("doPost() ==>");

		//System.out.println("文件上传");
		MultipartRequestHandler mrh = new MultipartRequestHandler(this.uploadFilePath, this.templateService);
		Map<String, Object> result = mrh.FlashUpload(request,response);
		if (Constant._SUCCESS_ == result.get(Constant._RESULT_)) {
			String path = (String) result.get(Constant._PATH_);
			BaseController.logger.info("doPost() path=" + path);
			// Analyze the doc,docx file
//				ParseDocMetadata pdm = new ParseDocMetadata(path, metaData, request);
//				pdm.openFile();
		} else {
//				response.setStatus(502, String.valueOf(result.get(Constant._REASON_)));
			response.sendError(501, String.valueOf(result.get(Constant._REASON_)));
			BaseController.logger.error("doPost() Error occurred when uploading doc file.");
			//throw new ServletException(String.valueOf(result.get(Constant._REASON_)));
		}
		mrh = null;
		BaseController.logger.debug("doPost() <==");
	}

	/**
	 * 上传文件
	 * @param request
	 * @param upload
	 * @throws Exception 
	 */
	@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadFiles(MultipartHttpServletRequest request, @RequestParam("file") MultipartFile upload, String mbType) throws Exception{
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		if (user == null) {
			String topCorpBianma = request.getParameter("topCorpBianma");
			user = new User();
			user.setTOP_CORP_BIANMA(topCorpBianma);
			session.setAttribute(Const.SESSION_USER, user);
		}

		String id = request.getParameter("id");
		String tablesId = request.getParameter("tablesId");
		String columnsId = request.getParameter("columnsId");
		String columnsValue = request.getParameter("columnsValue");
        // id (rowId) 可能为空,公开表单上传的情况下
		if (StringUtil.strIsEmpty(tablesId) || StringUtil.strIsEmpty(columnsId)) {
			return jsonResult(false, "上传失败:" + "tablesId和columnsId不能为空");
		}

		FileInfo file = new FileInfo();

		String fileType = FileHelper.getFileType(upload.getOriginalFilename());
		String path = null;
		String localPath = null;
		String pdfPath = null;
		long pdfLength = 0l;
		try {
			// Convert to pdf when file type is office
			
			if ("txt".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)|| "xls".equalsIgnoreCase(fileType)|| "doc".equalsIgnoreCase(fileType) || "docx".equalsIgnoreCase(fileType) ||
					"ppt".equalsIgnoreCase(fileType) || "pptx".equalsIgnoreCase(fileType)) {
				localPath = request.getSession().getServletContext().getRealPath("/") + UuidUtil.get32UUID() + "." + fileType;
			}
			path = FastDFSHelper.fastDFSUpload(upload.getInputStream(), upload.getOriginalFilename(), upload.getSize(), localPath);
			if (localPath != null) {
				DocConverter dc = new DocConverter(localPath);
		    	String pdfLocal = dc.doc2pdf();
		    	File pdfLocalFile = new File(pdfLocal);
		    	pdfLength = pdfLocalFile.length();
		    	InputStream is = new FileInputStream(pdfLocalFile);
		    	pdfPath = FastDFSHelper.fastDFSUpload(is, pdfLocalFile.getName(), pdfLength, null);
		    	// 删除本地
		    	new File (localPath).delete();
		    	is.close();
		    	pdfLocalFile.delete();
			}
		
		
		} catch (Exception e) {
			return jsonResult(false, "上传失败:" + e.getMessage());
		}
		file.setId(UuidUtil.get32UUID());
		file.setCorpId(user.getTOP_CORP_ID());	
		file.setFileName(upload.getOriginalFilename());
		file.setFileUrl(path);
		file.setPdfUrl(pdfPath);	
		file.setFileSize(upload.getSize());
		file.setFileType(fileType);
		file.setFileStatus(Constant.FILE_UPLOAD_ALREADY);
		file.setCreateDate(new Date());
		file.setModifyDate(file.getCreateDate());
		file.setOperator((user.getUSERNAME() == null) ? "unknown" : user.getUSERNAME());

		try {
			file.setFileStatus(Constant.FILE_IMPORT_SUCCESS);
			// metadata of fastdfs, fileExtNamesqlfileLength4773fileNameaics-user.sql
			PageData pd = new PageData();
			pd.put(columnsId, file.toString());
			pd.put("id", id);				// row key id
            /*
			 * UPDATE xiaoniukafu.STUDENT_PHYSICAL SET xx = 'aics-voice.log|2852|log|group1/M00/00/00/ZcmE3o533.log' WHERE STUDENT_PHYSICAL_ID = 'null'
			 * Update current column
			 * 存在id为空的情况, 因为从公开表单进入,可能id为空.
			 * 这种情况下,用户需要保存按钮再次保存文件上传数据, 否则文件会丢失
             */
            if (!StringUtils.isEmpty(id)) {
                templateService.save(tablesId, pd, null);
            }
			// Save to file info table
			pd = file.toPageData();
			fileInfoService.save(pd);
			// Delete file in fastdfs in columnsValue
			if (StringUtil.strIsNotEmpty(columnsValue)) {
				String temp[] = columnsValue.split("\\|");
				String fileId = temp[3];
				String uuid = temp[4];
				FastDFSHelper.deleteFile(fileId);
				pd.put("ID", uuid);
				fileInfoService.delete(pd);
			}
			return jsonResult(true, file.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResult(false, "上传失败:" + e.getMessage());
		} finally {
		}
	}

	/***************************************************
	 * URL: /fileUploadForm
	 * fileUploadForm(): popup the file upload form
	 ****************************************************/
	@RequestMapping(value = "fileUploadForm")
	public ModelAndView fileUploadForm(String id, String tablesId, String columnsId, String columnsValue) {
		BaseController.logger.debug("fileUploadForm() ==>");

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/template/files/files_upload_form");

		modelAndView.addObject("id", id);
		modelAndView.addObject("tablesId", tablesId);
		modelAndView.addObject("columnsId", columnsId);
		modelAndView.addObject("columnsValue", columnsValue);
		modelAndView.addObject("topCorpBianma", Jurisdiction.getTopCorpBianma());

		BaseController.logger.debug("fileUploadForm() <==");
		return modelAndView;
	}

	@RequestMapping(value = "fileListView")
	public ModelAndView fileListView(String catelogId) {
		BaseController.logger.debug("fileListView() ==>");

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/admin/files/files_upload_list");
		modelAndView.addObject("catelogId", catelogId);

		BaseController.logger.debug("fileListView() <==");
		return modelAndView;
	}

	@RequestMapping(value = "saveUploadedFileInfo",  method = RequestMethod.POST )
	@ResponseBody
	public Map<String, Object> saveUploadedFileInfo(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		BaseController.logger.debug("saveUploadedFileInfo() ==>");
		boolean bRet = false;
		try {
			/*
			 * Step n, save basic file informaton to db
			 */
			FileInfo fi = new FileInfo();
//			fi.setId(metaData.getId());
//			fi.setFilename(metaData.getFileName());
//			fi.setFilesize(metaData.getFileSize());
//			fi.setFiletype(metaData.getFileType());
//			String date = metaData.getUploadDate();
//			if (date != null) {
//				fi.setCreateDate(DateHelper.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
//			}
			//templateService.insert(fi);
		} catch (Exception ioe) {
			ioe.printStackTrace();
			return jsonResult(false, "错误信息:" + ioe.getMessage());
		}

		if (bRet) {
			return jsonResult(true, Constant._SAVE_SUCCESS_);
		}
		return jsonResult(false, Constant._SAVE_FILE_INFO_FAILED_);
	}

	@RequestMapping(value = "getFileInfoList", method = RequestMethod.POST )
	@ResponseBody
	public Map<String,Object> getFileInfoList(HttpServletRequest request, HttpServletResponse response,
			String id, String fileName, String fileType) {
		BaseController.logger.debug("getFileInfoList() ==>");
		int pageId = Integer.parseInt(request.getParameter("page"));
		int pageSize = Integer.parseInt(request.getParameter("rows"));
		int startNum = pageSize * (pageId - 1);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startNum", startNum);
		paramMap.put("pageSize", pageSize);
		paramMap.put("id", id);
		paramMap.put("fileName", fileName);
		paramMap.put("fileType", fileType);
//		paramMap.put("accurate", "");
		Map<String,Object> paginationMap = null; // templateService.selectAllByPagination(paramMap);
		String returnJsonString = JsonHelper.mapToJson(paginationMap, "yyyy-MM-dd HH:mm:ss");
		BaseController.logger.debug("getFileInfoList() <== returns=" + returnJsonString);
		return paginationMap;
	}

	@RequestMapping(value = "getCatelogFileList", method = RequestMethod.POST )
	@ResponseBody
	public Map<String,Object> getCatelogFileList(HttpServletRequest request, HttpServletResponse response,
			String catelogId) {
		BaseController.logger.debug("getCatelogFileList() ==>");
		int pageId = Integer.parseInt(request.getParameter("page"));
		int pageSize = Integer.parseInt(request.getParameter("rows"));
		int startNum = pageSize * (pageId - 1);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startNum", startNum);
		paramMap.put("pageSize", pageSize);
		paramMap.put("catelogId", catelogId);
		Map<String,Object> paginationMap = null; //templateService.selectAllByPagination(paramMap);
		String returnJsonString = JsonHelper.mapToJson(paginationMap, "yyyy-MM-dd HH:mm:ss");
		BaseController.logger.debug("getCatelogFileList() <== returns=" + returnJsonString);
		return paginationMap;
	}

	@RequestMapping(value = "deleteCatelogFile", method = RequestMethod.POST )
	@ResponseBody
	public Map<String,Object> deleteCatelogFile(HttpServletRequest request, HttpServletResponse response,
			Long fileId, Long catelogId, Long gCatelogId, String url) {
		BaseController.logger.debug("deleteCatelogFile() ==> id=" + fileId);
		boolean bRet = false;
		String errorMsg = "";
		try {
			// templateService.deleteByPrimaryKey(fileId);
			// Need to delete files on fastdfs?
			FastDFSHelper.deleteFile(url);
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
			bRet = false;
		}
		BaseController.logger.debug("deleteCatelogFile() <== ");
		if (bRet) {
			return jsonResult(true, Constant._DELETE_SUCCESS_);
		}
		return jsonResult(false, errorMsg);
	}
}