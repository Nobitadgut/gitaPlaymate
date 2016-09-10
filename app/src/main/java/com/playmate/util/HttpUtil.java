package com.playmate.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class HttpUtil {

    /**
     * Get image from newwork
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public static byte[] getURLByte(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get data from stream
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static void readAsFile(InputStream inSream, File file) throws Exception{
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while( (len = inSream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();
    }

    /**
     * Get image from newwork
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public static InputStream getImageStream(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }

    public static String getHttpData(String url){
        String resultStr;
        resultStr = getHttp(url);
        return resultStr;
    }

    public static String getHttpData(String url, String jsonStr){
        String resultStr = "";
        try {
            resultStr = getHttp(url + "?data=" + URLEncoder.encode(jsonStr, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public static String getHttpData(String url, String jsonStr, String otherParm){
        String resultStr = "";
        try {
            resultStr = getHttp(url + "?data=" + URLEncoder.encode(jsonStr, "UTF-8") + otherParm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    private static String getHttp(String url) {
        String resultStr;
        try {
            //HttpGet连接对象
            HttpGet httpRequest = new HttpGet(url);
            //取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            //请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            //请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //取得返回的字符串
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                httpResponse.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String lineStr;
                while ((lineStr = in.readLine()) != null) {
                    sb.append(lineStr + "\n");
                }
                in.close();
                resultStr = sb.toString();
//                resultStr = EntityUtils.toString(httpResponse.getEntity());
            } else {
                resultStr = "error code:" + httpResponse.getStatusLine().getStatusCode();
            }
        } catch (IOException e) {
            resultStr = e.getMessage();
        }

        return resultStr;
    }

    public static String postHttp(String url, List<NameValuePair> postParameters) {
        String resultStr;
        try {
            HttpPost httpRequest = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();

            //实例化UrlEncodedFormEntity对象
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);

            //使用HttpPost对象来设置UrlEncodedFormEntity的Entity
            httpRequest.setEntity(formEntity);
            HttpResponse httpResponse = httpclient.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //取得返回的字符串
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                httpResponse.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String lineStr;
                while ((lineStr = in.readLine()) != null) {
                    sb.append(lineStr + "\n");
                }
                in.close();

                resultStr = sb.toString();
            } else {
                resultStr = "error code" + httpResponse.getStatusLine().getStatusCode();
            }
        } catch (Exception e) {
            // Do something about exceptions
            resultStr = e.getMessage();

        }
        return resultStr;
    }
}
