package org.hcmus.tis.model.unittest;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class IterationTest {
	@Mock
	private Project project;
	@Mock
	private Iteration parentIteration;
	@Mock
	private Project subProject;
	@Mock 
	Iteration subIteration;
	@Mock
	private Iteration iteration;
	
	private Iteration aut;
	Collection<WorkItemContainer> projectChildren;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		doReturn((long)1).when(iteration).getId();
		doReturn((long)2).when(parentIteration).getId();
		doReturn((long)2).when(subIteration).getId();
		doReturn((long)4).when(project).getId();
		doReturn((long)5).when(subIteration).getId();
		 projectChildren = new HashSet<WorkItemContainer>();
		projectChildren.add(iteration);
		projectChildren.add(parentIteration);
		projectChildren.add(subProject);
		Collection<WorkItemContainer> parentChildren = new HashSet<WorkItemContainer>();
		parentChildren.add(subIteration);
		Collection<WorkItemContainer> childChildren = new HashSet<WorkItemContainer>();
		doReturn(projectChildren).when(project).getChildren();
		doReturn(parentChildren).when(parentIteration).getChildren();
		doReturn(childChildren).when(subIteration).getChildren();
		doReturn(childChildren).when(iteration).getChildren();
	}
	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testGetdescendantIterations(){
		Collection<Iteration> result = Iteration.getdescendantIterations(project);
		assertEquals(3, result.size());
		assertTrue(result.contains(iteration));
		assertTrue(result.contains(parentIteration));
		assertTrue(result.contains(subIteration));
	}

}
