package com.weixiao.frame.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.weixiao.frame.base.ConstantDict;

public class BaseDataUtil {

	private BaseDataUtil() {

	}

	public final static Set<String> FIELD_CONSTANT_SET = new HashSet<String>();//需要操作的字段名称
	
	static{
		FIELD_CONSTANT_SET.add("areaPlatMark");
		FIELD_CONSTANT_SET.add("userType");
		FIELD_CONSTANT_SET.add("schoolPlatMark");
		FIELD_CONSTANT_SET.add("platMark");
		FIELD_CONSTANT_SET.add("grade");
	}

	/**
	 * 对引用base_table开头的表赋予对应的字段名称 引用规则: 字段必须命名tableId(exclude base_ ,e.g areaPlatMark)
	 * 字段必须含有tableName(exclude base_,e.g areaName)
	 * 
	 * @param list
	 */
	public synchronized static void setBaseFieldName(List<Object> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (Object object : list) {
			setFieldName(object);
		}
	}

	/**
	 * 对引用base_table开头的表赋予对应的字段名称 引用规则: 字段必须命名tableId(exclude base_ ,e.g areaPlatMark)
	 * 字段必须含有tableName(exclude base_,e.g areaName)
	 * @param object
	 */
	public static void setFieldName(Object object) {
		if (object == null)
			return;
		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			for (Field f : fields) {
				if (f.getName().endsWith("Id")) {
					f.setAccessible(true);
				
					
					if (f.getName().contains("subjectId")) {
//						BaseSubject obj = BaseDataConstant.BASE_SUBJECT_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("subjectName", obj.getSubjectName());
//						}
						continue;
					} else if (f.getName().contains("teacherTitleId")) {
//						BaseTeacherTitle obj = BaseDataConstant.BASE_TEACHERTITLE_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("teacherTitleName", obj.getName());
//						}
						continue;
					} else if (f.getName().contains("transportModeId")) {
//						BaseTransportMode obj = BaseDataConstant.BASE_TRANSPORMODE_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("transportModeName", obj.getName());
//						}
						continue;
					} else if (f.getName().contains("versionId")) {
//						BaseVersion obj = BaseDataConstant.BASE_VERSION_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("versionName", obj.getVersionName());
//						}
						continue;
					} else if (f.getName().contains("roleId")) {
//						AuthRole obj = BaseDataConstant.AUTH_ROLE_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("userTypeName", obj.getRoleName());
//						}
						continue;
					} else if (f.getName().contains("chapterId")) {
//						Chapter obj = BaseDataConstant.BASE_CHAPTER_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("chapterName", obj.getChapterName());
//						}
						continue;
					} else if (f.getName().contains("sectionId")) {
//						Section obj = BaseDataConstant.BASE_SECTION_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("sectionName", obj.getSectionName());
//						}
						continue;
					} else if (f.getName().contains("knowledgeId")) {
//						Knowledge obj = BaseDataConstant.BASE_KNOWLEDGE_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("knowledgeName", obj.getKnowledgeName());
//						}
						continue;
					} else if (f.getName().contains("semesterId")) {
//						String obj = BaseDataConstant.BASE_SEMESTER_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("semesterName", obj);
//						}
						continue;
					} else if (f.getName().contains("learningPhaseId")) {
//						String obj = BaseDataConstant.BASE_LEARNINGPHASE_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("learningPhaseName", obj);
//						}
						continue;
					}
				}else if (FIELD_CONSTANT_SET.contains(f.getName())) {
					f.setAccessible(true);
					if (f.getName().equals("grade")) {
						
						continue;
					} else 	if (f.getName().contains("userType")) {
//						AuthRole obj = BaseDataConstant.AUTH_ROLE_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("userTypeName", obj.getRoleName());
//						}
						continue;
					} else if (f.getName().contains("schoolPlatMark")) {
//						School obj = BaseDataConstant.SCHOOL_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("schoolName", obj.getSchoolName());
//						}
						continue;
					} else if (f.getName().contains("platMark")) {
//						School obj = BaseDataConstant.SCHOOL_MAP.get(f.get(object));
//						if (obj != null) {
//							map.put("platMarkName", obj.getSchoolName());
//						}
						continue;
					} else if (f.getName().contains("semester")) {
//						if(f.get(object) != null){
//							String obj = BaseDataConstant.BASE_SEMESTER_MAP.get(f.get(object));
//							if (obj != null) {
//								map.put("semesterName", obj);
//							}
//						}
						continue;
					}
				}
			}
			for (Field f : fields) {
				if (map.containsKey(f.getName())) {
					f.setAccessible(true);
					f.set(object, map.get(f.getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
