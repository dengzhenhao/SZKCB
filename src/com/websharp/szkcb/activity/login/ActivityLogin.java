package com.websharp.szkcb.activity.login;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.entity.EntityKeyValue;
import com.websharp.entity.EntityRight;
import com.websharp.entity.EntityUser;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.szkcb.R;
import com.websharp.szkcb.activity.BaseActivity;
import com.websharp.szkcb.activity.main.ActivityMain;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.StringUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends BaseActivity {

	EditText et_username;
	EditText et_password;
	Button btn_login;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			try {
				AsyncLogin();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_login);
	}

	@Override
	public void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
	}

	@Override
	public void bindData() {
		btn_login.setOnClickListener(this);

		Constant.InitDefaultDirs();
		AppData.InitAppData(this, Constant.APP_NAME);
		String str = PrefUtil.getPref(ActivityLogin.this, "user", "");
		if (!str.isEmpty()) {
			GlobalData.curUser = JSONUtils.fromJson(str, EntityUser.class);
			GlobalData.TICKET = PrefUtil.getPref(ActivityLogin.this, "ticket", "");
			String rights = PrefUtil.getPref(ActivityLogin.this, "rights", "");
			et_username.setText(GlobalData.curUser.UserID);
			et_password.setText(GlobalData.curUser.Password);
			GlobalData.listRight = new Gson().fromJson(rights,
					new com.google.gson.reflect.TypeToken<ArrayList<EntityRight>>() {
					}.getType());
			btn_login.performClick();
		}
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					JSONObject jsonUser = obj.getJSONObject("data").getJSONObject("User");
					jsonUser.put("Password", getText(et_password));
					GlobalData.curUser = JSONUtils.fromJson(jsonUser.toString(), EntityUser.class);
					GlobalData.TICKET = obj.getJSONObject("data").optString("Ticket", "");
					GlobalData.listRight = new Gson().fromJson(obj.getJSONObject("data").getString("Rights"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityRight>>() {
							}.getType());
					PrefUtil.setPref(ActivityLogin.this, "user", jsonUser.toString());
					PrefUtil.setPref(ActivityLogin.this, "ticket", GlobalData.TICKET);
					PrefUtil.setPref(ActivityLogin.this, "rights", obj.getJSONObject("data").getString("Rights"));
					initWithApiKey(ActivityLogin.this);
					Util.createToast(ActivityLogin.this, R.string.common_login_success, Toast.LENGTH_SHORT).show();
					Util.startActivity(ActivityLogin.this, ActivityMain.class, true);
				} else {
					Util.createToast(ActivityLogin.this, obj.optString("message"), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	private void AsyncLogin() throws Exception {
		String userName = getText(et_username);
		String password = getText(et_password);
		if (password.isEmpty() || userName.isEmpty()) {
			Util.createToast(this, "用户名与密码不能为空!", Toast.LENGTH_LONG).show();
			return;
		}

		if (StringUtil.isContainChinese(password)) {
			Util.createToast(this, "密码不能包含中文", Toast.LENGTH_LONG).show();
		}
		new SwzfHttpHandler(cb, ActivityLogin.this).login(userName, password);
	}
}
