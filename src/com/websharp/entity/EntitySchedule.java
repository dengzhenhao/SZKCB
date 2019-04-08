package com.websharp.entity;

public class EntitySchedule {
	public String MajorName = "";
	public String ScheduleName = "";
	public int ScheduleYear = 0;
	public String PicSrc = "";
	public int MajorID = 1;
	public String StartTime = "";
	public String ExpiredTime = "";
	public int PlanCount = 0;
	public int FactCount = 0;
	public boolean Enabled = false;
	public String Description = "";
	public int ID = 0;

	public String getMajorName() {
		return MajorName;
	}

	public void setMajorName(String majorName) {
		MajorName = majorName;
	}

	public String getScheduleName() {
		return ScheduleName;
	}

	public void setScheduleName(String scheduleName) {
		ScheduleName = scheduleName;
	}

	public int getScheduleYear() {
		return ScheduleYear;
	}

	public void setScheduleYear(int scheduleYear) {
		ScheduleYear = scheduleYear;
	}

	public String getPicSrc() {
		return PicSrc;
	}

	public void setPicSrc(String picSrc) {
		PicSrc = picSrc;
	}

	public int getMajorID() {
		return MajorID;
	}

	public void setMajorID(int majorID) {
		MajorID = majorID;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getExpiredTime() {
		return ExpiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		ExpiredTime = expiredTime;
	}

	public int getPlanCount() {
		return PlanCount;
	}

	public void setPlanCount(int planCount) {
		PlanCount = planCount;
	}

	public int getFactCount() {
		return FactCount;
	}

	public void setFactCount(int factCount) {
		FactCount = factCount;
	}

	public boolean isEnabled() {
		return Enabled;
	}

	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
