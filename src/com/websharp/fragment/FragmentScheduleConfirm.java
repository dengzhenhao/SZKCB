package com.websharp.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.data.UtilKeyValue;
import com.websharp.entity.EntityInterview;
import com.websharp.entity.EntitySchedule;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.szkcb.R;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentScheduleConfirm extends Fragment {
	GridView gv_schedule;
	GridView gv_interview;
	public String groupType = "";
	AdapterSchedule adapterSchedule = null;
	AdapterInterview adapterInterview = null;
	int curScheduleID = 0;

	// 2.1 定义用来与外部activity交互，获取到宿主activity
	private FragmentInteraction listerner;

	// 1 定义了所有activity必须实现的接口方法
	public interface FragmentInteraction {
		void process(int curInterviewID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_schedule, container, false);
		init(view);

		return view;

	}

	private void init(View view) {
		gv_schedule = (GridView) view.findViewById(R.id.gv_schedule);
		gv_interview = (GridView) view.findViewById(R.id.gv_interview);
		gv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				for (int i = 0; i < gv_schedule.getChildCount(); i++) {
					View convertView = gv_schedule.getChildAt(i);
					LinearLayout layout_bg_schedule = (LinearLayout) convertView.findViewById(R.id.layout_bg_schedule);
					TextView tv_schedule_name = (TextView) convertView.findViewById(R.id.tv_schedule_name);
					if (position == i) {
						layout_bg_schedule.setBackgroundResource(R.drawable.border_input_620000_2);
						tv_schedule_name.setTextColor(getResources().getColor(R.color.white));
						curScheduleID = adapterSchedule.mList.get(i).ID;
					} else {
						layout_bg_schedule.setBackgroundResource(R.drawable.border_input_620000);
						tv_schedule_name.setTextColor(getResources().getColor(R.color.color_list_head));
					}
				}
				bindDataInterview();
			}
		});

		gv_interview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

				// 判断是不是已经过期，或者实际签到人数大于等于计划报名人数了
				int time = 0;
				try {
					time = new DateUtil().secondBetweenNow(adapterInterview.mList.get(position).ExpiredTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (time >= 0) {
					if (adapterInterview.mList.get(position).FactCount < adapterInterview.mList
							.get(position).PlanCount) {
						for (int i = 0; i < gv_interview.getChildCount(); i++) {
							View convertView = gv_interview.getChildAt(i);
							LinearLayout layout_bg_interview = (LinearLayout) convertView
									.findViewById(R.id.layout_bg_interview);
							TextView tv_interview_name = (TextView) convertView.findViewById(R.id.tv_interview_name);
							if (position == i) {
								layout_bg_interview.setBackgroundResource(R.drawable.border_input_620000_2);
								tv_interview_name.setTextColor(getResources().getColor(R.color.white));
								if (listerner != null) {
									listerner.process(adapterInterview.mList.get(i).ID);
								}
							} else {
								layout_bg_interview.setBackgroundResource(R.drawable.border_input_620000);
								tv_interview_name.setTextColor(getResources().getColor(R.color.color_list_head));
							}
						}
					}else{
						Util.createToast(getActivity(), "您选的考试场次 面试人数已满 ,请选择其它场次", Toast.LENGTH_LONG).show();
					}
				} else {
					Util.createToast(getActivity(), "您选的考试场次时间已过期,请选择其它场次", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	AsyncHttpCallBack cbSchedule = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);

			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Gson gson = new Gson();

					GlobalData.listScheduleConfirm = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntitySchedule>>() {
							}.getType());
					adapterSchedule = new AdapterSchedule(GlobalData.listScheduleConfirm);
					gv_schedule.setAdapter(adapterSchedule);
					adapterSchedule.notifyDataSetChanged();
				} else {
					Util.createToast(getActivity(), obj.optString("message"), 3000).show();
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

	public void bindDataSchedule() {
		// 绑定基本信息
		try {
			if (adapterSchedule != null) {
				adapterSchedule.mList.clear();
				adapterSchedule.notifyDataSetChanged();
			}
			if (adapterInterview != null) {
				adapterInterview.mList.clear();
				adapterInterview.notifyDataSetChanged();
			}

			new SwzfHttpHandler(cbSchedule, getActivity()).querySchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	AsyncHttpCallBack cbInterview = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);

			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Gson gson = new Gson();

					GlobalData.listInterviewConfirm = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityInterview>>() {
							}.getType());
					adapterInterview = new AdapterInterview(GlobalData.listInterviewConfirm);
					gv_interview.setAdapter(adapterInterview);
					adapterInterview.notifyDataSetChanged();
				} else {
					Util.createToast(getActivity(), obj.optString("message"), 3000).show();
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

	public void bindDataInterview() {
		// 绑定基本信息
		try {
			new SwzfHttpHandler(cbInterview, getActivity()).queryInterview(curScheduleID + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ViewHolderSchedule {
		private TextView tv_schedule_name;
	}

	class AdapterSchedule extends BaseAdapter {
		ArrayList<EntitySchedule> mList;
		private LayoutInflater mInflater;

		public AdapterSchedule(ArrayList<EntitySchedule> list) {
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
				mInflater = LayoutInflater.from(getActivity());
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_schedule, null);
				holder = new ViewHolderSchedule();
				holder.tv_schedule_name = (TextView) convertView.findViewById(R.id.tv_schedule_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderSchedule) convertView.getTag();
			}
			holder.tv_schedule_name.setText(mList.get(position).ScheduleName);
			return convertView;
		}
	}

	class ViewHolderInterview {
		private LinearLayout layout_bg_interview;
		private TextView tv_interview_name;
		private TextView tv_interview_info;
	}

	class AdapterInterview extends BaseAdapter {
		ArrayList<EntityInterview> mList;
		private LayoutInflater mInflater;

		public AdapterInterview(ArrayList<EntityInterview> list) {
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
			ViewHolderInterview holder = null;

			if (mInflater == null) {
				mInflater = LayoutInflater.from(getActivity());
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_interview, null);
				holder = new ViewHolderInterview();
				holder.layout_bg_interview = (LinearLayout)convertView.findViewById(R.id.layout_bg_interview);
				holder.tv_interview_name = (TextView) convertView.findViewById(R.id.tv_interview_name);
				holder.tv_interview_info = (TextView) convertView.findViewById(R.id.tv_interview_info);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderInterview) convertView.getTag();
			}
			
			holder.tv_interview_name.setText(mList.get(position).InterviewName);
			holder.tv_interview_info
					.setText(getString(R.string.interview_info,
							new DateUtil().TimeParseStringToFormatString(mList.get(position).StartTime, "HH:mm") + "-"
									+ new DateUtil().TimeParseStringToFormatString(mList.get(position).ExpiredTime,
											"HH:mm"),
							mList.get(position).PlanCount + "", mList.get(position).FactCount + ""));
			
			holder.layout_bg_interview.setBackgroundResource(R.drawable.border_input_interview_gray);
			holder.tv_interview_name.setTextColor(getActivity().getResources().getColor(R.color.color_border));
			holder.tv_interview_info.setTextColor(getActivity().getResources().getColor(R.color.color_border));
			
			int time = 0;
			try {
				time = new DateUtil().secondBetweenNow(mList.get(position).ExpiredTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (time >= 0) {
				if (mList.get(position).FactCount < mList
						.get(position).PlanCount) {
					holder.layout_bg_interview.setBackgroundResource(R.drawable.border_input_620000);
					holder.tv_interview_name.setTextColor(getActivity().getResources().getColor(R.color.color_list_head));
					holder.tv_interview_info.setTextColor(getActivity().getResources().getColor(R.color.color_list_head));
				}
			}
			
			return convertView;
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
		if (activity instanceof FragmentInteraction) {
			listerner = (FragmentInteraction) activity; // 2.2 获取到宿主activity并赋值
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
