package com.as.util;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mortennobel.imagescaling.ResampleOp;

/**
 * 文件工具类
 * @author 熊焱
 * @date 2013-1-28 上午10:35:42
 * 
 */
public class FileHelper {
	
	protected static Log log = LogFactory.getLog(FileHelper.class);
	
	/**
	 * 缓存大小
	 */
	public static final int BUFFER_SIZE = 16 * 1024;
	
	/**
	 * 根目录
	 */
	public static final String FILE_DIR = "files";
	
	/**
	 * 日期目录
	 * @return
	 */
	public static String getDatePath() {
		String now = DateUtil.today(DateUtil.DEFAULT_DATE_FORMAT);
		return File.separator + now.split("-")[0] + File.separator + now.split("-")[1] + File.separator + now.split("-")[2] + File.separator;
	}
	
	/**
	 * 返回文件在服务器上的路径
	 * @param request
	 * @return
	 */
	public static String getFilePath(HttpServletRequest request, String dir) {
		//服务器文件地址
		String basePath = request.getSession().getServletContext().getRealPath(StringUtil.strIsNotEmpty(dir) ? dir : FILE_DIR);
		//日期文件夹
		String datePath = getDatePath();
		// 判断文件是否存在
		File filePath = new File(basePath + datePath);
		//不存在创建
		if (!filePath.exists()) {   
			filePath.mkdirs();   
		} 
		return filePath.getPath();
	}
	
