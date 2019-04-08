package com.websharp.szkcb.activity.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.entity.EntityStudent;
import com.websharp.fragment.FragmentScheduleConfirm;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityConfirm extends BaseActivity  implements FragmentScheduleConfirm.FragmentInteraction {

	FragmentStudentInfo fragmentStudentInfo;
	private ImageView iv_menu_type;
	private TextView tv_menu_type;
	private LinearLayout layout_back;
	private LinearLayout layout_qr;
	private LinearLayout layout_choose_batch;
	private TextView tv_batch;
	private LinearLayout layout_content,layout_schedule;
	private FragmentScheduleConfirm frag_schedule;

	@Override
	public void process(int curInterviewID) {
		GlobalData.curInterviewIDConfirm = curInterviewID;
		for (int i = 0; i < GlobalData.listInterviewConfirm.size(); i++) {
			if (curInterviewID == GlobalData.listInterviewConfirm.get(i).ID) {
				tv_batch.setText("当前批次：" + GlobalData.listInterviewConfirm.get(i).InterviewName);
				break;
			}
		}
		layout_schedule.setVisibility(View.GONE);
		layout_content.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_qr:
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, Constant.QR_REQUEST_CODE_MAIN);
			break;
		case R.id.layout_choose_batch:
			layout_content.setVisibility(View.GONE);
			layout_schedule.setVisibility(View.VISIBLE);
			frag_schedule.bindDataSchedule();
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_confirm);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_menu_type = (ImageView) findViewById(R.id.iv_menu_type);
		tv_menu_type = (TextView) findViewById(R.id.tv_menu_type);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_qr = (LinearLayout)findViewById(R.id.layout_qr);
		fragmentStudentInfo=(FragmentStudentInfo)getFragmentManager().findFragmentById(R.id.frag_student_info);
		layout_choose_batch = (LinearLayout) findViewById(R.id.layout_choose_batch);
		tv_batch = (TextView) findViewById(R.id.tv_batch);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		layout_schedule = (LinearLayout) findViewById(R.id.layout_schedule);
		frag_schedule = (FragmentScheduleConfirm) getFragmentManager().findFragmentById(R.id.frag_schedule);
		frag_schedule.groupType = SwzfHttpHandler.GROUP_TYPE_FIRST;
		layout_choose_batch.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		layout_qr.setOnClickListener(this);
	} 

	@Override
	public void bindData() {
		// TODO Auto-generated method stub 
		iv_menu_type.setImageResource(R.drawable.icon_confirm);
		tv_menu_type.setText("考生确认");
//		layout_qr.performClick();
		
		for (int i = 0; i < GlobalData.listInterviewConfirm.size(); i++) {
			if (GlobalData.curInterviewIDConfirm == GlobalData.listInterviewConfirm.get(i).ID) {
				tv_batch.setText("当前批次：" + GlobalData.listInterviewConfirm.get(i).InterviewName);
				break;
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Constant.QR_REQUEST_CODE_MAIN) {
				String studentNo = data.getExtras().getString("data");
				try {
					if (GlobalData.curInterviewIDConfirm == 0) {
						Util.createToast(this, "请选择批次", 3000).show();
						return;
					}
					AsyncConfirm(studentNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	AsyncHttpCallBack cb_confirm = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Util.createToast(ActivityConfirm.this, obj.optString("message"), 3000).show();
					JSONObject jsonData = obj.getJSONObject("data");
					GlobalData.curStudent = JSONUtils.fromJson(jsonData.toString(), EntityStudent.class);
					fragmentStudentInfo.bindData();
					
				} else {
					Util.createToast(ActivityConfirm.this, obj.optString("message"), 3000).show();
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

	private void AsyncConfirm(String studentNo) throws Exception {
		new SwzfHttpHandler(cb_confirm, ActivityConfirm.this).confirm(studentNo,GlobalData.curInterviewIDConfirm+"");
	}

}
