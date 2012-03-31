package org.hcmus.tis.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hcmus.tis.dto.Page;
import org.hcmus.tis.dto.PageRequest;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findStudyClassesByNameLike" })
public class StudyClass {

    @NotNull
    @Size(max = 50, min = 1)
    private String name;

    private String description;

	public static Page<StudyClass> findAll(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		Page<StudyClass> result = new Page<StudyClass>();
		result.setNumber(pageRequest.getPageNumber());
		result.setTotalElements(StudyClass.countStudyClasses());
		long elementNumber = StudyClass.countStudyClasses();
		long pageTotal = elementNumber / pageRequest.getPageSize();
		if(elementNumber % pageRequest.getPageSize() != 0){
			++pageTotal;
		}
		result.setTotalPages(pageTotal);
		int firstIndex = (pageRequest.getPageNumber() - 1) * pageRequest.getPageSize();
		result.setElements(StudyClass.findStudyClassEntries(firstIndex, pageRequest.getPageSize()));
		return result;
				
	}
}
