package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.TypedQuery;

import org.hibernate.jdbc.Work;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = WorkItem.class)
public class WorkItemIntegrationTest {
	@Autowired
	IterationDataOnDemand iterationDataOnDemand;
	@Autowired
	ProjectDataOnDemand projectDataOnDemand;
	@Autowired
	WorkItemDataOnDemand workItemDataOnDemand;

	@Test
	public void testMarkerMethod() {
	}

	@Test
	public void testCountWorkItemByProject() {
		// Project project = projectDataOnDemand.getRandomProject();
		// Iteration iteration = iterationDataOnDemand.getRandomIteration();
		// iteration.setParentContainer(project);
		// WorkItem workItem1 = workItemDataOnDemand.getSpecificWorkItem(1);
		// workItem1.setWorkItemContainer(project);
		// WorkItem workItem2 = workItemDataOnDemand.getSpecificWorkItem(2);
		// workItem2.setWorkItemContainer(iteration);

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
	public void testRemove() {
	}
}
