package org.hcmus.tis.util;

import java.util.ArrayList;
import java.util.List;

import org.hcmus.tis.dto.Page;
import org.hcmus.tis.dto.StudyClassDTO;
import org.hcmus.tis.model.StudyClass;

public class StudyClassMapper {
	public static StudyClassDTO map(StudyClass source){
		StudyClassDTO des = new StudyClassDTO();
		des.setName(source.getName());
		des.setId(source.getId());
		des.setDescription(source.getDescription());
		return des;
	}
	public static List<StudyClassDTO> map(Page<StudyClass> source){
		List<StudyClassDTO> dtos = new ArrayList<StudyClassDTO>();
		for(StudyClass studyClass : source.getElements() ){
			dtos.add(map(studyClass));
		}
		return dtos;
	}

}
