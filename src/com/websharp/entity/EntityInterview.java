package com.websharp.entity;

public class EntityInterview {
	public int ID = 0;
	public String InterviewName = "";
	public String StartTime = "";
	public String ExpiredTime = "";
	public int PlanCount = 0;
	public int FactCount = 0;
	public String Description = "";
	public int ScoreTimes = 0;
	public boolean Enabled = false;
	public boolean NeedHealth = false;
	public boolean NeedSight = false;
	public boolean NeedColorWeakness = false;
	public boolean NeedMandarinScore = false;
	public boolean NeedOracyScore = false;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getInterviewName() {
		return InterviewName;
	}

	public void setInterviewName(String interviewName) {
		InterviewName = interviewName;
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

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getScoreTimes() {
		return ScoreTimes;
	}

	public void setScoreTimes(int scoreTimes) {
		ScoreTimes = scoreTimes;
	}

	public boolean isEnabled() {
		return Enabled;
	}

	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}

	public boolean isNeedHealth() {
		return NeedHealth;
	}

	public void setNeedHealth(boolean needHealth) {
		NeedHealth = needHealth;
	}

	public boolean isNeedSight() {
		return NeedSight;
	}

	public void setNeedSight(boolean needSight) {
		NeedSight = needSight;
	}

	public boolean isNeedColorWeakness() {
		return NeedColorWeakness;
	}

	public void setNeedColorWeakness(boolean needColorWeakness) {
		NeedColorWeakness = needColorWeakness;
	}

	public boolean isNeedMandarinScore() {
		return NeedMandarinScore;
	}

	public void setNeedMandarinScore(boolean needMandarinScore) {
		NeedMandarinScore = needMandarinScore;
	}

	public boolean isNeedOracyScore() {
		return NeedOracyScore;
	}

	public void setNeedOracyScore(boolean needOracyScore) {
		NeedOracyScore = needOracyScore;
	}

	
	
}
