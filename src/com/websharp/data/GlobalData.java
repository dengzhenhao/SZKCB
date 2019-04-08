package com.websharp.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.websharp.entity.EntityGroup;
import com.websharp.entity.EntityInterview;
import com.websharp.entity.EntityRight;
import com.websharp.entity.EntitySchedule;
import com.websharp.entity.EntityStudent;
import com.websharp.entity.EntityUser;

public class GlobalData {

	public static void clear() {
		curUser = null;
		if (listRight != null)
			listRight.clear();
		curStudent = null;
		TICKET = null;
		if (listScheduleFirst != null)
			listScheduleFirst.clear();
		if (listInterviewFirst != null)
			listInterviewFirst.clear();
		if (listScheduleSecond != null)
			listScheduleSecond.clear();
		if (listInterviewSecond != null)
			listInterviewSecond.clear();
		if (listStudentFirstCompetitionWaitGroup != null)
			listStudentFirstCompetitionWaitGroup.clear();
		curInterviewIDFirstCompetition = 0;
		if (listStudentSecondCompetitionWaitGroup != null)
			listStudentSecondCompetitionWaitGroup.clear();
		curInterviewIDSecondCompetition = 0;
		curInterviewIDConfirm = 0;
		System.gc();
	}

	public static EntityUser curUser = null;
	public static EntityStudent curStudent = null;

	public static String TICKET = "";
	

	public static int curInterviewIDConfirm = 0;

	public static ArrayList<EntityStudent> listStudentFirstCompetitionWaitGroup = new ArrayList<EntityStudent>();
	public static int curInterviewIDFirstCompetition = 0;

	public static ArrayList<EntityStudent> listStudentSecondCompetitionWaitGroup = new ArrayList<EntityStudent>();
	public static int curInterviewIDSecondCompetition = 0;
	
	public static ArrayList<EntitySchedule> listScheduleConfirm = new ArrayList<EntitySchedule>();
	public static ArrayList<EntityInterview> listInterviewConfirm = new ArrayList<EntityInterview>();

	public static ArrayList<EntitySchedule> listScheduleFirst = new ArrayList<EntitySchedule>();
	public static ArrayList<EntityInterview> listInterviewFirst = new ArrayList<EntityInterview>();

	public static ArrayList<EntitySchedule> listScheduleSecond = new ArrayList<EntitySchedule>();
	public static ArrayList<EntityInterview> listInterviewSecond = new ArrayList<EntityInterview>();

	public static EntityGroup curFirstCompetitionGroup = null;
	public static EntityGroup curSecondCompetitionGroup = null;

	public static ArrayList<EntityStudent> listStudentFirstCompetitionGroup = new ArrayList<EntityStudent>();
	public static ArrayList<EntityStudent> listStudentSecondCompetitionGroup = new ArrayList<EntityStudent>();

	public static ArrayList<EntityRight> listRight = new ArrayList<EntityRight>();

}
