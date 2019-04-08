package com.websharp.szkcb.activity.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.entity.EntityStudent;
import com.websharp.entity.EntityUser;
import com.websharp.fragment.FragmentStudentInfo;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.qr.CaptureActivity;
import com.websharp.szkcb.R;
import com.websharp.szkcb.activity.BaseActivity;
import com.websharp.szkcb.activity.login.ActivityLogin;
import com.websharp.szkcb.activity.main.ActivityMain;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.StringUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityHeightWeight extends BaseActivity {

	FragmentStudentInfo fragmentStudentInfo;
	private ImageView iv_menu_type;
	private TextView tv_menu_type;
	private LinearLayout layout_back;
	private LinearLayout layout_qr;
	private LinearLayout layout_save;
	private EditText et_height;
	private EditText et_weight;
	private ScrollView sv;
	private LinearLayout layout_out;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break; 
		case R.id.layout_qr:
			et_height.setText("");
			et_weight.setText("");
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, Constant.QR_REQUEST_CODE_MAIN);
			break;
		case R.id.layout_save:
			if (getText(et_height).isEmpty()) {
				Util.createToast(this, "身高不能为空", 3000).show();
				return;
			}
			if (getText(et_weight).isEmpty()) {
				Util.createToast(this, "体重不能为空", 3000).show();
				return;
			}
			try {
				AsyncHealth(GlobalData.curStudent.ConfirmNo, getText(et_height), getText(et_weight));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_height_weight);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		sv = (ScrollView) findViewById(R.id.sv);
		layout_out = (LinearLayout) findViewById(R.id.layout_out);
		iv_menu_type = (ImageView) findViewById(R.id.iv_menu_type);
		tv_menu_type = (TextView) findViewById(R.id.tv_menu_type);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_qr = (LinearLayout) findViewById(R.id.layout_qr);
		layout_save = (LinearLayout) findViewById(R.id.layout_save);
		et_height = (EditText) findViewById(R.id.et_height);
		et_weight = (EditText) findViewById(R.id.et_weight);
		fragmentStudentInfo = (FragmentStudentInfo) getFragmentManager().findFragmentById(R.id.frag_student_info);
		layout_back.setOnClickListener(this);
		layout_qr.setOnClickListener(this);
		layout_save.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		iv_menu_type.setImageResource(R.drawable.icon_height_weight);
		tv_menu_type.setText("体重身高");
		layout_qr.performClick();
		layout_out.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				sv.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Constant.QR_REQUEST_CODE_MAIN) {
				String studentNo = data.getExtras().getString("data");
				try {
					AsyncGetStudent(studentNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
				Util.createToast(ActivityHeightWeight.this, obj.optString("message"), 3000).show();
				if (obj.optString("code").equals("0")) {
					JSONObject jsonData = obj.getJSONObject("data");
					GlobalData.curStudent = JSONUtils.fromJson(jsonData.toString(), EntityStudent.class);
					fragmentStudentInfo.bindData();
				} else {
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

	private void AsyncGetStudent(String studentNo) throws Exception {
		new SwzfHttpHandler(cb_query_student_info, ActivityHeightWeight.this).getStudent(studentNo);
	}

	private void AsyncHealth(String confirmNo, String height, String weight) throws Exception {
		new SwzfHttpHandler(cb_query_student_info, ActivityHeightWeight.this).health(confirmNo, height, weight);
	}

}
