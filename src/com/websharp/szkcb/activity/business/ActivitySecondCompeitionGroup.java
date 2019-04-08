package com.websharp.szkcb.activity.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.entity.EntityStudent;
import com.websharp.fragment.FragmentScheduleSecond;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivitySecondCompeitionGroup extends BaseActivity  implements FragmentScheduleSecond.FragmentInteraction {

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
	private FragmentScheduleSecond frag_schedule;

	@Override
	public void process(int curInterviewID) {
		GlobalData.curInterviewIDSecondCompetition = curInterviewID;
		for (int i = 0; i < GlobalData.listInterviewSecond.size(); i++) {
			if (curInterviewID == GlobalData.listInterviewSecond.get(i).ID) {
				tv_batch.setText("当前批次：" + GlobalData.listInterviewSecond.get(i).InterviewName);
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
			if (GlobalData.curInterviewIDSecondCompetition == 0) {
				Util.createToast(this, "请选择批次", 3000).show();
				return;
			}
			if (GlobalData.listStudentSecondCompetitionWaitGroup.size() == 0) {
				Util.createToast(ActivitySecondCompeitionGroup.this, "当前分组没有学生！", 3000).show();
				return;
			}

			Util.createDialog(this, null, R.string.dialog_title_common, R.string.dialog_msg_confirm, null, true,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							JSONArray jArr = new JSONArray();
							try {
								for (int i = 0; i < GlobalData.listStudentSecondCompetitionWaitGroup.size(); i++) {
									JSONObject jObj = new JSONObject();
									jObj.put("SignupNo",
											GlobalData.listStudentSecondCompetitionWaitGroup.get(i).SignupNo + "");
									jObj.put("ConfirmNo",
											GlobalData.listStudentSecondCompetitionWaitGroup.get(i).ConfirmNo + "");
									jArr.put(jObj);
								}
								AsyncJoinGroup(SwzfHttpHandler.GROUP_TYPE_SECOND,
										GlobalData.curInterviewIDSecondCompetition + "", jArr.toString());
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
		setContentView(R.layout.activity_second_competition_group);
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
		frag_schedule = (FragmentScheduleSecond) getFragmentManager().findFragmentById(R.id.frag_schedule);
		frag_schedule.groupType = SwzfHttpHandler.GROUP_TYPE_SECOND;
		layout_back.setOnClickListener(this);
		layout_qr.setOnClickListener(this);
		layout_save.setOnClickListener(this);
		layout_choose_batch.setOnClickListener(this);
		mInflater = LayoutInflater.from(ActivitySecondCompeitionGroup.this);
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		iv_menu_type.setImageResource(R.drawable.icon_second_competition_group);
		tv_menu_type.setText("复试分组");
		if (GlobalData.curInterviewIDSecondCompetition != 0) {
			for (int i = 0; i < GlobalData.listInterviewSecond.size(); i++) {
				if (GlobalData.curInterviewIDSecondCompetition == GlobalData.listInterviewSecond.get(i).ID) {
					tv_batch.setText("当前批次：" + GlobalData.listInterviewSecond.get(i).InterviewName);
					break;
				}
			}
		}

		if (GlobalData.listStudentSecondCompetitionWaitGroup.size() > 0) {
			bindStudent();
		} 
	}

	LayoutInflater mInflater = null;
	View convertView = null;

	public void bindStudent() {
		layout_list_student.removeAllViews();
		for (int i = 0; i < GlobalData.listStudentSecondCompetitionWaitGroup.size(); i++) {
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
			tv_name.setText(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).RealName);
			tv_sex.setText(UtilKeyValue.GetSex(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).Sex));
			tv_birthday.setText(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).Birthday);
			tv_test_no.setText(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).ConfirmNo);
			tv_ethnic.setText(UtilKeyValue.GetNation(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).Nation));
			tv_mobile.setText(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).MobilePhone);
			tv_junior_high_school.setText(GlobalData.listStudentSecondCompetitionWaitGroup.get(i).SchoolName);
			iv_delete.setTag(GlobalData.listStudentSecondCompetitionWaitGroup.get(i));
			iv_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					EntityStudent student = (EntityStudent) v.getTag();
					String confirm_no = student.ConfirmNo;
					for (int k = 0; k < GlobalData.listStudentSecondCompetitionWaitGroup.size(); k++) {
						if (confirm_no.equals(GlobalData.listStudentSecondCompetitionWaitGroup.get(k).ConfirmNo)) {
							GlobalData.listStudentSecondCompetitionWaitGroup.remove(k);
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
					if (GlobalData.listStudentSecondCompetitionWaitGroup.size() == 10) {
						Util.createToast(ActivitySecondCompeitionGroup.this, "当前分组已有10名学生！", 3000).show();
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
				Util.createToast(ActivitySecondCompeitionGroup.this, obj.optString("message"), 3000).show();
				if (obj.optString("code").equals("0")) {
					JSONObject jsonData = obj.getJSONObject("data");
					EntityStudent student = JSONUtils.fromJson(jsonData.toString(), EntityStudent.class);
					if (student.ConfirmNo == null || student.ConfirmNo.isEmpty()) {
						Util.createToast(ActivitySecondCompeitionGroup.this, "此学生还未确认，没有考试号！", 3000).show();
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
						GlobalData.listStudentSecondCompetitionWaitGroup.add(student);
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
		new SwzfHttpHandler(cb_query_student_info, ActivitySecondCompeitionGroup.this).getStudent(studentNo);
	}

	AsyncHttpCallBack cb_join_group = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				Util.createToast(ActivitySecondCompeitionGroup.this, obj.optString("message"), 3000).show();
				if (obj.optString("code").equals("0")) {
					JSONObject jsonData = obj.getJSONObject("data");
					GlobalData.listStudentSecondCompetitionWaitGroup.clear();
					GlobalData.curInterviewIDSecondCompetition = 0;
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
		new SwzfHttpHandler(cb_join_group, ActivitySecondCompeitionGroup.this).joinGroup(groupType, interviewID,
				studentData);
	}

}
