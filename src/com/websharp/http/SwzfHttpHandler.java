package com.websharp.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.loopj.android.http.RequestParams;
import com.websharp.data.GlobalData;
import com.websharp.entity.EntityUser;
import com.websharputil.code.DescUtil;
import com.websharputil.code.CodeUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.json.JSONUtils;

public class SwzfHttpHandler extends BaseHttpHandler {

	public static final String CLIENT = "android";

	public static int PAGE_SIZE = 20;

	/**
	 * 接口中加密的密钥
	 */
	public final static String ENCRYPT_KEY = "JSCN_STB_API";

	/**
	 * 服务器地址
	 */
	public static final String BASE_URL_COMMON = "http://www.taowu.com.cn";
	public static final String BASE_URL_TEST = "http://taowu.websail.cn";
	public static final String BASE_URL = BASE_URL_COMMON+"/handlers"; 

	/** 
	 * 检查更新
	 */
	// public static final String URL_CHECK_VERSION = "/Api/Apk/Check";

	/**
	 * 登录接口
	 */
	public static final String URL_LOGIN = "/Security/LoginHandler.ashx";

	public static final String URL_GetStudent = "/App/GetStudentHandler.ashx";

	/**
	 * 3、考生确认
	 */
	public static final String URL_Confirm = "/App/ConfirmHandler.ashx";

	/**
	 * 4、身高体重 请求方式：POST 请求参数：confirmNo=考试号（确认以后生成的确认号）&height=180&weight=160
	 * 返回参数：单个考生信息 JsonObject
	 */
	public static final String URL_Health = "/App/HealthHandler.ashx";

	/**
	 * 5、视力检查 请求方式：POST
	 * 请求参数：confirmNo=考试号（确认以后生成的确认号）&leftSight=5.1&rightSight=5.2 返回参数：单个考生信息
	 * JsonObject
	 */
	public static final String URL_Sight = "/App/SightHandler.ashx";

	/**
	 * 6、评分标准 请求方式：POST 请求参数：无
	 * 返回参数：返回普通话(Mandarin)、英语口语(Oracy)、初试(General)、复试的评分标准(Retrial)
	 */
	public static final String URL_QueryScoreType = "/App/QueryScoreTypeHandler.ashx";

	/**
	 * 7.普通话测试 地址： 请求方式： POST 请求参数：confirmNo=考试号（确认以后生成的确认号）&score= 普通话评分标准对应的枚举
	 * 返回参数： 单个考生信息 JsonObject
	 **/
	public static final String URL_MandarinScore = "/App/MandarinScoreHandler.ashx";

	/**
	 * 8、英语口语测试 地址： 请求方式： POST 请求参数：confirmNo=考试号（确认以后生成的确认号）&score=
	 * 英语口语评分标准对应的枚举 返回参数： 单个考生信息 JsonObject
	 */
	public static final String URL_OracyScore = "/App/OracyScoreHandler.ashx";

	/**
	 * 9、查询专业 地址： 请求方式： POST 请求参数：groupType=初试：C；复试：F&score= 英语口语评分标准对应的枚举 返回参数：
	 * 返回所有当前可用的招生计划 JsonArray
	 **/
	public static final String URL_QuerySchedule = "/App/QueryScheduleHandler.ashx";

	/**
	 * 10、查询面试批次 地址： 请求方式： POST 请求参数：scheduleId= 用户选择的专业ID 返回参数： 返回所有对应的面试批次
	 * JsonArray
	 **/
	public static final String URL_QueryInterview = "/App/QueryInterviewHandler.ashx";

	/**
	 * 11、提交分组 地址： 请求方式： POST
	 * 请求参数：groupType=初试：C；复试：F&interviewId=面试批次的ID&studentDate= List
	 * <考生信息> 返回参数： { Group = 分组实体, Students = 组内所有的考生信息 } public class
	 * 考生信息:BusinessEntity { public string SignupNo { get; set; } public string
	 * ConfirmNo { get; set; } }
	 */
	public static final String URL_JoinGroup = "/App/SubmitGroupHandler.ashx";

	/**
	 * 12、 查询分组 地址：请求方式： POST 请求参数：groupName= 组名 返回参数： { Group = 分组实体, Students
	 * = 组内所有的考生信息 }
	 **/
	public static final String URL_GetGroup = "/App/GetGroupHandler.ashx";

	/**
	 * 13、 提交评分 地址：请求方式： POST 请求参数：groupName=组名&scoreDate= List<评分实体对象> 返回参数：
	 * 评分提交结果 评分实体对象： public class 评分实体对:BusinessEntity { public int? StudentID
	 * { get; set; } public string Score { get; set; } public string Description
	 * { get; set; } }
	 **/
	public static final String URL_SubmitScore = "/App/SubmitScoreHandler.ashx";

	public static final String URL_QueryEnum = "/App/QueryEnumHandler.ashx";
	
	public static final String URL_ColorWeakness = "/App/ColorWeaknessHandler.ashx";

	public static final String GROUP_TYPE_FIRST = "C";
	public static final String GROUP_TYPE_SECOND = "F";

