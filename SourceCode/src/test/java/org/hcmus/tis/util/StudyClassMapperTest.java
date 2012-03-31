package org.hcmus.tis.util;

import static org.junit.Assert.*;

import java.util.List;

import org.hcmus.tis.dto.Page;
import org.hcmus.tis.dto.StudyClassDTO;
import org.hcmus.tis.model.StudyClass;
import org.junit.Assert;
import org.junit.Test;

public class StudyClassMapperTest {

	@Test
	public void testMapStudyClassToStudyClassDTO() {
		StudyClass source = new StudyClass();
		source.setId((long)1);
		source.setName("name");
		source.setDescription("des");
		StudyClassDTO expectedDes = new StudyClassDTO();
		expectedDes.setId((long)1);
		expectedDes.setName("name");
		expectedDes.setDescription("des");
		StudyClassMapper aut = new StudyClassMapper();
		StudyClassDTO actualDes = aut.map(source);
		Assert.assertEquals("map method for 'StudyClassMapper' return wrong value", expectedDes.getId(), actualDes.getId());
		Assert.assertEquals("map method for 'StudyClassMapper' return wrong value", expectedDes.getName(), actualDes.getName());
		Assert.assertEquals("map method for 'StudyClassMapper' return wrong value", expectedDes.getDescription(), actualDes.getDescription());
	}
	@Test
	public void testPageToStudyClassDTOs() {
		Page<StudyClass> source = new Page<StudyClass>();
		for(int index = 0; index < 2; ++index)
		{
			StudyClass studyClass = new StudyClass();
			studyClass.setId((long)index);
			studyClass.setName("name_" +String.valueOf(index));
			studyClass.setDescription("description_" + String.valueOf(index));
		}
		StudyClassMapper aut = new StudyClassMapper();
		List<StudyClassDTO> des = aut.map(source);
		Assert.assertEquals(source.getElements().size(), des.size());
		for(int index = 0; index <  source.getElements().size(); ++index)
		{
			Assert.assertEquals((long)source.getElements().get(index).getId(), des.get(index).getId());
			Assert.assertEquals(source.getElements().get(index).getName(), des.get(index).getName());
			Assert.assertEquals(source.getElements().get(index).getDescription(), des.get(index).getDescription());
		}
	}

}
