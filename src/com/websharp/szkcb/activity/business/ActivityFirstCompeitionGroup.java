package com.websharp.szkcb.activity.business;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.entity.EntityStudent;
import com.websharp.entity.EntityUser;
import com.websharp.fragment.FragmentScheduleFirst;
import com.websharp.fragment.FragmentStudentInfo;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.qr.CaptureActivity;
import com.websharp.szkcb.R;
import com.websharp.szkcb.activity.BaseActivity;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityFirstCompeitionGroup extends BaseActivity implements FragmentScheduleFirst.FragmentInteraction {

	private ImageView iv_menu_type;
	private TextView tv_menu_type;
	private LinearLayout layout_back;
	private LinearLayout layout_qr;
	private LinearLayout layout_save;
	private LinearLayout layout_choose_batch;
	private TextView tv_batch;
	private LinearLayout layout_list_student;
	private int batch_id = 0;
	private LinearLayout layout_content, layout_schedule;
	private FragmentScheduleFirst frag_schedule;

	@Override
	public void process(int curInterviewID) {
		GlobalData.curInterviewIDFirstCompetition = curInterviewID;
		for (int i = 0; i < GlobalData.listInterviewFirst.size(); i++) {
			if (curInterviewID == GlobalData.listInterviewFirst.get(i).ID) {
				tv_batch.setText("当前批次：" + GlobalData.listInterviewFirst.get(i).InterviewName);
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
		case R.id.layout_save:
			if (GlobalData.curInterviewIDFirstCompetition == 0) {
				Util.createToast(this, "请选择批次", 3000).show();
				return;
			}
			if (GlobalData.listStudentFirstCompetitionWaitGroup.size() == 0) {
				Util.createToast(ActivityFirstCompeitionGroup.this, "当前分组没有学生！", 3000).show();
				return;
			}

			Util.createDialog(this, null, R.string.dialog_title_common, R.string.dialog_msg_confirm, null, true,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							JSONArray jArr = new JSONArray();
							try {
								for (int i = 0; i < GlobalData.listStudentFirstCompetitionWaitGroup.size(); i++) {
									JSONObject jObj = new JSONObject();
									jObj.put("SignupNo",
											GlobalData.listStudentFirstCompetitionWaitGroup.get(i).SignupNo + "");
									jObj.put("ConfirmNo",
											GlobalData.listStudentFirstCompetitionWaitGroup.get(i).ConfirmNo + "");
									jArr.put(jObj);
								}
								AsyncJoinGroup(SwzfHttpHandler.GROUP_TYPE_FIRST,
										GlobalData.curInterviewIDFirstCompetition + "", jArr.toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).show();
			break;
		}

	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_first_competition_group);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_menu_type = (ImageView) findViewById(R.id.iv_menu_type);
		tv_menu_type = (TextView) findViewById(R.id.tv_menu_type);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_qr = (LinearLayout) findViewById(R.id.layout_qr);
		layout_save = (LinearLayout) findViewById(R.id.layout_save);
		layout_choose_batch = (LinearLayout) findViewById(R.id.layout_choose_batch);
		layout_list_student = (LinearLayout) findViewById(R.id.layout_list_student);
		tv_batch = (TextView) findViewById(R.id.tv_batch);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		layout_schedule = (LinearLayout) findViewById(R.id.layout_schedule);
		frag_schedule = (FragmentScheduleFirst) getFragmentManager().findFragmentById(R.id.frag_schedule);
		frag_schedule.groupType = SwzfHttpHandler.GROUP_TYPE_FIRST;
		layout_back.setOnClickListener(this);
		layout_qr.setOnClickListener(this);
		layout_save.setOnClickListener(this);
		layout_choose_batch.setOnClickListener(this);
		mInflater = LayoutInflater.from(ActivityFirstCompeitionGroup.this);
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		iv_menu_type.setImageResource(R.drawable.icon_first_competition_group);
		tv_menu_type.setText("初试分组");
		if (GlobalData.curInterviewIDFirstCompetition != 0) {
			for (int i = 0; i < GlobalData.listInterviewFirst.size(); i++) {
				if (GlobalData.curInterviewIDFirstCompetition == GlobalData.listInterviewFirst.get(i).ID) {
					tv_batch.setText("当前批次：" + GlobalData.listInterviewFirst.get(i).InterviewName);
					break;
				}
			}
		}

		if (GlobalData.listStudentFirstCompetitionWaitGroup.size() > 0) {
			bindStudent();
		}
	}

	LayoutInflater mInflater = null;
	View convertView = null;

	public void bindStudent() {
		layout_list_student.removeAllViews();
		for (int i = 0; i < GlobalData.listStudentFirstCompetitionWaitGroup.size(); i++) {
			convertView = mInflater.inflate(R.layout.item_group, null);
			LinearLayout layout_item_bg = (LinearLayout) convertView.findViewById(R.id.layout_item_bg);
			TextView tv_seq = (TextView) convertView.findViewById(R.id.tv_seq);
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			TextView tv_sex = (TextView) convertView.findViewById(R.id.tv_sex);
			TextView tv_birthday = (TextView) convertView.findViewById(R.id.tv_birthday);
			TextView tv_test_no = (TextView) convertView.findViewById(R.id.tv_test_no);
			TextView tv_ethnic = (TextView) convertView.findViewById(R.id.tv_ethnic);
			TextView tv_mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
			TextView tv_junior_high_school = (TextView) convertView.findViewById(R.id.tv_junior_high_school);
			ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			if (i % 2 == 0) {
				layout_item_bg.setBackgroundResource(R.color.color_list_bg_f3f3f3);
			} else {
				layout_item_bg.setBackgroundResource(R.color.color_list_bg_ffffff);
			}
			tv_seq.setText(i + 1 + "");
			tv_name.setText(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).RealName);
			tv_sex.setText(UtilKeyValue.GetSex(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).Sex));
			tv_birthday.setText(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).Birthday);
			tv_test_no.setText(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).ConfirmNo);
			tv_ethnic.setText(UtilKeyValue.GetNation(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).Nation));
			tv_mobile.setText(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).MobilePhone);
			tv_junior_high_school.setText(GlobalData.listStudentFirstCompetitionWaitGroup.get(i).SchoolName);
			iv_delete.setTag(GlobalData.listStudentFirstCompetitionWaitGroup.get(i));
			iv_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					EntityStudent student = (EntityStudent) v.getTag();
					String confirm_no = student.ConfirmNo;
					for (int k = 0; k < GlobalData.listStudentFirstCompetitionWaitGroup.size(); k++) {
						if (confirm_no.equals(GlobalData.listStudentFirstCompetitionWaitGroup.get(k).ConfirmNo)) {
							GlobalData.listStudentFirstCompetitionWaitGroup.remove(k);
							break;
						}
					}
					bindStudent();
				}
			});
			layout_list_student.addView(convertView);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Constant.QR_REQUEST_CODE_MAIN) {
				String studentNo = data.getExtras().getString("data");
				try {
					if (GlobalData.listStudentFirstCompetitionWaitGroup.size() == 10) {
						Util.createToast(ActivityFirstCompeitionGroup.this, "当前分组已有10名学生！", 3000).show();
						return;
					}
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
				Util.createToast(ActivityFirstCompeitionGroup.this, obj.optString("message"), 3000).show();
				if (obj.optString("code").equals("0")) {
					JSONObject jsonData = obj.getJSONObject("data");
					EntityStudent student = JSONUtils.fromJson(jsonData.toString(), EntityStudent.class);
					if (student.ConfirmNo == null || student.ConfirmNo.isEmpty()) {
						Util.createToast(ActivityFirstCompeitionGroup.this, "此学生还未确认，没有考试号！", 3000).show();
						return;
					}

					boolean exist = false;
					// for (int i = 0; i < listStudent.size(); i++) {
					// if
					// (student.ConfirmNo.equals(listStudent.get(i).ConfirmNo))
					// {
					// exist = true;
					// break;
					// }
					// }
					if (!exist) {
						GlobalData.listStudentFirstCompetitionWaitGroup.add(student);
						bindStudent();
					}
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
		new SwzfHttpHandler(cb_query_student_info, ActivityFirstCompeitionGroup.this).getStudent(studentNo);
	}

	AsyncHttpCallBack cb_join_group = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				Util.createToast(ActivityFirstCompeitionGroup.this, obj.optString("message"), 3000).show();
				if (obj.optString("code").equals("0")) {
					JSONObject jsonData = obj.getJSONObject("data");
					GlobalData.listStudentFirstCompetitionWaitGroup.clear();
					GlobalData.curInterviewIDFirstCompetition = 0;
					bindStudent();
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

	private void AsyncJoinGroup(String groupType, String interviewID, String studentData) throws Exception {
		new SwzfHttpHandler(cb_join_group, ActivityFirstCompeitionGroup.this).joinGroup(groupType, interviewID,
				studentData);
	}

}
