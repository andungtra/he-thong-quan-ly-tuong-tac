package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import javax.persistence.TypedQuery;

import org.hcmus.tis.model.StudyClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
@RunWith(PowerMockRunner.class)
@PrepareForTest(StudyClass.class)
@MockStaticEntityMethods
public class StudyClassControllerTest {

	@Test
	public void testFindStudyClassByNameLikeWithEmptyInput() {
		PowerMockito.mockStatic(StudyClass.class);
		PowerMockito.when(StudyClass.findAllStudyClasses()).thenReturn(null);
		Model mockUIModel = Mockito.mock(Model.class);
		StudyClassController controller = new StudyClassController();
		controller.findStudyClassesQuickly("", mockUIModel);
		PowerMockito.verifyStatic();
		StudyClass.findAllStudyClasses();
		Mockito.verify(mockUIModel).addAttribute("query", "");
		
	}
	@Test
	public void testFindStudyClassByNameLikeWithNonEmptyInput(){
		PowerMockito.mockStatic(StudyClass.class);
		TypedQuery<StudyClass> mockedResult = Mockito.mock(TypedQuery.class);
		String name= "name";
		PowerMockito.when(StudyClass.findStudyClassesByNameLike(name)).thenReturn(mockedResult);
		Model mockUIModel = Mockito.mock(Model.class);
		StudyClassController controller = new StudyClassController();
		controller.findStudyClassesQuickly(name, mockUIModel);
		PowerMockito.verifyStatic();
		StudyClass.findStudyClassesByNameLike(name);
		Mockito.verify(mockUIModel).addAttribute("query", name);
	}

}
