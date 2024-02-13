package com.as.controller.files.pdf;

import com.as.controller.base.BaseController;
import com.as.util.DateUtil;
import com.as.util.FastDFSHelper;
import org.csource.fastdfs.FileInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Controller
@RequestMapping(value="/files/pdf")
public class PdfPlayerController {
	
	private static final int DEFAULT_BUFFER_SIZE = 512*1024; // ..bytes = 20KB.
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";
    private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.

	/*
	 * pdf播放页面
	 */
	@RequestMapping(value="/page")
	public ModelAndView page(String fid, String type) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/template/files/pdf/pdf_player");
		modelAndView.addObject("fid", fid);
		modelAndView.addObject("type", type);	

		return modelAndView;
	}
	
	/*
	 * 获取文件流
	 */
	@RequestMapping(value = "getFileBytes",  method = {RequestMethod.GET,RequestMethod.HEAD} )
	@ResponseBody
	public void getFileBytes(HttpServletRequest request, HttpServletResponse response,
			String fid) {
		BaseController.logger.debug("getFileBytes() ==>");

		// fid = "group1/M00/00/00/ZcmPE1lhrVGABdOTAClWfHSA1_w519.mp4";

		String fileName;

		boolean content = ("HEAD".equalsIgnoreCase(request.getMethod())) ? false : true;
		System.out.println("Method type:" + request.getMethod());

		try {
			FileInfo fileInfo = FastDFSHelper.getFileMetaData(fid);
			// 1. Check if file actually exists in filesystem.
			if (fid == null || fid.length() == 0 || fileInfo == null) {
				// Do your thing if the file appears to be non-existing.
	            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
	            return;
			}

			// Prepare some variables. The ETag is an unique identifier of the file.
			fileName = fileInfo.getSourceIpAddr();
	        long length = fileInfo.getFileSize();
	        long lastModified = fileInfo.getCreateTimestamp().getTime();
	        String eTag = fileName + "_" + length + "_" + lastModified;
	        long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;
	   
	        // Prepare some variables. The full Range represents the complete file.
			HttpRequestRange full = new HttpRequestRange(0, length - 1, length);
			List<HttpRequestRange> ranges = new ArrayList<HttpRequestRange>();
			// Validate and process Range and If-Range headers.
			String range = request.getHeader("Range");
			System.out.println("request header->range:" + range);

			String contentType = request.getSession().getServletContext().getMimeType(fileName);
			boolean acceptsGzip = false;
			String disposition = "inline";

	        // If content type is text, then determine whether GZIP content encoding is supported by
	        // the browser and expand content type with the one and right character encoding.
	        if (contentType.startsWith("text")) {
	            String acceptEncoding = request.getHeader("Accept-Encoding");
	            acceptsGzip = acceptEncoding != null && HttpRequestRange.accepts(acceptEncoding, "gzip");
	            contentType += ";charset=UTF-8";
	        } 
	    	// Initialize response.
	    	response.reset();
	    	response.setBufferSize(DEFAULT_BUFFER_SIZE);
	    	// disposition = attachment?
	    	response.setHeader("Content-Disposition", disposition + "; filename=\"" + eTag + "\"");
	    	response.setHeader("Accept-Ranges", "bytes");
	    	response.setHeader("ETag", eTag);
	    	response.setDateHeader("Last-Modified", lastModified);
	        response.setDateHeader("Expires", expires);
            OutputStream output = response.getOutputStream();
	        try {
	            if (ranges.isEmpty() || ranges.get(0) == full) {
	            	response.setContentType(contentType);
	            	if (content) {
	                    if (acceptsGzip) {
	                        // The browser accepts GZIP, so GZIP the content.
	                        response.setHeader("Content-Encoding", "gzip");
	                        output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
	                    } else {
	                        // Content length is not directly predictable in case of GZIP.
	                        // So only add it if there is no means of GZIP, else browser will hang.
	                        response.setHeader("Content-Length", String.valueOf(full.length));
	                    }
		                //response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
		                //response.setHeader("Content-Length", String.valueOf(full.length));
		                HttpRequestRange.copy(fid, output, full.start, full.length);
	            	}
	            } else if (ranges.size() == 1) {

	                // Return single part of file.
	            	HttpRequestRange r = ranges.get(0);
	            	response.setContentType(contentType);
	                response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
	                response.setHeader("Content-Length", String.valueOf(r.length));
	                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
	                if (content) {
		                // Copy single part range.
		                HttpRequestRange.copy(fid, output, r.start, r.length);
	                }

	            } else {
	            	 
	            }
	        } finally {
	        	// Gently close streams.
	            output.close();
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BaseController.logger.debug("getFileBytes() <==");
	}
	private static class HttpRequestRange {

		   long start;
	       long end;
	       long length;
	       long total;

	       /**
	        * Construct a byte range.
	        * @param start Start of the byte range.
	        * @param end End of the byte range.
	        * @param total Total length of the byte source.
	        */
	       public HttpRequestRange(long start, long end, long total) {
	           this.start = start;
	           this.end = end;
	           this.length = end - start + 1;
	           this.total = total;
	       }

	       public static long sublong(String value, int beginIndex, int endIndex) {
	           String substring = value.substring(beginIndex, endIndex);
	           return (substring.length() > 0) ? Long.parseLong(substring) : -1;
	       }

	       private static void copy(String fid, OutputStream output, long start, long length) throws Exception {
	    	   System.out.println("copy(" + start + ",length=" + length + ")");
	    	   Date beginDate = DateUtil.getCurDate();
	    	   System.out.println("Begin download ==>" + beginDate);

		       long downloaded = 0;
		       long myStart = start;
		       long step = 0;

	           while(downloaded < length) {
	        	   long rem = (length - downloaded);

	               if (rem > DEFAULT_BUFFER_SIZE) {
	            	   step = DEFAULT_BUFFER_SIZE;
	               } else {
	            	   step = rem;
	               }
	    	       byte[] buffer = FastDFSHelper.fastDFSDownload(fid, myStart, step);
	    	       if (buffer == null) break;
	               output.write(buffer, 0, (int) step);
	               buffer = null;
	               downloaded += step;
	               myStart += step;
	               
	           }
		        Date endDate = DateUtil.getCurDate();
		        long last = (endDate.getTime() - beginDate.getTime())/1000;
		        System.out.println("End download <==" + endDate + " last:" + last);
		        if (last > 0) {
		        	System.out.println("K/Second=" + length/1024/last);
		        }
	       }
	      

	       private static void copyFromLocal(OutputStream output, long start, long length)
	    	        throws IOException
		    {
	       
	    	   RandomAccessFile input = new RandomAccessFile("/home/as/oceans.mp4", "r");
		        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		        int read;

		        Date beginDate = DateUtil.getCurDate();
		        System.out.println("Begin download ==>" + beginDate);
		        if (input.length() == length) {
		            // Write full range.
		            while ((read = input.read(buffer)) > 0) {
		                output.write(buffer, 0, read);
		            }
		        } else {
		            // Write partial range.
		            input.seek(start);
		            long toRead = length;

		            while ((read = input.read(buffer)) > 0) {
		                if ((toRead -= read) > 0) {
		                    output.write(buffer, 0, read);
		                } else {
		                    output.write(buffer, 0, (int) toRead + read);
		                    break;
		                }
		            }
		        }
		        Date endDate = DateUtil.getCurDate();
		        System.out.println("End download <==" + endDate + " last:" + (endDate.getTime() - beginDate.getTime())/1000);
		        System.out.println("K/Second=" + length/1024/((endDate.getTime() - beginDate.getTime())/1000));

		    }
	       /**
	        * Returns true if the given accept header accepts the given value.
	        * @param acceptHeader The accept header.
	        * @param toAccept The value to be accepted.
	        * @return True if the given accept header accepts the given value.
	        */
	       private static boolean accepts(String acceptHeader, String toAccept) {
	           String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
	           Arrays.sort(acceptValues);
	           return Arrays.binarySearch(acceptValues, toAccept) > -1
	               || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
	               || Arrays.binarySearch(acceptValues, "*/*") > -1;
	       }

	       /**
	        * Returns true if the given match header matches the given value.
	        * @param matchHeader The match header.
	        * @param toMatch The value to be matched.
	        * @return True if the given match header matches the given value.
	        */
	       private static boolean matches(String matchHeader, String toMatch) {
	           String[] matchValues = matchHeader.split("\\s*,\\s*");
	           Arrays.sort(matchValues);
	           return Arrays.binarySearch(matchValues, toMatch) > -1
	               || Arrays.binarySearch(matchValues, "*") > -1;
	       }
		}
	
}
