package org.hcmus.tis.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hibernate.jdbc.Work;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.sun.mail.imap.protocol.Status;

@RooIntegrationTest(entity = WorkItem.class)
public class WorkItemIntegrationTest {
	@Autowired
	IterationDataOnDemand iterationDataOnDemand;
	@Autowired
	ProjectDataOnDemand projectDataOnDemand;
	@Autowired
	WorkItemDataOnDemand workItemDataOnDemand;
	@Autowired
	MemberInformationDataOnDemand memberInformationDataOnDemand;
	@Autowired
	WorkItemStatusDataOnDemand workItemStatusDataOnDemand;

	@Test
	public void testMarkerMethod() {
	}

	@Test
	public void testCountWorkItemByProject() {

		WorkItem workItem1 = workItemDataOnDemand.getSpecificWorkItem(0);
		WorkItem workItem2 = workItemDataOnDemand.getSpecificWorkItem(1);
		Project project = new Project();
		projectDataOnDemand.setName(project, 11);
		projectDataOnDemand.setProjectProcess(project, 11);
		project.persist();
		Iteration iteration = new Iteration();
		iterationDataOnDemand.setName(iteration, 11);
		iteration.setParentContainer(project);
		iteration.persist();
		workItem1.setWorkItemContainer(project);
		workItem2.setWorkItemContainer(iteration);
		Assert.assertEquals(2, WorkItem.countWorkItemByProject(project));
	}

