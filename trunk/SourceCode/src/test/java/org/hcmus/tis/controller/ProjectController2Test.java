package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.repository.IterationRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.WorkItemStatusRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.Model;
@RunWith(PowerMockRunner.class)
public class ProjectController2Test {
	@Mock
	private SearchConditionsDTO searchCondition;
	@Mock
	Model uiModel;
	@Mock
	Project project;
	@Mock
	MemberInformation owner ;
	@Mock
	ProjectProcess projectProcess;
	@Mock
	WorkItemStatusRepository workItemStatusRepository;
	@Mock
	MemberInformationRepository memberInformationRepository;
	@Mock
	IterationRepository iterationRepository;
	private ProjectController aut;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		doReturn((long)1).when(project).getId();
		doReturn(projectProcess).when(project).getProjectProcess();
		doReturn((long)1).when(owner).getId();
		doReturn(owner).when(searchCondition).getOwner();
		aut = new ProjectController();
		aut.setWorkItemStatusRepository(workItemStatusRepository);
		aut.setMemberInformationRepository(memberInformationRepository);
		aut.setIterationRepository(iterationRepository);

	}
	@Test
	@PrepareForTest({Project.class, MemberInformation.class, Iteration.class, WorkItemStatus.class})
	public void testAdvancedSearch() {
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(project.getId())).thenReturn(project);
		PowerMockito.mockStatic(WorkItemStatus.class);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(Iteration.class);
		String result =  aut.advancedSearch(project.getId(), uiModel, searchCondition);
		assertEquals("projects/advancedtasks", result);
	}

}
