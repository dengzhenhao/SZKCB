package com.websharp.fragment;

import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.szkcb.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentStudentInfo extends Fragment {
	TextView tv_name;
	TextView tv_sex;
	TextView tv_test_no;
	TextView tv_birthday;
	TextView tv_ethnic;
	TextView tv_junior_high_school;
	TextView tv_address;
	TextView tv_mobile;
	TextView tv_tel;
	TextView tv_duty_in_class;
	TextView tv_info_source;
	TextView tv_awards;
	TextView tv_art_speciality;
	TextView tv_height;
	TextView tv_weight;
	TextView tv_recorder_height_weight;
	TextView tv_sight_left;
	TextView tv_sight_right;
	TextView tv_weak_ness_result;
	TextView tv_recorder_sight;
	TextView tv_chinese_result;
	TextView tv_english_result;
	TextView tv_recorder_chinese;
	TextView tv_recorder_english;
	TextView tv_first_compeition_qualied_count;
	TextView tv_first_compeition_unqualied_count;
	TextView tv_second_compeition_level_a_count;
	TextView tv_second_compeition_level_b_count;
	TextView tv_second_compeition_level_c_count;
	TextView tv_total_score_result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_student_info, container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_sex = (TextView) view.findViewById(R.id.tv_sex);
		tv_test_no = (TextView) view.findViewById(R.id.tv_test_no);
		tv_birthday = (TextView) view.findViewById(R.id.tv_birthday);
		tv_ethnic = (TextView) view.findViewById(R.id.tv_ethnic);
		tv_junior_high_school = (TextView) view.findViewById(R.id.tv_junior_high_school);
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
		tv_tel = (TextView) view.findViewById(R.id.tv_tel);
		tv_duty_in_class = (TextView) view.findViewById(R.id.tv_duty_in_class);
		tv_info_source = (TextView) view.findViewById(R.id.tv_info_source);
		tv_awards = (TextView) view.findViewById(R.id.tv_awards);
		tv_art_speciality = (TextView) view.findViewById(R.id.tv_art_speciality);
		tv_height = (TextView) view.findViewById(R.id.tv_height);
		tv_weight = (TextView) view.findViewById(R.id.tv_weight);
		tv_recorder_height_weight = (TextView) view.findViewById(R.id.tv_recorder_height_weight);
		tv_sight_left = (TextView) view.findViewById(R.id.tv_sight_left);
		tv_sight_right = (TextView) view.findViewById(R.id.tv_sight_right);
		tv_weak_ness_result = (TextView) view.findViewById(R.id.tv_weak_ness_result);
		tv_recorder_sight = (TextView) view.findViewById(R.id.tv_recorder_sight);
		tv_chinese_result = (TextView) view.findViewById(R.id.tv_chinese_result);
		tv_english_result = (TextView) view.findViewById(R.id.tv_english_result);
		tv_recorder_chinese = (TextView) view.findViewById(R.id.tv_recorder_chinese);
		tv_recorder_english = (TextView) view.findViewById(R.id.tv_recorder_english);
		tv_first_compeition_qualied_count = (TextView) view.findViewById(R.id.tv_first_compeition_qualied_count);
		tv_first_compeition_unqualied_count = (TextView) view.findViewById(R.id.tv_first_compeition_unqualied_count);
		tv_second_compeition_level_a_count = (TextView) view.findViewById(R.id.tv_second_compeition_level_a_count);
		tv_second_compeition_level_b_count = (TextView) view.findViewById(R.id.tv_second_compeition_level_b_count);
		tv_second_compeition_level_c_count = (TextView) view.findViewById(R.id.tv_second_compeition_level_c_count);
		tv_total_score_result = (TextView) view.findViewById(R.id.tv_total_score_result);
	}

	public void bindData() {
		// 绑定基本信息
		try {
			tv_name.setText(GlobalData.curStudent.RealName);
			tv_sex.setText(UtilKeyValue.GetSex(GlobalData.curStudent.Sex));
			tv_test_no.setText(GlobalData.curStudent.ConfirmNo);
			tv_birthday.setText(GlobalData.curStudent.Birthday);
			tv_ethnic.setText(UtilKeyValue.GetNation(GlobalData.curStudent.Nation));
			tv_junior_high_school.setText(GlobalData.curStudent.SchoolName);
			tv_address.setText(GlobalData.curStudent.HomeAddress);
			tv_mobile.setText(GlobalData.curStudent.MobilePhone);
			tv_tel.setText(GlobalData.curStudent.HomePhone);
			tv_duty_in_class.setText(GlobalData.curStudent.Job);
			tv_info_source.setText(
					(GlobalData.curStudent.FromSchool ? "学校 " : "") + (GlobalData.curStudent.FromFamily ? "亲朋告知 " : "")
							+ (GlobalData.curStudent.FromMedia ? "媒体 " : ""));
			tv_awards.setText(GlobalData.curStudent.Award);
			tv_art_speciality.setText(GlobalData.curStudent.Skill);
			tv_height.setText(GlobalData.curStudent.Height + "");
			tv_weight.setText(GlobalData.curStudent.Weight + "");
			tv_recorder_height_weight.setText("");
			tv_sight_left.setText(GlobalData.curStudent.LeftSight);
			tv_sight_right.setText(GlobalData.curStudent.RightSight);
			
			tv_recorder_sight.setText(GlobalData.curStudent.SightTeacherName);
			if (GlobalData.curStudent.ColorWeakness != 0) {
				tv_weak_ness_result.setText(UtilKeyValue.GetColorWeakness(GlobalData.curStudent.ColorWeakness));
			}
			
			if (GlobalData.curStudent.MandarinScore != 0) {
				tv_chinese_result.setText(UtilKeyValue.GetMandarin(GlobalData.curStudent.MandarinScore));
			}
			
			if (GlobalData.curStudent.OracyScore != 0) {
				tv_english_result.setText(UtilKeyValue.GetOracy(GlobalData.curStudent.OracyScore));
			}
			
			tv_recorder_chinese.setText(GlobalData.curStudent.MandarinTeacherName);
			tv_recorder_english.setText(GlobalData.curStudent.OracyTeacherName);
			tv_first_compeition_qualied_count.setText(GlobalData.curStudent.ScorePass);
			tv_first_compeition_unqualied_count.setText(GlobalData.curStudent.ScoreBack);
			tv_second_compeition_level_a_count.setText(GlobalData.curStudent.ScoreA);
			tv_second_compeition_level_b_count.setText(GlobalData.curStudent.ScoreB);
			tv_second_compeition_level_c_count.setText(GlobalData.curStudent.ScoreC);
			tv_total_score_result.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// bindData();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
