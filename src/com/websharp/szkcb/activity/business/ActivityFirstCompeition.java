package com.websharp.szkcb.activity.business;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.entity.EntityGroup;
import com.websharp.entity.EntitySchedule;
import com.websharp.entity.EntityStudent;
import com.websharp.entity.EntityStudentGroup;
import com.websharp.entity.EntityStudentScore;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.szkcb.R;
import com.websharp.szkcb.activity.BaseActivity;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityFirstCompeition extends BaseActivity {

	private ImageView iv_menu_type;
	private TextView tv_menu_type;
	private LinearLayout layout_back;
	private LinearLayout layout_refresh_group;
	private LinearLayout layout_refresh;
	private LinearLayout layout_group;
	private LinearLayout layout_save;
	private GridView gv_group_student;
	private TextView tv_group;
	AdapterGroupStudent adapterGroupStudent = null;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_refresh:
			bindGroup();
			break;
		case R.id.layout_save:
			JSONArray jArr = new JSONArray();
			try {
				for (int i = 0; i < GlobalData.listStudentFirstCompetitionGroup.size(); i++) {
					if (GlobalData.listStudentFirstCompetitionGroup.get(i).Score.isEmpty()) {
						Util.createToast(this,
								"学生 " + GlobalData.listStudentFirstCompetitionGroup.get(i).RealName + " 未评分,不能提交", 3000)
								.show();
						return;
					}
					JSONObject jObj = new JSONObject();
					jObj.put("StudentID", GlobalData.listStudentFirstCompetitionGroup.get(i).ID);
					jObj.put("Score", GlobalData.listStudentFirstCompetitionGroup.get(i).Score + "");
					jObj.put("Desc", "");
					jArr.put(jObj);
				}
				submitScore(jArr.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_first_competition);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		iv_menu_type = (ImageView) findViewById(R.id.iv_menu_type);
		tv_menu_type = (TextView) findViewById(R.id.tv_menu_type);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_refresh_group = (LinearLayout) findViewById(R.id.layout_refresh_group);
		layout_refresh = (LinearLayout) findViewById(R.id.layout_refresh);
		layout_group = (LinearLayout) findViewById(R.id.layout_group);
		layout_save = (LinearLayout) findViewById(R.id.layout_save);
		gv_group_student = (GridView) findViewById(R.id.gv_group_student);
		tv_group = (TextView) findViewById(R.id.tv_group);
		layout_refresh.setOnClickListener(this);
		layout_save.setOnClickListener(this);
		layout_back.setOnClickListener(this);

	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		iv_menu_type.setImageResource(R.drawable.icon_first_competition);
		tv_menu_type.setText("初试");
	}

	AsyncHttpCallBack cbGetGroup = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);

			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Gson gson = new Gson();
					GlobalData.curFirstCompetitionGroup = gson.fromJson(obj.getJSONObject("data").getString("Group"),
							EntityGroup.class);
					tv_group.setText("批次:" + GlobalData.curFirstCompetitionGroup.InterviewName + "  分组名称:"
							+ GlobalData.curFirstCompetitionGroup.GroupName);
					GlobalData.listStudentFirstCompetitionGroup = gson.fromJson(
							obj.getJSONObject("data").getString("Students"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityStudent>>() {
							}.getType());
					adapterGroupStudent = new AdapterGroupStudent(GlobalData.listStudentFirstCompetitionGroup);
					gv_group_student.setAdapter(adapterGroupStudent);
					adapterGroupStudent.notifyDataSetChanged();
					layout_refresh_group.setVisibility(View.GONE);
					layout_group.setVisibility(View.VISIBLE); 
				} else {
					Util.createToast(ActivityFirstCompeition.this, obj.optString("message"), 3000).show();
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

	public void bindGroup() {
		// 绑定基本信息
		try {
			new SwzfHttpHandler(cbGetGroup, this).getGroup("0","1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	AsyncHttpCallBack cbSubmitScore = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);

			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				Util.createToast(ActivityFirstCompeition.this, obj.optString("message"), 3000).show();
				if (obj.optString("code").equals("0")) {
					//finish();
					GlobalData.curFirstCompetitionGroup = null;
					GlobalData.listStudentFirstCompetitionGroup.clear();
					layout_refresh_group.setVisibility(View.VISIBLE);
					layout_group.setVisibility(View.GONE); 
					adapterGroupStudent.notifyDataSetChanged();
					
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

	public void submitScore(String scoreData) {
		// 绑定基本信息
		try {
			new SwzfHttpHandler(cbSubmitScore, this).submitScore(GlobalData.curFirstCompetitionGroup.GroupName,
					scoreData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ViewHolderSchedule {
		private ImageView iv_tag_checked;
		private TextView tv_name;
		private TextView tv_seq;
		private TextView tv_height;
		private TextView tv_weight;
		private TextView tv_sight_left;
		private TextView tv_sight_right;
		private TextView tv_chinese_result;
		private TextView tv_english_result;
		private Button btn_pass;
		private Button btn_failed;
	}

	class AdapterGroupStudent extends BaseAdapter {
		ArrayList<EntityStudent> mList;
		private LayoutInflater mInflater;

		public AdapterGroupStudent(ArrayList<EntityStudent> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getCount() == 0)
				return null;
			ViewHolderSchedule holder = null;

			if (mInflater == null) {
				mInflater = LayoutInflater.from(ActivityFirstCompeition.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_first_competition, null);
				holder = new ViewHolderSchedule();
				holder.iv_tag_checked = (ImageView) convertView.findViewById(R.id.iv_tag_checked);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_seq = (TextView) convertView.findViewById(R.id.tv_seq);
				holder.tv_height = (TextView) convertView.findViewById(R.id.tv_height);
				holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
				holder.tv_sight_left = (TextView) convertView.findViewById(R.id.tv_sight_left);
				holder.tv_sight_right = (TextView) convertView.findViewById(R.id.tv_sight_right);
				holder.tv_chinese_result = (TextView) convertView.findViewById(R.id.tv_chinese_result);
				holder.tv_english_result = (TextView) convertView.findViewById(R.id.tv_english_result);
				holder.btn_pass = (Button) convertView.findViewById(R.id.btn_pass);
				holder.btn_failed = (Button) convertView.findViewById(R.id.btn_failed);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderSchedule) convertView.getTag();
			}

			holder.tv_name.setText(mList.get(position).RealName);
			holder.tv_seq.setText("No." + (position + 1));
			holder.tv_height.setText("身高: " + mList.get(position).Height + "cm");
			holder.tv_weight.setText("体重: " + mList.get(position).Weight + "kg");
			holder.tv_sight_left.setText("视力左: " + mList.get(position).LeftSight);
			holder.tv_sight_right.setText("视力右: " + mList.get(position).RightSight);
			holder.tv_chinese_result.setText("普通话: " + UtilKeyValue.GetMandarin(mList.get(position).MandarinScore));
			holder.tv_english_result.setText("英语口语: " + UtilKeyValue.GetOracy(mList.get(position).OracyScore));
			holder.btn_pass.setTag(position);
			holder.btn_failed.setTag(position);

			holder.btn_pass.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					View child = gv_group_student.getChildAt(index);
					ImageView iv_checked = (ImageView) child.findViewById(R.id.iv_tag_checked);
					Button btn_pass = (Button) child.findViewById(R.id.btn_pass);
					Button btn_failed = (Button) child.findViewById(R.id.btn_failed);
					btn_pass.setBackgroundResource(R.drawable.border_input_059500);
					btn_pass.setTextColor(getResources().getColor(R.color.white));
					btn_failed.setBackgroundResource(R.drawable.border_input);
					btn_failed.setTextColor(getResources().getColor(R.color.color_border));
					iv_checked.setBackgroundColor(getResources().getColor(R.color.color_list_head));
					GlobalData.listStudentFirstCompetitionGroup.get(index).Score = "合格";
				}
			});

			holder.btn_failed.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					View child = gv_group_student.getChildAt(index);
					ImageView iv_checked = (ImageView) child.findViewById(R.id.iv_tag_checked);
					Button btn_pass = (Button) child.findViewById(R.id.btn_pass);
					Button btn_failed = (Button) child.findViewById(R.id.btn_failed);
					btn_failed.setBackgroundResource(R.drawable.border_input_aaaaaa);
					btn_failed.setTextColor(getResources().getColor(R.color.white));
					btn_pass.setBackgroundResource(R.drawable.border_input);
					btn_pass.setTextColor(getResources().getColor(R.color.color_border));
					iv_checked.setBackgroundColor(getResources().getColor(R.color.color_list_head));
					GlobalData.listStudentFirstCompetitionGroup.get(index).Score = "不合格";
				}
			});
			return convertView;
		}
	}

}
