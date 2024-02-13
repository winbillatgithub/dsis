package com.as.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.test.DownloadFileWriter;

/**
 * FastDFS文件上传下载工具类
 * @author admin
 * @date 2013-11-25 下午04:09:04
 */
public class FastDFSHelper {
	
	private static Logger logger = Logger.getLogger(FastDFSHelper.class);
    private static TrackerServer trackerServer = null;
    public static TrackerServer getTrackerServer() {
    	if (trackerServer == null) {
	    	TrackerClient tracker = new TrackerClient();
	        try {
				trackerServer = tracker.getConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				trackerServer = null;
			}
    	}
    	return trackerServer;
    }
	/**
	 * 文件路径上传
	 * @param local_filename   D:/workspace/Demo/src/ps3223b.jpg
	 * @return                 group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg
	 */
	public static String fastDFSUpload(String local_filename){ 
		if(StringUtil.strIsNotEmpty(local_filename)) {
			TrackerServer trackerServer = null;
			try {
				//建立链接
				TrackerClient tracker = new TrackerClient();
				trackerServer = tracker.getConnection();
		        StorageServer storageServer = null;
		        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
		        
		        //设置元信息
		        NameValuePair[] metaList = new NameValuePair[1];
		        metaList[0] = new NameValuePair("fileName", local_filename);
		        
		        //上传文件
		        return client.upload_file1(local_filename, null, metaList);
			} catch (Exception e) {
				logger.error("文件上传失败：", e);
				throw new RuntimeException("文件上传失败", e);
			} finally {
				try {					
					if(null != trackerServer) {
						trackerServer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 字节数组上传 
	 * @param bytes       字节数组
	 * @param fileExtName 文件类型
	 * @return          group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg
	 */
	public static String fastDFSUpload(byte[] bytes, String fileExtName){ 
		if(null != bytes && bytes.length > 0) {
			TrackerServer trackerServer = null;
			try {
				//建立链接
				TrackerClient tracker = new TrackerClient();
				trackerServer = tracker.getConnection();
		        StorageServer storageServer = null;
		        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
		        
		        //设置元信息
		        NameValuePair[] metaList = new NameValuePair[2];
		        metaList[0] = new NameValuePair("fileName", System.currentTimeMillis()+"."+fileExtName);
		        metaList[1] = new NameValuePair("fileExtName", fileExtName);
		        
		        //上传文件
		        return client.upload_file1(bytes, fileExtName, metaList);
			} catch (Exception e) {
				logger.error("文件上传失败：", e);
				throw new RuntimeException("文件上传失败", e);
			} finally {
				try {					
					if(null != trackerServer) {
						trackerServer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 字节数组上传 
	 * @param bytes         InputStream 流
	 * @param uploadFileName   文件名称
	 * @param fileLength       文件长度
	 * @return                 group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg
	 */
	public static String fastDFSUpload(byte[] bytes, String uploadFileName, long fileLength){ 
		if(null != bytes && bytes.length > 0 && StringUtil.strIsNotEmpty(uploadFileName) && fileLength > 0) {
			TrackerServer trackerServer = null;
			String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1); 
			try {
				//建立链接
				TrackerClient tracker = new TrackerClient();
				trackerServer = tracker.getConnection();
		        StorageServer storageServer = null;
		        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
		        
		        //设置元信息
		        NameValuePair[] metaList = new NameValuePair[3];
		        metaList[0] = new NameValuePair("fileName", uploadFileName);
		        metaList[1] = new NameValuePair("fileExtName", fileExtName); 
		        metaList[2] = new NameValuePair("fileLength", String.valueOf(fileLength));
		        
		        //上传文件
		        return client.upload_file1(bytes, fileExtName, metaList); 
			} catch (Exception e) {
				logger.error("文件上传失败：", e);
				throw new RuntimeException("文件上传失败", e);
			} finally {
				try {		
					if(null != trackerServer) {
						trackerServer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 流上传 
	 * @param inStream         InputStream 流
	 * @param uploadFileName   文件名称
	 * @param fileLength       文件长度
	 * @return                 group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg
	 */
	public static String fastDFSUpload(InputStream inStream, String uploadFileName, long fileLength, String localPath){ 
		if(null != inStream && StringUtil.strIsNotEmpty(uploadFileName) && fileLength > 0) {
			TrackerServer trackerServer = null;
			String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1); 
			try {
				//建立链接
				TrackerClient tracker = new TrackerClient();
				trackerServer = tracker.getConnection();
		        StorageServer storageServer = null;
		        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
		        
		        //设置元信息
		        NameValuePair[] metaList = new NameValuePair[3];
		        metaList[0] = new NameValuePair("fileName", uploadFileName);
		        metaList[1] = new NameValuePair("fileExtName", fileExtName); 
		        metaList[2] = new NameValuePair("fileLength", String.valueOf(fileLength));
		        
		        //上传文件
		        return client.upload_file1(getFileBytes(inStream, fileLength, localPath), fileExtName, metaList);
			} catch (Exception e) {
				logger.error("文件上传失败：", e);
				throw new RuntimeException("文件上传失败", e);
			} finally {
				try {		
					if(null != trackerServer) {
						trackerServer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取字节数组
	 * @param inStream
	 * @param fileLength
	 * @return
	 */
	public static byte[] getFileBytes(InputStream inStream, long fileLength, String localPath) {
		ByteArrayOutputStream baos = null;
		DataOutputStream out = null;
		try {
			if (localPath != null && !"".equals(localPath)) {
				out = new DataOutputStream(
		                new BufferedOutputStream(
		                new FileOutputStream(localPath)));
			}
			baos = new ByteArrayOutputStream(1024 * 1024);//1M
			byte[] bytes = new byte[(int) fileLength];
			byte[] b = new byte[1024 * 1024];
	        int len = 0;
	        int count = 0;
	        while ((len = inStream.read(b)) != -1) {
	        	for (int i = 0; i < len; ++i) {
	        		bytes[count + i] = b[i];
	            }
	            count += len;
	            // write to local file, for convert ppts,docs to PDF format
	            if (out != null && len != -1) {
	    			out.write(b, 0, len);
	            }
	        }
	        return bytes;
		} catch (Exception e) {
			logger.error("获取文件字节数组失败：", e);
			throw new RuntimeException("文件上传失败");
		} finally {
			try {				
				if (out != null) {
					out.close();
				}
				if(null != baos) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 文件下载
	 * @param imageUrl        group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg
	 * @param downloadFile  c:/test.jpg
	 */
	public static void fastDFSDownload(String imageUrl, String downloadFile){ 
		if(StringUtil.strIsNotEmpty(imageUrl)) {
			TrackerServer trackerServer = null;
			try {
				//建立链接
				TrackerClient tracker = new TrackerClient();
				trackerServer = tracker.getConnection();
		        StorageServer storageServer = null;
		        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
				
				client.download_file(imageUrl.substring(0, imageUrl.indexOf("/")), imageUrl.substring(imageUrl.indexOf("/")+1), 0, 0, new DownloadFileWriter(downloadFile));
			} catch (Exception e) {
				logger.error("文件下载失败：", e);
				throw new RuntimeException("文件下载失败", e);
			} finally {
				try {					
					if(null != trackerServer) {
						trackerServer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
    /**
     * 获取文件流
     * @param fileId         fileid on storage server
     * @param fileOffset     the start offset of the file
     * @param downloadBytes  download bytes, 0 for remain bytes from offset
     * @return
     * @throws Exception
     */
    public static byte[] fastDFSDownload(String fileId, long fileOffset, long downloadBytes) throws Exception{  
         System.out.println("begin download=======================>time:[" + new Date() + "]fileId=[" + fileId + "]offset=[" + fileOffset + "]size=[" + downloadBytes);
         TrackerClient tracker = new TrackerClient();
         TrackerServer trackerServer = tracker.getConnection();
         StorageServer storageServer = null;

         StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
         String groupName = fileId.substring(0, fileId.indexOf("/"));
         String filepath = fileId.substring(fileId.indexOf("/")+1);
         
         byte[] b = storageClient.download_file(groupName, filepath, fileOffset, downloadBytes);

         trackerServer.close();
         storageClient = null;
         tracker = null;

         System.out.println("begin download<=======================>time:" + new Date() );

         return b;
    }
    /**
     * 文件是否存在
     * @param fileId         fileid on storage server
     * @return
     * @throws Exception
     */
    public static FileInfo getFileMetaData(String fileId) {  
         System.out.println("下载文件=======================");
         FileInfo fi = null;
         try {
	         TrackerClient tracker = new TrackerClient();
	         TrackerServer trackerServer = tracker.getConnection();
	         StorageServer storageServer = null;
	
	         StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
	         String groupName = fileId.substring(0, fileId.indexOf("/"));
	         String filepath = fileId.substring(fileId.indexOf("/")+1);
        	 fi = storageClient.get_file_info1(fileId);
        	 
        	 // fileName, fileLength, fileExtName, 
        	 NameValuePair[] nvp = storageClient.get_metadata1(fileId);
        	 if (nvp != null) {
        		 for(int i = 0; i < nvp.length; i ++) {
        			 if ("fileName".equals(nvp[i].getName())) {
        				 fi.setSourceIpAddr(nvp[i].getValue());
        				 break;
        			 }
        		 }
        	 }        	 
         } catch (Exception e) {
        	 e.printStackTrace();
         }
         return fi;
    }
    /**
     * 删除文件
     * @param fileId         fileid on storage server
     * @throws Exception
     */
    public static void deleteFile(String fileId) throws Exception{  
        System.out.println("删除文件=======================");
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
        String groupName = fileId.substring(0, fileId.indexOf("/"));
        String filepath = fileId.substring(fileId.indexOf("/")+1);
        int i = storageClient.delete_file(groupName, filepath);
        System.out.println(i == 0 ? "删除成功" : "删除失败:" + i);
    }

	public static void main(String[] args) {
		String fileName = "/Users/winbill/Downloads/korea.jpg";
		String fid = fastDFSUpload(fileName);
		String url = "http://192.168.0.215/group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg";

		if(url.startsWith("http://")){
			url = url.substring(7);
			System.out.println("--"+url);
			System.out.println("--"+url.indexOf("/"));
			url = url.substring(url.indexOf("/")+1);
			System.out.println("-000-"+url);
		}

		String fileId = "http://192.168.0.215/group1/M00/00/3C/wKgA11KSsC2AJ6Y8AAQukBrzu6E160.jpg";
		String downloadFile = "c:/test.jpg";

		fastDFSDownload(fileId, downloadFile);

		String s = "group1/M00/00/3C/wKgA2FKPEkyALCAgAAQukBrzu6E604.jpg";

		System.out.println(s.substring(0, s.indexOf("/")));
		System.out.println(s.substring(s.indexOf("/")+1));

		String ss = "e:/a.jpg";
		System.out.println(fastDFSUpload(ss));
	}
	
}
