package com.websharp.szkcb.activity.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.entity.EntityKeyValue;
import com.websharp.entity.EntityStudent;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.szkcb.R;
import com.websharp.szkcb.activity.BaseActivity;
import com.websharp.szkcb.activity.business.ActivityColorWekness;
import com.websharp.szkcb.activity.business.ActivityChinese;
import com.websharp.szkcb.activity.business.ActivityConfirm;
import com.websharp.szkcb.activity.business.ActivityEnglish;
import com.websharp.szkcb.activity.business.ActivityFirstCompeition;
import com.websharp.szkcb.activity.business.ActivityFirstCompeitionGroup;
import com.websharp.szkcb.activity.business.ActivityHeightWeight;
import com.websharp.szkcb.activity.business.ActivitySecondCompeition;
import com.websharp.szkcb.activity.business.ActivitySecondCompeitionGroup;
import com.websharp.szkcb.activity.business.ActivitySight;
import com.websharp.szkcb.activity.login.ActivityLogin;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMain extends BaseActivity {

	private TextView tv_username;
	private ImageView iv_cancel;
	private ImageView iv_height_weight;
	private ImageView iv_color_weakness;
	private ImageView iv_sight;
	private ImageView iv_english;
	private ImageView iv_chinese;
	private ImageView iv_first_competition_group;
	private ImageView iv_first_competition;
	private ImageView iv_second_competition_group;
	private ImageView iv_second_competition;
	private ImageView iv_confirm_student_info;
	private TextView tv_version;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cancel:
			Util.createDialog(this, null, R.string.dialog_title_common, R.string.dialog_msg_cancel, null, true,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							GlobalData.clear();
							Editor ed = PrefUtil.getEditor(ActivityMain.this);
							ed.remove("user");
							ed.remove("ticket");
							ed.commit();
							Util.startActivity(ActivityMain.this, ActivityLogin.class, true);
						}
					}).show();
			break;
		case R.id.iv_color_weakness:
			Util.startActivity(ActivityMain.this, ActivityColorWekness.class, false);
			break;
		case R.id.iv_height_weight:
			Util.startActivity(ActivityMain.this, ActivityHeightWeight.class, false);
			break;
		case R.id.iv_sight:
			Util.startActivity(ActivityMain.this, ActivitySight.class, false);
			break;
		case R.id.iv_english:
			Util.startActivity(ActivityMain.this, ActivityEnglish.class, false);
			break;
		case R.id.iv_chinese:
			Util.startActivity(ActivityMain.this, ActivityChinese.class, false);
			break;
		case R.id.iv_first_competition_group:
			Util.startActivity(ActivityMain.this, ActivityFirstCompeitionGroup.class, false);
			break;
		case R.id.iv_first_competition:
			Util.startActivity(ActivityMain.this, ActivityFirstCompeition.class, false);
			break;
		case R.id.iv_second_competition_group:
			Util.startActivity(ActivityMain.this, ActivitySecondCompeitionGroup.class, false);
			break;
		case R.id.iv_second_competition:
			Util.startActivity(ActivityMain.this, ActivitySecondCompeition.class, false);
			break;
		case R.id.iv_confirm_student_info:
			Util.startActivity(ActivityMain.this, ActivityConfirm.class, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void init() {
		AppData.InitAppData(this, getResources().getString(R.string.app_name));
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_username = (TextView) findViewById(R.id.tv_username);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
		iv_height_weight = (ImageView) findViewById(R.id.iv_height_weight);
		iv_sight = (ImageView) findViewById(R.id.iv_sight);
		iv_color_weakness = (ImageView) findViewById(R.id.iv_color_weakness);
		iv_english = (ImageView) findViewById(R.id.iv_english);
		iv_chinese = (ImageView) findViewById(R.id.iv_chinese);
		iv_first_competition_group = (ImageView) findViewById(R.id.iv_first_competition_group);
		iv_first_competition = (ImageView) findViewById(R.id.iv_first_competition);
		iv_second_competition_group = (ImageView) findViewById(R.id.iv_second_competition_group);
		iv_second_competition = (ImageView) findViewById(R.id.iv_second_competition);
		iv_confirm_student_info = (ImageView) findViewById(R.id.iv_confirm_student_info);

		iv_cancel.setOnClickListener(this);
		tv_version.setText("当前版本：" + AppData.VERSION_NAME+""+(SwzfHttpHandler.BASE_URL.indexOf(SwzfHttpHandler.BASE_URL_COMMON)>=0?"正式":"测试"));
		boolean allowSubmit = false;
		String src = "";
		for (int i = 0; i < GlobalData.listRight.size(); i++) {
			allowSubmit = GlobalData.listRight.get(i).AllowSubmit;
			src = GlobalData.listRight.get(i).Src;
			if (src.equals("01")) {
				if (allowSubmit)
					iv_confirm_student_info.setOnClickListener(this);
				else
					iv_confirm_student_info.setBackgroundResource(R.drawable.btn_confirm_student_info_disabled);
			} else if (src.equals("02")) {
				if (allowSubmit)
					iv_sight.setOnClickListener(this);
				else
					iv_sight.setBackgroundResource(R.drawable.btn_sight_disabled);
			} else if (src.equals("03")) {
				if (allowSubmit)
					iv_height_weight.setOnClickListener(this);
				else
					iv_height_weight.setBackgroundResource(R.drawable.btn_height_weight_disabled);
			} else if (src.equals("04")) {
				if (allowSubmit)
					iv_color_weakness.setOnClickListener(this);
				else
					iv_color_weakness.setBackgroundResource(R.drawable.btn_bsnl_disabled);
			} else if (src.equals("05")) {
				if (allowSubmit)
					iv_english.setOnClickListener(this);
				else
					iv_english.setBackgroundResource(R.drawable.btn_english_disabled);
			} else if (src.equals("06")) {
				if (allowSubmit)
					iv_chinese.setOnClickListener(this);
				else
					iv_chinese.setBackgroundResource(R.drawable.btn_chinese_disabled);
			} else if (src.equals("07")) {
				if (allowSubmit)
					iv_first_competition_group.setOnClickListener(this);
				else
					iv_first_competition_group.setBackgroundResource(R.drawable.btn_first_competition_group_disabled);
			} else if (src.equals("08")) {
				if (allowSubmit)
					iv_first_competition.setOnClickListener(this);
				else
					iv_first_competition.setBackgroundResource(R.drawable.btn_first_competition_disabled);
			} else if (src.equals("09")) {
				if (allowSubmit)
					iv_second_competition_group.setOnClickListener(this);
				else
					iv_second_competition_group.setBackgroundResource(R.drawable.btn_second_competition_group_disabled);
			} else if (src.equals("10")) {
				if (allowSubmit)
					iv_second_competition.setOnClickListener(this);
				else
					iv_second_competition.setBackgroundResource(R.drawable.btn_second_competition_disabled);
			}
		}
	}

	@Override
	public void bindData() {
		tv_username.setText(GlobalData.curUser.UserName);
		try {
			AsyncQueryEnum();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	AsyncHttpCallBack cb_query_student_info = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Gson gson = new Gson();
					UtilKeyValue.listArea = gson.fromJson(obj.getJSONObject("data").getString("Area"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listGeneral = gson.fromJson(obj.getJSONObject("data").getString("General"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listMandarin = gson.fromJson(obj.getJSONObject("data").getString("Mandarin"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listNation = gson.fromJson(obj.getJSONObject("data").getString("Nation"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listOracy = gson.fromJson(obj.getJSONObject("data").getString("Oracy"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listRetrial = gson.fromJson(obj.getJSONObject("data").getString("Retrial"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listSex = gson.fromJson(obj.getJSONObject("data").getString("Sex"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
					UtilKeyValue.listColorWeakNess = gson.fromJson(obj.getJSONObject("data").getString("ColorWeakness"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityKeyValue>>() {
							}.getType());
				} else {
					Util.createToast(ActivityMain.this, obj.optString("message"), 3000).show();
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

	private void AsyncQueryEnum() throws Exception {
		new SwzfHttpHandler(cb_query_student_info, ActivityMain.this).queryEnum();
	}

}
