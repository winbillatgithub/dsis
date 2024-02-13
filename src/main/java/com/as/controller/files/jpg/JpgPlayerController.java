package com.as.controller.files.jpg;

import com.as.util.DateUtil;
import com.as.util.FastDFSHelper;
import org.csource.fastdfs.FileInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping(value="/files/jpg")
public class JpgPlayerController {
	
	private static final int DEFAULT_BUFFER_SIZE = 512*1024; // ..bytes = 20KB.
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";
    private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.

	/*
	 * pdf播放页面
	 */
	@RequestMapping(value="/page")
	public ModelAndView page(HttpServletRequest request, HttpServletResponse response,String fid, String type) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/template/files/img/jpg_player");

		try {
			FileInfo fileInfo = FastDFSHelper.getFileMetaData(fid);
			// 1. Check if file actually exists in filesystem.
			if (fid == null || fid.length() == 0 || fileInfo == null) {
				return modelAndView;
			}
			boolean content = ("HEAD".equalsIgnoreCase(request.getMethod())) ? false : true;
			// Prepare some variables. The ETag is an unique identifier of the file.
			String fileName = fileInfo.getSourceIpAddr();
			long length = fileInfo.getFileSize();
			long lastModified = fileInfo.getCreateTimestamp().getTime();
			long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;
			try {
				JpgPlayerController.HttpRequestRange full = new JpgPlayerController.HttpRequestRange(0, length - 1, length);
				byte[] buffer = FastDFSHelper.fastDFSDownload(fid,  full.start, full.length);
				BASE64Encoder encoder = new BASE64Encoder();
				String png_base64 = encoder.encodeBuffer(buffer).trim();//转换成base64串
				png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
				modelAndView.addObject("base64", png_base64);
			} finally {
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return modelAndView;
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
