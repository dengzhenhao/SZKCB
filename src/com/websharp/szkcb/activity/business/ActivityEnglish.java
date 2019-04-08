package com.websharp.szkcb.activity.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.entity.EntityStudent;
import com.websharp.fragment.FragmentStudentInfo;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.qr.CaptureActivity;
import com.websharp.szkcb.R;
import com.websharp.szkcb.activity.BaseActivity;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityEnglish extends BaseActivity {

	FragmentStudentInfo fragmentStudentInfo;
	private ImageView iv_menu_type;
	private TextView tv_menu_type;
	private LinearLayout layout_back;
	private LinearLayout layout_qr;
	private LinearLayout layout_save;
	private CheckBox cbx_best,cbx_qualied,cbx_unqualied;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_qr:
			cbx_qualied.performClick();
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, Constant.QR_REQUEST_CODE_MAIN);
			break; 
		case R.id.layout_save:
			if (!cbx_best.isChecked() && !cbx_qualied.isChecked() && !cbx_unqualied.isChecked()) {
				Util.createToast(this, "请选择英语口语考试成绩", 3000).show();
				return;
			}
			String score = "";
			if(cbx_best.isChecked()){
				score = cbx_best.getTag().toString();
			}
			if(cbx_qualied.isChecked()){
				score = cbx_qualied.getTag().toString();
			}
			if(cbx_unqualied.isChecked()){
				score = cbx_unqualied.getTag().toString();
			}
			try {
				AsyncOracyScore(GlobalData.curStudent.ConfirmNo,score);
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
		setContentView(R.layout.activity_english);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_menu_type = (ImageView) findViewById(R.id.iv_menu_type);
		tv_menu_type = (TextView) findViewById(R.id.tv_menu_type);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_qr = (LinearLayout) findViewById(R.id.layout_qr);
		layout_save = (LinearLayout) findViewById(R.id.layout_save);
		cbx_best= (CheckBox) findViewById(R.id.cbx_best);
		cbx_qualied= (CheckBox) findViewById(R.id.cbx_qualied);
		cbx_unqualied = (CheckBox) findViewById(R.id.cbx_unqualied);
		fragmentStudentInfo = (FragmentStudentInfo) getFragmentManager().findFragmentById(R.id.frag_student_info);
		layout_back.setOnClickListener(this);
		layout_qr.setOnClickListener(this);
		layout_save.setOnClickListener(this);
		cbx_best.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cbx_best.isChecked()){
					cbx_qualied.setChecked(false);
					cbx_unqualied.setChecked(false);
				}
			}
		});
		
		cbx_qualied.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cbx_qualied.isChecked()){
					cbx_best.setChecked(false);
					cbx_unqualied.setChecked(false);
				}
			}
		});
		
		cbx_unqualied.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cbx_unqualied.isChecked()){
					cbx_best.setChecked(false);
					cbx_qualied.setChecked(false);
				}
			}
		});
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		iv_menu_type.setImageResource(R.drawable.icon_english);
		tv_menu_type.setText("英语口语");
		layout_qr.performClick();
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
				Util.createToast(ActivityEnglish.this, obj.optString("message"), 3000).show();
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
		new SwzfHttpHandler(cb_query_student_info, ActivityEnglish.this).getStudent(studentNo);
	}

	private void AsyncOracyScore(String confirmNo,String score) throws Exception {
		new SwzfHttpHandler(cb_query_student_info, ActivityEnglish.this).oracyScore(confirmNo,score);
	}


}
