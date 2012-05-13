package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import javax.persistence.TypedQuery;

import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.WorkItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
@MockStaticEntityMethods
@RunWith(PowerMockRunner.class)
public class CommentControllerTest {
	private CommentController aut;
	private Model uiModel;
	@Before
	public void setUp(){
		aut = new CommentController();
		uiModel = Mockito.mock(Model.class);
	}

	@Test
	@PrepareForTest({Comment.class, WorkItem.class})
	public void testList() {
		Long workItemId = (long)1;
		Integer firstResult = 1;
		Integer maxResult = 5;
		WorkItem mockedWorkItem = Mockito.mock(WorkItem.class);
		PowerMockito.mockStatic(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(mockedWorkItem);
		PowerMockito.mockStatic(Comment.class);
		TypedQuery<Comment> mockedTypedQuery = Mockito.mock(TypedQuery.class);
		PowerMockito.when(Comment.findCommentsByWorkItem(Mockito.any(WorkItem.class))).thenReturn(mockedTypedQuery);
		String result = aut.listCommentsByWorkItem(workItemId, firstResult, maxResult, uiModel);
		Assert.assertEquals("comments/listByWorkItem", result);
		Mockito.verify(uiModel).addAttribute(Mockito.eq("comments"), Mockito.any());
		Mockito.verify(uiModel).addAttribute("workItemId", workItemId);
	}

}
