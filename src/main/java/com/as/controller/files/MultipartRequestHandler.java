package com.as.controller.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.as.entity.Constant;
import com.as.service.business.template.TemplateManager;

public class MultipartRequestHandler {
	protected static Logger baseLogger = LoggerFactory.getLogger(MultipartRequestHandler.class);

	private String uploadFilePath = null;
	private TemplateManager templateService;

	MultipartRequestHandler(String path, TemplateManager templateService) {
		this.uploadFilePath = path;
		this.templateService = templateService;
	}

	// this method is used to get file name out of request headers
	// 
	public Map<String, Object> FlashUpload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		baseLogger.trace("FlashUpload() ==>");

		Map<String, Object> result = new HashMap<String, Object>(); 
		response.setContentType("text/html;charset=utf-8;");
		request.setCharacterEncoding("utf-8");

		// 设置接收的编码格式
		request.setCharacterEncoding("UTF-8");
		String fileRealPath = "";// 文件存放真实地址

		// 名称 界面编码 必须 和request 保存一致..否则乱码
		String name = request.getParameter("name");
		String folder = request.getParameter("folder");
		System.out.println("folder="+folder+"|name="+name);

		String firstFileName = "";
		// 获得容器中上传文件夹所在的物理路径
		//System.out.println(this.getServletConfig().getServletContext().getRealPath("/"));
		//System.out.println(FileUtil.getDatePath());
		String savePath = null;
		if (uploadFilePath != null) {
			savePath = uploadFilePath;
		} else {
			// Error Log
			baseLogger.error("FlashUpload() Upload file path is not set!");
			savePath = request.getServletPath() + "uploads/";
		}
		baseLogger.info("FlashUpload() Upload file path:" + savePath);
		//System.out.println(savePath);
		File file = new File(savePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		//StringBuilder info = new StringBuilder();
		int infoCount = 0;
		try {
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("UTF-8");
			// 获取多个上传文件
			List fileList  = upload.parseRequest(request);
			// 遍历上传文件写入磁盘
			Iterator it = fileList.iterator();
			while (it.hasNext()) {
				Object obit = it.next();
				if (obit instanceof DiskFileItem) {
					DiskFileItem item = (DiskFileItem) obit;
					System.out.println("item.getFieldName()="+item.getFieldName());

					// 如果item是文件上传表单域
					// 获得文件名及路径
					String fileName = item.getName();
					if (fileName != null) {
						if(infoCount == 0){
							result.put("fileName", fileName);
						}else{
							result.put(",fileName", fileName);
						}
						infoCount ++;
						
						firstFileName = item.getName().substring(
								item.getName().lastIndexOf("\\") + 1);
						// Whether the file already uploaded
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("fileName", firstFileName);
						paramMap.put("accurate", "true");
						int count = 0;// templateService.selectAllCount(paramMap); TODO:
						baseLogger.trace("doPost() uploaded files count=" + count);
						if (count > 0) {
							baseLogger.warn("doPost() <== File aleady upload:" + count);
							result.put(Constant._RESULT_, Constant._FAILED_);
							result.put(Constant._REASON_, "该文件已上传：" + firstFileName);
							return result;
						}

						System.out.println("fileName="+fileName);
						String formatName = firstFileName
								.substring(firstFileName.lastIndexOf("."));// 获取文件后缀名
						fileRealPath = savePath + firstFileName;// 文件存放真实地址
						File realFile = new File(fileRealPath);
						realFile.createNewFile();//生成文件  
						BufferedInputStream in = new BufferedInputStream(item.getInputStream());// 获得文件输入流
						BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(realFile));// 获得文件输出流
						Streams.copy(in, outStream, true);// 开始把文件写到你指定的上传文件夹
				
						// 上传成功，则插入数据库
						if (new File(fileRealPath).exists()) {
							System.out.println(fileRealPath);
							result.put(Constant._PATH_, fileRealPath);
							result.put(Constant._RESULT_, Constant._SUCCESS_);
							return result;
						} else {
							result.put(Constant._RESULT_, Constant._FAILED_);
							result.put(Constant._REASON_, "File not found:" + fileRealPath);
							return result;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			baseLogger.error("FlashUpload() <== No upload file.");
			result.put(Constant._RESULT_, Constant._FAILED_);
			result.put(Constant._REASON_, "Exception:" + ex.getMessage());
		}

		baseLogger.trace("FlashUpload() ==>");
		return result;
	}

}
