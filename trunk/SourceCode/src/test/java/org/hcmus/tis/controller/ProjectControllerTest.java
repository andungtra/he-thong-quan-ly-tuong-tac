package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.persistence.TypedQuery;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.dto.DSRestResponse;
import org.hcmus.tis.dto.SiteMapItem;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectDataOnDemand;
import org.hcmus.tis.util.Parameter;
import org.hibernate.mapping.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Project.class)
@MockStaticEntityMethods
public class ProjectControllerTest extends AbstractShiroTest{
	private ProjectController aut;
	private Model uiModel;
	private Subject mockedSubject;
	private Session mockedSession; 
	private Account mockedLoginedAccount;
	@Before
	public void setUp(){
		aut = new ProjectController();
		uiModel = Mockito.mock(Model.class);
		PowerMockito.mockStatic(Project.class);
		
		 mockedSubject = Mockito.mock(Subject.class);
		 mockedSession = Mockito.mock(Session.class);
		 mockedLoginedAccount = Mockito.mock(Account.class);
		 Mockito.doReturn(mockedSession).when(mockedSubject).getSession();
		 Mockito.doReturn(mockedLoginedAccount).when(mockedSession).getAttribute("account");
		Mockito.doReturn(true).when(mockedSubject).isAuthenticated();
		Mockito.doNothing().when(mockedSubject).checkPermissions(Mockito.any(String[].class));
		setSubject(mockedSubject);
	}
	public void tearDown(){
		clearSubject();
	}
	@Test
	public void testFindProjectsByNameLikeWhenNameEmpty() {
		ProjectController controller = new ProjectController();
		PowerMockito.when(Project.findAllProjects()).thenReturn(null);
		controller.findProjectsQuickly("", uiModel, 1, 2);
		PowerMockito.verifyStatic();
		Project.findProjectEntries(0, 2);
		Mockito.verify(uiModel).addAttribute(Mockito.eq("parameters"), Mockito.anyCollection());
	}
	@Test
	public void testFindProjectByNameWhenNameNotNull(){
		ProjectController controller = new ProjectController();
		PowerMockito.mockStatic(Project.class);
		String name = "name";
		TypedQuery<Project> mockResult =  Mockito.mock(TypedQuery.class);
		Mockito.when(mockResult.getResultList()).thenReturn(new Vector<Project>());
		PowerMockito.when(Project.findProjectsByNameLike(Mockito.eq(name), Mockito.anyInt(), Mockito.anyInt())).thenReturn(mockResult);
		Model mockUIModel = Mockito.mock(Model.class);
		
		controller.findProjectsQuickly(name, mockUIModel, null, null);
		
		PowerMockito.verifyStatic();
		Project.findProjectsByNameLike(name, 0, 10);
		Mockito.verify(mockUIModel).addAttribute("query", name);
		Mockito.verify(mockUIModel).addAttribute(Mockito.eq("parameters"), Mockito.anyCollectionOf(Parameter.class));
	}
	@Test
	public void testListMembers(){
	
		Project mockedProject = Mockito.mock(Project.class);
		Mockito.doReturn(new HashSet<MemberInformation>()).when(mockedProject).getMemberInformations();
		PowerMockito.when(Project.findProject((long)1)).thenReturn(mockedProject);
		String result = aut.listMembers((long)1, uiModel);
		
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberinformations"), Mockito.anyCollection());
	}
	@Test
	public void testGetEvents(){
		Long id = (long)1;
		String encodedMemberIds = "";
		Project mockedProject = Mockito.mock(Project.class);
		Calendar mockedCalendar = Mockito.mock(Calendar.class);
		Mockito.doReturn(mockedCalendar).when(mockedProject).getCalendar();
		Collection<Event> events = new ArrayList<Event>();
		Event event  = new Event();
		events.add(event);
		Mockito.doReturn(events).when(mockedCalendar).getEvents();
		Collection<Event> eventsOfMember = new HashSet<Event>();
		Event mockedMemberEvent = Mockito.mock(Event.class);
		eventsOfMember.add(mockedMemberEvent);
		PowerMockito.when(Project.findProject(id)).thenReturn(mockedProject);
		Mockito.doReturn(eventsOfMember).when(mockedProject).getEventsOfMembers();
		
		DSRestResponse restResponse = aut.getEvents(id, encodedMemberIds);
		
		Mockito.verify(mockedProject).getEventsOfMembers();
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(2, restResponse.getResponse().getData().size());
	}
	@Test
	public void testCreateEvent(){
		
		Long projectId = (long)1;
		Event mockedEvent = Mockito.mock(Event.class);
		BindingResult mockedBindingResult = Mockito.mock(BindingResult.class);
		Project mockedProject = Mockito.mock(Project.class);
		Calendar mockedCalendar = Mockito.mock(Calendar.class);
		Mockito.doReturn(mockedCalendar).when(mockedProject).getCalendar();
		Collection<Event> mockedEvents = Mockito.mock(Collection.class);
		Mockito.doReturn(mockedEvents).when(mockedCalendar).getEvents();
		PowerMockito.when(Project.findProject(projectId)).thenReturn(mockedProject);
		
		DSRestResponse restResponse = aut.creatEvent(projectId, mockedEvent, mockedBindingResult);
		
		PowerMockito.verifyStatic();
		Project.findProject(projectId);
		Mockito.verify(mockedEvents).add(mockedEvent);
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(1, restResponse.getResponse().getData().size());
	}
	@Test
	public void testUpdateEvent(){
		
		Long projectId = (long)1;
		Long id = (long)2;
		BindingResult mockedBindingResult = Mockito.mock(BindingResult.class);
		Event mockedEvent = Mockito.mock(Event.class);
		Event mockedResultEvent = Mockito.mock(Event.class);
		Mockito.doReturn(mockedResultEvent).when(mockedEvent).merge();
		
		DSRestResponse restResponse = aut.updateEvent(projectId, id, mockedEvent, mockedBindingResult);
		
		Mockito.verify(mockedEvent).merge();
		Mockito.verify(mockedEvent).setId(id);
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(1, restResponse.getResponse().getData().size());
		Assert.assertTrue(restResponse.getResponse().getData().contains(mockedResultEvent));
	}
	@Test
	@PrepareForTest(Event.class)
	public void testDeleteEvent(){
		Long accountId = (long)1;
		Long id = (long)1;
		Event mockedEvent = Mockito.mock(Event.class);
		Calendar mockedCalendar = Mockito.mock(Calendar.class);
		Collection<Calendar> calendars = new ArrayList<Calendar>();
		calendars.add(mockedCalendar);
		Mockito.doReturn(calendars).when(mockedEvent).getCalendars();
		Collection<Event> events = Mockito.mock(Collection.class);
		Mockito.doReturn(events).when(mockedCalendar).getEvents();
		PowerMockito.mockStatic(Event.class);
		PowerMockito.when(Event.findEvent(id)).thenReturn(mockedEvent);
		
		DSRestResponse restResponse = aut.deleteEvent(accountId, id);
		
		Mockito.verify(events).remove(mockedEvent);
		Mockito.verify(mockedEvent).remove();
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(1, restResponse.getResponse().getData().size());
		Assert.assertTrue(restResponse.getResponse().getData().contains(mockedEvent));
		
	}
	@Test
	@PrepareForTest({Project.class})
	public void testGetRoadmap(){
		Long itemId = (long)1;
		Project mockedProject = Mockito.mock(Project.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(itemId)).thenReturn(mockedProject);
		
		String result = aut.getRoadmap(itemId, uiModel);
		Mockito.verify(uiModel).addAttribute("project", mockedProject);
		Assert.assertEquals("projects/roadmap", result);
	}
	List<SiteMapItem> siteMapItems;
	@Test
	@PrepareForTest({Project.class})
	public void testshowHomepage(){
		long id = (long)2;
		Project mockedProject = Mockito.mock(Project.class);
		Project mockedParentProject = Mockito.mock(Project.class);
		Mockito.doReturn(mockedParentProject).when(mockedProject).getParentContainer();
		Mockito.doReturn("name").when(mockedProject).getName();
		Mockito.doReturn((long)1).when(mockedProject).getId();
		Mockito.doReturn("name parent").when(mockedParentProject).getName();
		Mockito.doReturn((long)2).when(mockedParentProject).getId();
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(id)).thenReturn(mockedProject);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				siteMapItems = (List<SiteMapItem>) invocation.getArguments()[1];
				return null;
			}
		}).when(uiModel).addAttribute(Mockito.eq("siteMapItems"), Mockito.any(List.class));
		String result = aut.showhomepage(id, uiModel);
		
		PowerMockito.verifyStatic();
		Project.findProject(id);
		Assert.assertEquals("projects/homepage", result);
		Mockito.verify(uiModel).addAttribute(Mockito.eq("siteMapItems"), Mockito.any(List.class));
		Assert.assertEquals(2, siteMapItems.size());
		Assert.assertEquals("name parent", siteMapItems.get(0).getName());
		Assert.assertEquals("/projects/2", siteMapItems.get(0).getUrl());
	}
}
