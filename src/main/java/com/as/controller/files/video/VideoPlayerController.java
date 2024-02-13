package com.as.controller.files.video;

import com.as.controller.base.BaseController;
import com.as.util.DateUtil;
import com.as.util.FastDFSHelper;
import org.csource.fastdfs.FileInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Controller
@RequestMapping(value="/files/video")
public class VideoPlayerController {
	
	private static final int DEFAULT_BUFFER_SIZE = 512*1024; // ..bytes = 20KB.
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";
    private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.

	/*
	 * 视频播放页面
	 */
	@RequestMapping(value="/page")
	public ModelAndView page(String fid, String type) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/template/files/video/video_player");
		modelAndView.addObject("fid", fid);
		modelAndView.addObject("type", type);

		return modelAndView;
	}

	/*
	 * 文件/视频下载
	 */
	@RequestMapping(value = "download",  method = {RequestMethod.GET,RequestMethod.HEAD} )
	@ResponseBody
	public void download(HttpServletRequest request, HttpServletResponse response,
			String fid, String type) {
		BaseController.logger.debug("download() ==>");
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
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
			String fileName = fileInfo.getSourceIpAddr();
	        long length = fileInfo.getFileSize();
	        long lastModified = fileInfo.getCreateTimestamp().getTime();
	        String eTag = fileName + "_" + length + "_" + lastModified;
	        long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;
	        
			outs = response.getOutputStream();
			bouts = new BufferedOutputStream(outs);
			response.setContentType("application/x-download");
	        response.setHeader(
	                "Content-disposition",
	                "attachment;filename="
	                        + URLEncoder.encode(fileName, "UTF-8"));
	        
            // Return single part of file.
        	// HttpRequestRange r = new HttpRequestRange(0, 4096, length);
            // Copy single part range.
	        long bytesRead = 0;
	        long bytesLeft = length;
	        long offset = 0;
        	while (bytesLeft > 0) {
        		bytesRead = (bytesLeft > 1048576) ? 1048576 : bytesLeft;
        		HttpRequestRange.copy(fid, bouts, offset, bytesRead);
        		offset += bytesRead;
        		bytesLeft -= bytesRead;
        	}

            bouts.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != outs) {
				try {
					outs.close();
				} catch (Exception e) {}
            }
            if (null != bouts) {
            	try {
            		bouts.close();
            	} catch (Exception e) {}
            }
		}
		BaseController.logger.debug("download() <==");
	}

	/*
	 * 获取视频流
	 */
	@RequestMapping(value = "getFileBytes",  method = {RequestMethod.GET,RequestMethod.HEAD} )
	@ResponseBody
	public void getFileBytes(HttpServletRequest request, HttpServletResponse response,
			String fid) {
		Date beginDate = DateUtil.getCurDate();
		BaseController.logger.debug("getFileBytes() ==>" + beginDate);
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
	     
	        // Validate request headers for caching ---------------------------------------------------

	        // If-None-Match header should contain "*" or ETag. If so, then return 304.
	        String ifNoneMatch = request.getHeader("If-None-Match");
	        if (ifNoneMatch != null && HttpRequestRange.matches(ifNoneMatch, eTag)) {
	            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
	            response.setHeader("ETag", eTag); // Required in 304.
	            response.setDateHeader("Expires", expires); // Postpone cache with 1 week.
	            return;
	        }

	        // If-Modified-Since header should be greater than LastModified. If so, then return 304.
	        // This header is ignored if any If-None-Match header is specified.
	        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
	        if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
	            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
	            response.setHeader("ETag", eTag); // Required in 304.
	            response.setDateHeader("Expires", expires); // Postpone cache with 1 week.
	            return;
	        }
	        
	        // Validate request headers for resume ----------------------------------------------------

	        // If-Match header should contain "*" or ETag. If not, then return 412.
	        String ifMatch = request.getHeader("If-Match");
	        if (ifMatch != null && !HttpRequestRange.matches(ifMatch, eTag)) {
	            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
	            return;
	        }

	        // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
	        long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
	        if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
	            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
	            return;
	        }

	        // Validate and process range -------------------------------------------------------------

	        // Prepare some variables. The full Range represents the complete file.
			HttpRequestRange full = new HttpRequestRange(0, length - 1, length);
			List<HttpRequestRange> ranges = new ArrayList<HttpRequestRange>();
			// Validate and process Range and If-Range headers.
			String range = request.getHeader("Range");
			System.out.println("request header->range:" + range);
			if(null != range){
				 // Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
	            if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
	                response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
	                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
	                return;
	            }
	            // If-Range header should either match ETag or be greater then LastModified. If not,
	            // then return full file.
	            String ifRange = request.getHeader("If-Range");
	            if (ifRange != null && !ifRange.equals(eTag)) {
	                try {
	                    long ifRangeTime = request.getDateHeader("If-Range"); // Throws IAE if invalid.
	                    if (ifRangeTime != -1 && ifRangeTime + 1000 < lastModified) {
	                        ranges.add(full);
	                    }
	                } catch (IllegalArgumentException ignore) {
	                    ranges.add(full);
	                }
	            }
	            
	            // If any valid If-Range header, then process each part of byte range.
	            if (ranges.isEmpty()) {
	                for (String part : range.substring(6).split(",")) {
	                    // Assuming a file with length of 100, the following examples returns bytes at:
	                    // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
	                    long start = HttpRequestRange.sublong(part, 0, part.indexOf("-"));
	                    long end = HttpRequestRange.sublong(part, part.indexOf("-") + 1, part.length());

	                    if (start == -1) {
	                        start = length - end;
	                        end = length - 1;
	                    } else if (end == -1 || end > length - 1) {
	                        end = length - 1;
	                    }

	                    // Check if Range is syntactically valid. If not, then return 416.
	                    if (start > end) {
	                        response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
	                        response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
	                        return;
	                    }

	                    // Add range.                    
	                    ranges.add(new HttpRequestRange(start, end, length));
	                }
	            }
			}
			// Prepare and initialize response --------------------------------------------------------

	        // Get content type by file name and set default GZIP support and content disposition.
			String contentType = request.getSession().getServletContext().getMimeType(fileName);
			boolean acceptsGzip = false;
			String disposition = "inline";
			
			// If content type is unknown, then set the default value.
	        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
	        // To add new content types, add new mime-mapping entry in web.xml.
	        if (contentType == null) {
	            contentType = "video/mp4";
	        }
	        // If content type is text, then determine whether GZIP content encoding is supported by
	        // the browser and expand content type with the one and right character encoding.
	        if (contentType.startsWith("text")) {
	            String acceptEncoding = request.getHeader("Accept-Encoding");
	            acceptsGzip = acceptEncoding != null && HttpRequestRange.accepts(acceptEncoding, "gzip");
	            contentType += ";charset=UTF-8";
	        } 

	        // Else, expect for images, determine content disposition. If content type is supported by
	        // the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
	        else if (!contentType.startsWith("image")) {
	            String accept = request.getHeader("Accept");
	            disposition = accept != null && HttpRequestRange.accepts(accept, contentType) ? "inline" : "attachment";
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

	        // Send requested file (part(s)) to client ------------------------------------------------
	        // Prepare streams.
        	
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
	            	  // Return multiple parts of file.
	                response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
	                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

	                if (content) {
		                // Cast back to ServletOutputStream to get the easy println methods.
		                ServletOutputStream sos = (ServletOutputStream) output;

		                // Copy multi part range.
		                for (HttpRequestRange r : ranges) {
		                    // Add multipart boundary and header fields for every range.
		                    sos.println();
		                    sos.println("--" + MULTIPART_BOUNDARY);
		                    sos.println("Content-Type: " + contentType);
		                    sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);

		                    // Copy single part range of multi part range.
		                    HttpRequestRange.copy(fid, output, r.start, r.length);
		                }
		                // End with multipart boundary.
		                sos.println();
		                sos.println("--" + MULTIPART_BOUNDARY + "--");
	                }
	            }
		        Date endDate = DateUtil.getCurDate();
		        long last = (endDate.getTime() - beginDate.getTime())/1000;
		        System.out.println("End download <==" + endDate + " last:" + last);
		        if (last > 0) {
		        	System.out.println("K/Second=" + length/1024/last);
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
	
	
	/**
	 * 测试获取pdf文件
	 */
	@RequestMapping(value = "getPdfFileBytes",  method = RequestMethod.GET )
	@ResponseBody
	public void getPdfFileBytes(HttpServletRequest request, HttpServletResponse response,
			String fid, Long fileOffset, Long downloadBytes) {
		BaseController.logger.debug("getPdfFileBytes() ==>");
		//获得请求文件名  
        String filename = "a.pdf";
        //设置文件MIME类型  
        response.setContentType(request.getSession().getServletContext().getMimeType(filename));  
        //设置Content-Disposition  
        response.setHeader("Content-Disposition", "attachment;filename="+filename);  
        //获取目标文件的绝对路径  
        String fullFileName = "E:/" + filename;  
        //System.out.println(fullFileName);  
        //读取文件  
        try {
			InputStream in = new FileInputStream(fullFileName);  
			OutputStream out = response.getOutputStream();  
			
			//写文件  
			int b;  
			while((b=in.read())!= -1)  
			{  
			    out.write(b);  
			}  
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		BaseController.logger.debug("getPdfFileBytes() <==");
	}
}