	@Test
	public void testFindWorkItemsByProject() {
		WorkItem workItem1 = workItemDataOnDemand.getSpecificWorkItem(0);
		WorkItem workItem2 = workItemDataOnDemand.getSpecificWorkItem(1);
		Project project = new Project();
		projectDataOnDemand.setName(project, 11);
		projectDataOnDemand.setProjectProcess(project, 11);
		project.persist();
		Iteration iteration = new Iteration();
		iterationDataOnDemand.setName(iteration, 11);
		iteration.setParentContainer(project);
		iteration.persist();
		workItem1.setWorkItemContainer(project);
		workItem2.setWorkItemContainer(iteration);
		Collection<WorkItem> result = WorkItem.findWorkItemsByProject(project).getResultList();
		Assert.assertEquals(2, result.size());
	}
	@Test
	public void testGetTotalRecord1(){
		Project project = projectDataOnDemand.getNewTransientProject(11);
		project.persist();
		WorkItem workItem = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem.setWorkItemContainer(project);
		workItem.setTitle("title");
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setWorkItemContainer(project);
		workItem2.setTitle("abc");
		workItem2.persist();
		WorkItem workItem3 = workItemDataOnDemand.getNewTransientWorkItem(13);
		workItem3.setWorkItemContainer(projectDataOnDemand.getSpecificProject(0));
		workItem3.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setTitleDescription("tit");
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testGetTotalRecord2(){
		Project project = projectDataOnDemand.getNewTransientProject(11);
		project.persist();
		WorkItem workItem = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem.setWorkItemContainer(project);
		workItem.setTitle("title");
		workItem.setDescription("kkkkk");
		workItem.persist();
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setWorkItemContainer(project);
		workItem2.setTitle("abc");
		workItem2.persist();
		WorkItem workItem3 = workItemDataOnDemand.getNewTransientWorkItem(13);
		workItem3.setWorkItemContainer(projectDataOnDemand.getSpecificProject(0));
		workItem3.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setTitleDescription("kkk");
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testGetTotalRecordWithAsignee(){
		Project project = projectDataOnDemand.getNewTransientProject(11);
		project.persist();
		WorkItem workItem = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem.setWorkItemContainer(project);
		workItem.setAsignee(memberInformationDataOnDemand.getSpecificMemberInformation(0));
		workItem.persist();
		WorkItem nonIncluedWorkItem =  workItemDataOnDemand.getNewTransientWorkItem(12);
		nonIncluedWorkItem.setWorkItemContainer(project);
		nonIncluedWorkItem.setAsignee(memberInformationDataOnDemand.getSpecificMemberInformation(1));
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setTitleDescription("");
		searchCondition.setAsignee(memberInformationDataOnDemand.getSpecificMemberInformation(0));
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testGetTotalRecordWithNoContainer(){
		workItemDataOnDemand.init();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertTrue(totalRecord > 1);
	}
	@Test
	public void testGetTotalRecordWithContainer(){
		Project project = projectDataOnDemand.getNewTransientProject(11);
		project.persist();
		project.setChildren(new HashSet<WorkItemContainer>());
		WorkItem workItem = workItemDataOnDemand.getNewTransientWorkItem(11);
		workItem.setWorkItemContainer(project);
		workItem.persist();
		WorkItem nonIncludedWorkItem = workItemDataOnDemand.getNewTransientWorkItem(12);
		nonIncludedWorkItem.setWorkItemContainer(projectDataOnDemand.getSpecificProject(0));
		nonIncludedWorkItem.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setContainer(project);
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testGetTotalRecordWithOwner(){
		MemberInformation member1 = memberInformationDataOnDemand.getNewTransientMemberInformation(11);
		MemberInformation member2 = memberInformationDataOnDemand.getNewTransientMemberInformation(12);
		//member1.persist();
		//member2.persist();
		WorkItem workItem1 = workItemDataOnDemand.getNewTransientWorkItem(11);
		workItem1.setAuthor(member1);
		workItem1.persist();
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setAuthor(member2);
		workItem2.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setOwner(member1);
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testGetTotalRecordWithStatus(){
		WorkItemStatus status1 = workItemStatusDataOnDemand.getNewTransientWorkItemStatus(11);
		WorkItemStatus status2 = workItemStatusDataOnDemand.getNewTransientWorkItemStatus(12);
		//status1.persist();
		//status2.persist();
		WorkItem workItem1 = workItemDataOnDemand.getNewTransientWorkItem(11);
		workItem1.setStatus(status1);
		workItem1.persist();
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setStatus(status2);
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setStatus(status1);
		long totalRecord = WorkItem.getTotalRecord(searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testGetTotalRecordWithFilterAndTitleDescription(){
		WorkItem workItem = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem.setTitle("aaaa");
		workItem.setDescription("bbbb");
		workItem.persist();
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setTitle("aaa");
		workItem2.persist();
		WorkItem workItem3 = workItemDataOnDemand.getNewTransientWorkItem(13);
		workItem3.setWorkItemContainer(projectDataOnDemand.getSpecificProject(0));
		workItem3.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setTitleDescription("aa");
		String filter = "bb";
		long totalRecord = WorkItem.getFilteredRecord(filter, searchCondition);
		Assert.assertEquals(1, totalRecord);
	}
	@Test
	public void testAdvancedSearchWorkItem(){
		WorkItem workItem = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem.setTitle("aaaa");
		workItem.persist();
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setTitle("aaa");
		workItem2.persist();
		WorkItem workItem3 = workItemDataOnDemand.getNewTransientWorkItem(13);
		workItem3.setWorkItemContainer(projectDataOnDemand.getSpecificProject(0));
		workItem3.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setTitleDescription("aa");
		Collection<WorkItem>  result = WorkItem.findWorkItem( null, searchCondition, 1, 1);
		Assert.assertEquals(1, result.size());
	}
	@Test
	public void testSearchWorkItemWithTreeIteration(){
		Iteration parentIte = iterationDataOnDemand.getNewTransientIteration(11);
		parentIte.persist();
		Iteration subIte = iterationDataOnDemand.getNewTransientIteration(12);
		subIte.setParentContainer(parentIte);
		subIte.persist();
		parentIte.setChildren(new HashSet<WorkItemContainer>());
		parentIte.getChildren().add(subIte);
		WorkItem workItem1 = workItemDataOnDemand.getNewTransientWorkItem(11);
		workItem1.setWorkItemContainer(parentIte);
		workItem1.persist();
		WorkItem workItem2 = workItemDataOnDemand.getNewTransientWorkItem(12);
		workItem2.setWorkItemContainer(subIte);
		workItem2.persist();
		Iteration subSubIte = iterationDataOnDemand.getNewTransientIteration(13);
		subSubIte.setParentContainer(subIte);
		subSubIte.persist();
		subSubIte.setChildren(new HashSet<WorkItemContainer>());
		subIte.setChildren(new HashSet<WorkItemContainer>());
		subIte.getChildren().add(subSubIte);
		WorkItem workItem3 = workItemDataOnDemand.getNewTransientWorkItem(13);
		workItem3.setWorkItemContainer(subSubIte);
		workItem3.persist();
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setContainer(parentIte);
		
		long result = WorkItem.getTotalRecord(searchCondition);
		
		Assert.assertEquals(3, result);
	}
	@Test
	public void testRemove() {
	}
}
