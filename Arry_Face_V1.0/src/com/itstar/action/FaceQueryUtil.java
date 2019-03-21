package com.itstar.action;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.itstar.util.DbUtil;
import com.tz.IOPdemo.sysmanage.utils.GetToken;
import com.tz.IOPdemo.sysmanage.utils.HttpUtil;

/**
 * 百度大脑人工智能人脸对比工具类
 * @author arry
 * @version v1.0
 * 
 */
public class FaceQueryUtil {

	/**
	 * 通过摄像头获取脸部的信息和录入信息对比
	 * @author arry
	 * @param imgBase64 图像加密字符
	 * @return boolean 是否是本人
	 * 
	 */ 
	public static boolean getFaceInfo(String imgBase64){
		
		boolean flag = false;
		
		try{
			String sql = "select base64 from user";
			// 从数据库中读取录入的人脸信息
			StringBuffer buffer = new StringBuffer();
			List<Map<String,Object>> queryList = DbUtil.queryForList(sql);
			for(Map<String,Object> map : queryList){
				Object object = "," + map.get("base64");
				buffer.append(object);
			}
			
			// 人脸对比URL
			String matchUrl = "https://aip.baidubce.com/rest/2.0/face/v2/match";
			String params = URLEncoder.encode("images", "UTF-8") + "="
	                + URLEncoder.encode(imgBase64 + buffer.toString(), "UTF-8");
			// 调用百度大脑api
	        String accessToken = new GetToken("rpETmGyQ70ahjfIlWxjBYPtq", "T4uprWNx8tK2EmbpwLi1TLynG7nHzTAo").getToken();
			String result = HttpUtil.post(matchUrl, accessToken, params);
			
			System.out.println(result);
			
			JSONObject fromObject = JSONObject.fromObject(result);
			JSONArray jsonArray = fromObject.getJSONArray("result");
			
			int size = jsonArray.size();
			
			for(int i = 0 ; i < size; i++){
				JSONObject object = (JSONObject)jsonArray.get(i);
				double resultList = object.getDouble("score");
				if(resultList >= 90){
					flag = true;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
			
		return flag;
		
	}
	
	
}
