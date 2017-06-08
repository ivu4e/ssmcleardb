package com.weixiao.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageUtils {

	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author: 
	 * @CreateTime: 
	 * @param imgStr base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	*/
	public static boolean generateImage(String imgStr, String path) {
		//"data:image/jpeg;base64,"
		if (imgStr == null){
			return false;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// 解密
			byte[] b = decoder.decodeBuffer(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			
			// 获得文件对象  
            File f = new File(path);
            // 如果路径不存在,则创建  
            if (!f.getParentFile().exists()) {  
                f.getParentFile().mkdirs();  
            }
            
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author:
	 * @CreateTime:
	 * @return
	 */
	public static String getImageStr(String imgFile) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
	
	public static String getRelativePath(){
		return getRelativePath(null);
	}

	public static String getRelativePath(String filePath){
		Calendar now = Calendar.getInstance();
		String vode = UUID.randomUUID().toString().replaceAll("-", "");
		String extn = ".jpg";
		if (filePath != null){
			int idx = filePath.lastIndexOf(".");
			if (idx != -1){
				extn = filePath.substring(idx);
			}
		}
		String path = ("/static/image/" + now.get(Calendar.YEAR) + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH) + "/" + vode + extn);
		return path;
	}
	
	public static String getSavePath(HttpServletRequest request, String relativePath){
		StringBuffer buf = new StringBuffer();
		buf.append(request.getSession().getServletContext().getRealPath("."));
		buf.append(relativePath);
		return buf.toString();
	}
	// String strImg = getImageStr("F:/86619-106.jpg");
	// System.out.println(strImg);
	// generateImage(strImg, "F:/86619-107.jpg");
	public static void main(String[] args) {
		String vode = UUID.randomUUID().toString();
		for (int i = 0; i < 100; i++){
			vode = getRelativePath("abcd.jpg");
			System.out.println(vode);
		}
	}
}
