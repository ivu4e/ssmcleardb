package com.weixiao.frame.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

public class MainUI {

    public static String http(String url, Map<String, String> params) {

        URL u = null;

        HttpURLConnection con = null;

        //构建请求参数

        StringBuffer sb = new StringBuffer();

        if(params!=null){

            for (Map.Entry<String, String> e : params.entrySet()) {

                sb.append(e.getKey());

                sb.append("=");

                sb.append(e.getValue());

                sb.append("&");

            }
            sb.substring(0, sb.length() - 1);
        }
        System.out.println("send_url:"+url);

        System.out.println("send_data:"+sb.toString());
        
        long time = new Date().getTime();
       
        int x = 1+(int)(Math.random()*50);
        //尝试发送请求

        try {

            u = new URL(url);

            con = (HttpURLConnection)u.openConnection();

            con.setRequestMethod("POST");

            con.setDoOutput(true);

            con.setDoInput(true);

            con.setUseCaches(false);

          
//            App-Key: uwd1c0sxdlx2
//            Nonce: 14314
//            Timestamp: 1408706337
//            Signature: 45beb7cc7307889a8e711219a47b7cf6a5b000e8
//            Content-Type: application/x-www-form-urlencoded
//            Content-Length: 78
            
            con.setRequestProperty("App-Key","25wehl3u2qo7w");
            con.setRequestProperty("Nonce",x+"");
            con.setRequestProperty("Timestamp",time+"");
            con.setRequestProperty("Signature",sha1("d5UAVzOK3Xqy"+x+time));
            System.out.println(sha1("d5UAVzOK3Xqy"+x+time));
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length","78");
            

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(),"UTF-8");

            osw.write(sb.toString());

            osw.flush();

            osw.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (con != null) {

                con.disconnect();
            }
        }
        
        //读取返回内容

        StringBuffer buffer = new StringBuffer();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {

            e.printStackTrace();
            
        }
        
        return buffer.toString();
      

    }
  
    //生成签名
    public static String sha1(String str){
        if (null == str || 0 == str.length()){
            return null;
        }
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
             
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		return null;
    }

}