	public SwzfHttpHandler(AsyncHttpCallBack callback, Context context) {
		super(callback, context);

		if (GlobalData.curUser == null) {
			String str = PrefUtil.getPref(context, "user", "");
			if (!str.isEmpty()) {
				GlobalData.curUser = JSONUtils.fromJson(str, EntityUser.class);
			}
		}
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 *            登录名
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public void login(String userName, String password) throws Exception {
		RequestParams params = new RequestParams();
		params.add("userId", URLEncoder.encode(userName,"utf-8"));
		params.add("userPwd", password);
		new AsyncHttpUtil().post(BASE_URL + URL_LOGIN, params, handler);
	}

	/**
	 * 获取学生信息
	 * 
	 * @param studentNo
	 * @throws Exception
	 */
	public void getStudent(String studentNo) throws Exception {
		RequestParams params = new RequestParams();
		params.add("studentNo", studentNo);
		new AsyncHttpUtil().post(BASE_URL + URL_GetStudent, params, handler);
	}

	/**
	 * 考生确认
	 * 
	 * @param studentNo
	 * @throws Exception
	 */
	public void confirm(String studentNo,String interviewId) throws Exception {
		RequestParams params = new RequestParams();
		params.add("signupNo", studentNo);
		params.add("interviewId", interviewId);
		new AsyncHttpUtil().post(BASE_URL + URL_Confirm, params, handler);
	}

	/**
	 * 提交身高体重
	 * 
	 * @param confirmNo
	 * @param height
	 * @param weight
	 * @throws Exception
	 */
	public void health(String confirmNo, String height, String weight) throws Exception {
		RequestParams params = new RequestParams();
		params.add("confirmNo", confirmNo);
		params.add("height", height);
		params.add("weight", weight);
		new AsyncHttpUtil().post(BASE_URL + URL_Health, params, handler);
	}

	/**
	 * 提交视力
	 * 
	 * @param confirmNo
	 * @param leftSight
	 * @param rightSight
	 * @throws Exception
	 */
	public void sight(String confirmNo, String leftSight, String rightSight) throws Exception {
		RequestParams params = new RequestParams();
		params.add("confirmNo", confirmNo);
		params.add("leftSight", leftSight);
		params.add("rightSight", rightSight);
		new AsyncHttpUtil().post(BASE_URL + URL_Sight, params, handler);
	}

	/**
	 * 提交英语口语测试
	 * 
	 * @param confirmNo
	 * @param score
	 * @throws Exception
	 */
	public void oracyScore(String confirmNo, String score) throws Exception {
		RequestParams params = new RequestParams();
		params.add("confirmNo", confirmNo);
		params.add("score", score);
		new AsyncHttpUtil().post(BASE_URL + URL_OracyScore, params, handler);
	}

	/**
	 * 提交普通话测试
	 * 
	 * @param confirmNo
	 * @param score
	 * @throws Exception
	 */
	public void mandarinScore(String confirmNo, String score) throws Exception {
		RequestParams params = new RequestParams();
		params.add("confirmNo", confirmNo);
		params.add("score", score);
		new AsyncHttpUtil().post(BASE_URL + URL_MandarinScore, params, handler);
	}
	
	public void colorWeakness(String confirmNo, String colorWeakness) throws Exception {
		RequestParams params = new RequestParams();
		params.add("confirmNo", confirmNo);
		params.add("colorWeakness",colorWeakness);
		new AsyncHttpUtil().post(BASE_URL + URL_ColorWeakness, params, handler);
	}

	/**
	 * 查询枚举值
	 * 
	 * @throws Exception
	 */
	public void queryEnum() throws Exception {
		RequestParams params = new RequestParams();
		new AsyncHttpUtil().post(BASE_URL + URL_QueryEnum, params, handler);
	}

	/**
	 * 查询专业
	 * 
	 * @param groupType
	 * @throws Exception
	 */
	public void querySchedule() throws Exception {
		RequestParams params = new RequestParams();
		//params.add("groupType", groupType);
		new AsyncHttpUtil().post(BASE_URL + URL_QuerySchedule, params, handler);
	}

	/**
	 * 查询批次
	 * 
	 * @param scheduleId
	 * @throws Exception
	 */
	public void queryInterview(String scheduleId) throws Exception {
		RequestParams params = new RequestParams();
		params.add("scheduleId", scheduleId);
		new AsyncHttpUtil().post(BASE_URL + URL_QueryInterview, params, handler);
	}

	/**
	 * 提交分组
	 * 
	 * @param groupType
	 * @param interviewID
	 * @param studentData
	 * @throws Exception
	 */
	// groupType=初试：C；复试：F&interviewId=面试批次的ID&studentDate= List
	public void joinGroup(String groupType, String interviewID, String studentData) throws Exception {
		RequestParams params = new RequestParams();
		params.add("groupType", groupType);
		params.add("interviewID", interviewID);
		params.add("studentData", studentData);
		new AsyncHttpUtil().post(BASE_URL + URL_JoinGroup, params, handler);
	}

	/**
	 * 获取分组
	 * 
	 * @param groupName
	 * @throws Exception
	 */
	public void getGroup(String groupName,String groupProperty) throws Exception {
		RequestParams params = new RequestParams();
		params.add("groupName", groupName);
		params.add("groupProperty", groupProperty);
		new AsyncHttpUtil().post(BASE_URL + URL_GetGroup, params, handler);
	}

	/**
	 * 提交评分
	 * 
	 * @param groupName
	 * @param scoreData
	 * @throws Exception
	 */
	public void submitScore(String groupName, String scoreData) throws Exception {
		RequestParams params = new RequestParams();
		params.add("groupName", groupName);
		params.add("scoreData", scoreData);
		new AsyncHttpUtil().post(BASE_URL + URL_SubmitScore, params, handler);
	}
}
