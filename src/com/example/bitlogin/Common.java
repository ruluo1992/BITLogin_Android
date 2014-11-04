package com.example.bitlogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Base64;

public class Common {
	
	public static List<String> getSortedUserList(Activity activity){
		SharedPreferences p = activity.getSharedPreferences("user", 0);
		Set<String> set = p.getStringSet("userSet", null);
		if(set == null)
			return new ArrayList<String>();
		String[] users = set.toArray(new String[set.size()]);
		List<String> userList = Arrays.asList(users);
		Collections.sort(userList);
		return userList;
	}
	
	public static boolean connect(String param){
		String[] parts = param.split(":");
		if(parts.length != 2)
			return false;
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost();
		try {
			request.setURI(new URI("http://10.0.0.55/cgi-bin/do_login"));
		} catch (URISyntaxException e) {
			return false;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", parts[0]));
		params.add(new BasicNameValuePair("password", getMD5(parts[1])));
		params.add(new BasicNameValuePair("drop", "0"));
		params.add(new BasicNameValuePair("type", "1"));
		params.add(new BasicNameValuePair("n", "100"));
		UrlEncodedFormEntity formEntity;
		try {
			formEntity = new UrlEncodedFormEntity(
			        params);
			request.setEntity(formEntity);
	        HttpResponse response = client.execute(request);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response
	                .getEntity().getContent()));
	        String line = reader.readLine();
	        reader.close();
	        if(isNum(line))
	        	return true;
	        else 
	        	return false;
		} catch (IOException e) {
			return false;
		}       
	}
	
	public static boolean disConnect(String param){
		String[] parts = param.split(":");
		if(parts.length != 2)
			return false;
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost();
		try {
			request.setURI(new URI("http://10.0.0.55/cgi-bin/force_logout"));
		} catch (URISyntaxException e) {
			return false;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", parts[0]));
		params.add(new BasicNameValuePair("password", parts[1]));
		params.add(new BasicNameValuePair("drop", "0"));
		params.add(new BasicNameValuePair("type", "1"));
		params.add(new BasicNameValuePair("n", "1"));
		UrlEncodedFormEntity formEntity;
		try {
			formEntity = new UrlEncodedFormEntity(
			        params);
			request.setEntity(formEntity);
	        HttpResponse response = client.execute(request);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response
	                .getEntity().getContent()));
	        String line = reader.readLine();
	        return true;
		} catch (IOException e) {
			return false;
		}       
	}
	
	public static String getMD5(String input){
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		digest.reset();
		byte[] b = null;
		try {
			b = input.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
        byte[] hash = digest.digest(b);  
        StringBuffer md5StrBuff = new StringBuffer();  
        
        for (int i = 0; i < hash.length; i++) {              
            if (Integer.toHexString(0xFF & hash[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & hash[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & hash[i]));  
        }  
  
        return md5StrBuff.toString().substring(8,24);
	}
	
	public static boolean isNum(String input){
		for(int i = 0; i < input.length(); i++){
			if(input.charAt(i) >= '0' && input.charAt(i) <= '9')
				continue;
			else 
				return false;
		}
		return true;
	}

}