	/**
	 * spring3 MVC 上传文件
	 * @param request
	 * @param response
	 * @param upload
	 * @return
	 */
	public static String uploadFile(HttpServletRequest request, MultipartFile upload) {
		InputStream is = null;
		OutputStream os = null;
		try {
			//文件目录
			String path = getFilePath(request, null);
			//文件名称
			String fileName = getRandName(upload.getOriginalFilename());
			
			//第一种：拷贝图片
            //FileCopyUtils.copy(upload.getBytes(), new File(filePath, fileName)); 
            
            //第二种：以流的方式写入
            is = new BufferedInputStream(upload.getInputStream(), BUFFER_SIZE);
            // 建立一个上传文件的输出流
            os = new FileOutputStream(path + File.separator + fileName);
			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((len = is.read(buffer)) > 0) {
				// 将文件写入服务器
				os.write(buffer, 0, len);
			}
            return path + File.separator + fileName;
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("图片上传失败：", e);
        	return null;
        } finally {
        	if(null != os) {
        		try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(null != is) {
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}
	
	/**
	 * 普通的上传文件
	 * @param request
	 * @param srcFile
	 * @param fileName
	 * @return
	 */
	public static String uploadFile(HttpServletRequest request, File srcFile, String fileName) {
		InputStream is = null;
		OutputStream os = null;
		try {
			//文件目录
			String path = getFilePath(request, null);
			//以流的方式写入
            is = new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE);
            // 建立一个上传文件的输出流
			os = new FileOutputStream(path + File.separator + fileName);
			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((len = is.read(buffer)) > 0) {
				// 将文件写入服务器
				os.write(buffer, 0, len);
			}
			
			return path + File.separator + fileName;
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("图片上传失败：", e);
        	return null;
        } finally {
        	if(null != os) {
        		try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(null != is) {
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}

	/**
	 * 等比例缩放图片
	 * @param srcFile 原图片
	 * @param width 宽
	 * @param height 高
	 * @return 服务器上图片的路径
	 */
	public static String scaleImage(HttpServletRequest request, File srcFile, int width, int height) {
		try {
			//文件目录
			String path = getFilePath(request, null);
			//获取文件名
			String fileName = getRandName(srcFile.getName());
			//获取文件后缀
			String fileType = getFileType(srcFile.getName());
			
			BufferedImage inputBufImage = ImageIO.read(srcFile);
			ResampleOp resampleOp = new ResampleOp(width, height);
			BufferedImage rescaledTomato = resampleOp.filter(inputBufImage, null);
			ImageIO.write(rescaledTomato, fileType, new File(path + File.separator + fileName));
			
			return path + File.separator + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("图片上传失败：", e);
        	return null;
		}
	}
	
	/**
	 * 文件拷贝
	 * @param src
	 * @param dst
	 */
	public static void copyFile(File src, File dst) {
		InputStream is = null;
		OutputStream out = null;
		try {
			is = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = is.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("图片拷贝失败：", e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println("111:"+getFileName("111.jpg"));
	}
	
	
	/**
	 * 获取文件名后缀
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName){
		if(null != fileName && !"".equals(fileName)) {
			int index = 0;
			if((index = fileName.lastIndexOf(".")) != -1) {
				return fileName.substring(index + 1).toLowerCase();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public static int i = 0;
	
	/**
	 * 获取随机文件名
	 * @param fileName
	 * @return
	 */
	public static String getRandName(String fileName){
		String type = "jpg";
		if(StringUtil.strIsNotEmpty(fileName)) {
			type = getFileType(fileName);
		}
		//确保图片名字的唯一性
		if(i > 10000){
			i = 0;
		}
		return System.currentTimeMillis()+(i++)+"."+type;
	}

	/**
	 * 获取文件名
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName) {
		if(null != fileName && !"".equals(fileName)) {
			int index = 0;
			if((index = fileName.lastIndexOf(".")) != -1) {
				return fileName.substring(0, index);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 获取文件大小
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#0.0");
		if (size < 1024) {
			return df.format((double) size) + "B";
		} else if (size < 1048576) {
			return df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			return df.format((double) size / 1048576) + "M";
		} else {
			return df.format((double) size / 1073741824) + "G";
		}
	}
	
	/**
	 * 获取图片的像素
	 * @param file
	 * @return
	 */
	public static String getImageWH(File file) {
		try {
			Image src = ImageIO.read(file);
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			return w + " x " + h;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取图片的像素
	 * @param is
	 * @return
	 */
	public static String getImageWH(InputStream is) {
		try {
			Image src = ImageIO.read(is);
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			return w + " x " + h;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void imgUpload(  
		    MultipartHttpServletRequest multipartRequest,  
		    HttpServletResponse response) throws Exception {  
		    response.setContentType("text/html;charset=UTF-8");  
		    Map<String, Object> result = new HashMap<String, Object>();  
		               //获取多个file  
		    for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {  
		        String key = (String) it.next();  
		        MultipartFile imgFile = multipartRequest.getFile(key);  
		        if (imgFile.getOriginalFilename().length() > 0) {  
		            String fileName = imgFile.getOriginalFilename();  
		                               //改成自己的对象哦！  
		            Object obj = new Object();  
		            //Constant.UPLOAD_GOODIMG_URL 是一个配置文件路径  
		            try {  
		                String uploadFileUrl = multipartRequest.getSession().getServletContext().getRealPath("/files");
		                System.out.println(uploadFileUrl);
		                File _apkFile = saveFileFromInputStream(imgFile.getInputStream(), uploadFileUrl, fileName);  
		                //String file_path=_apkFile.getPath();
		                String file_name=_apkFile.getName();
		                System.out.println(file_name.length());
		                if (_apkFile.exists()) {  
		                    FileInputStream fis = new FileInputStream(_apkFile);  
		                } else  
		                    throw new FileNotFoundException("apk write failed:" + fileName);  
		                List list = new ArrayList();  
		                //将obj文件对象添加到list  
		                list.add(obj);  
		                result.put("success", true); 
		                result.put("file_path", file_name);
		            } catch (Exception e) {  
		                result.put("success", false);  
		                e.printStackTrace();  
		            }  
		        }  
		    }  
		    String json = new Gson().toJson(result,  
		            new TypeToken<Map<String, Object>>() {  
		            }.getType());  
		    response.getWriter().print(json);  
		}  
		  
		       //保存文件  
		private static File saveFileFromInputStream(InputStream stream, String path,  
		        String filename) throws IOException {  
		    File file = new File(path + "/" + filename);  
		    FileOutputStream fs = new FileOutputStream(file);  
		    byte[] buffer = new byte[1024 * 1024];  
		    int bytesum = 0;  
		    int byteread = 0;  
		    while ((byteread = stream.read(buffer)) != -1) {  
		        bytesum += byteread;  
		        fs.write(buffer, 0, byteread);  
		        fs.flush();  
		    }  
		    fs.close();  
		    stream.close();  
		    return file;  
		}  
}
