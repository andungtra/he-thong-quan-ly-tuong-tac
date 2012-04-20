package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import org.aspectj.lang.annotation.After;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
@RunWith(PowerMockRunner.class)
@MockStaticEntityMethods
@PrepareForTest({WorkItemStatus.class, Priority.class, Iteration.class, MemberInformation.class, WorkItemContainer.class, WorkItemType.class})
public class WorkItemControllerTest {
	private Model uiModel;
	private WorkItemController aut;
	@Before
	public void setUp(){
		PowerMockito.mockStatic(WorkItemStatus.class);
		PowerMockito.mockStatic(Priority.class);
		PowerMockito.mockStatic(Iteration.class);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(WorkItemContainer.class);
		PowerMockito.mockStatic(WorkItemType.class);
		
		uiModel = Mockito.mock(Model.class);
		aut = new WorkItemController();
	}
	@Test
	public void testCreateForm() {
		
		aut.createForm((long)1, (long) 1,null,  uiModel);
		
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemstatuses"), Mockito.anyCollectionOf(WorkItemStatus.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("prioritys"), Mockito.anyCollectionOf(Priority.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemcontainers"), Mockito.anyCollectionOf(Iteration.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberinformations"), Mockito.anyCollectionOf(MemberInformation.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workItemTypeId"), Mockito.anyLong());
		Mockito.verify(uiModel).addAttribute(Mockito.eq("projectId"), Mockito.anyLong());
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberInformationId"), Mockito.anyLong());
	}
	@Test
	public void testPopulateEditFormCustomly(){
		
	}
}
