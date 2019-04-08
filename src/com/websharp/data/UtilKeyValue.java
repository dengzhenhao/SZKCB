package com.websharp.data;

import java.util.ArrayList;

import com.websharp.entity.EntityKeyValue;

public class UtilKeyValue {

	public static ArrayList<EntityKeyValue> listNation = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listSex = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listArea = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listMandarin = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listOracy = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listGeneral = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listRetrial = new ArrayList<EntityKeyValue>();
	public static ArrayList<EntityKeyValue> listColorWeakNess = new ArrayList<EntityKeyValue>();

	public static String GetNation(int value) {
		for (int i = 0; i < listNation.size(); i++) {
			if (listNation.get(i).Value == value) {
				return listNation.get(i).Text;
			}
		}
		return "-";
	}

	public static String GetSex(int value) {
		for (int i = 0; i < listSex.size(); i++) {
			if (listSex.get(i).Value == value) {
				return listSex.get(i).Text;
			}
		}
		return "-";
	}

	public static String GetArea(int value) {
		for (int i = 0; i < listArea.size(); i++) {
			if (listArea.get(i).Value == value) {
				return listArea.get(i).Text;
			}
		}
		return "-";
	}

	public static String GetColorWeakness(int value) {
		for (int i = 0; i < listColorWeakNess.size(); i++) {
			if (listColorWeakNess.get(i).Value == value) {
				return listColorWeakNess.get(i).Text;
			}
		}
		return "-";
	}

	public static String GetMandarin(int value) {
		for (int i = 0; i < listMandarin.size(); i++) {
			if (listMandarin.get(i).Value == value) {
				return listMandarin.get(i).Text;
			}
		}
		return "-";
	}

	public static String GetOracy(int value) {
		for (int i = 0; i < listOracy.size(); i++) {
			if (listOracy.get(i).Value == value) {
				return listOracy.get(i).Text;
			}
		}
		return "-";
	}

	/**
	 * 初试
	 * 
	 * @param value
	 * @return
	 */
	public static String GetGeneral(int value) {
		for (int i = 0; i < listGeneral.size(); i++) {
			if (listGeneral.get(i).Value == value) {
				return listGeneral.get(i).Text;
			}
		}
		return "-";
	}

	/**
	 * 复试
	 * 
	 * @param value
	 * @return
	 */
	public static String GetRetrial(int value) {
		for (int i = 0; i < listRetrial.size(); i++) {
			if (listRetrial.get(i).Value == value) {
				return listRetrial.get(i).Text;
			}
		}
		return "-";
	}
}
