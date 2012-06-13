package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.persistence.TypedQuery;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.dto.SiteMapItem;
import org.hcmus.tis.dto.datatables.DSRestResponse;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectDataOnDemand;
import org.hcmus.tis.repository.EventRepository;
import org.hcmus.tis.util.Parameter;
import org.hibernate.mapping.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static  org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Project.class)
@MockStaticEntityMethods
public class ProjectControllerTest extends AbstractShiroTest{
	private ProjectController aut;
	@Mock
	private Model uiModel;
	@Mock
	private Subject mockedSubject;
	@Mock
	private Session mockedSession;
	@Mock
	private Account mockedLoginedAccount;
	@Mock
	private EventRepository eventRepository;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		aut = new ProjectController();
		PowerMockito.mockStatic(Project.class);
		 doReturn(mockedSession).when(mockedSubject).getSession();
		 doReturn(mockedLoginedAccount).when(mockedSession).getAttribute("account");
		doReturn(true).when(mockedSubject).isAuthenticated();
		doNothing().when(mockedSubject).checkPermissions(any(String[].class));
		setSubject(mockedSubject);
		aut.setEventRepository(eventRepository);
	}
	public void tearDown(){
		clearSubject();
	}
//	@Test
//	public void testFindProjectsByNameLikeWhenNameEmpty() {
//		ProjectController controller = new ProjectController();
//		PowerMockito.when(Project.findAllProjects()).thenReturn(null);
//		controller.findProjectsQuickly("", uiModel, 1, 2);
//		PowerMockito.verifyStatic();
//		Project.findProjectEntries(0, 2);
//		verify(uiModel).addAttribute(eq("parameters"), anyCollection());
//	}
//	@Test
//	public void testFindProjectByNameWhenNameNotNull(){
//		ProjectController controller = new ProjectController();
//		PowerMockito.mockStatic(Project.class);
//		String name = "name";
//		TypedQuery<Project> mockResult =  mock(TypedQuery.class);
//		when(mockResult.getResultList()).thenReturn(new Vector<Project>());
//		PowerMockito.when(Project.findProjectsByNameLike(eq(name), anyInt(), anyInt())).thenReturn(mockResult);
//		Model mockUIModel = mock(Model.class);
//		
//		controller.findProjectsQuickly(name, mockUIModel, null, null);
//		
//		PowerMockito.verifyStatic();
//		Project.findProjectsByNameLike(name, 0, 10);
//		verify(mockUIModel).addAttribute("query", name);
//		verify(mockUIModel).addAttribute(eq("parameters"), anyCollectionOf(Parameter.class));
//	}
	@Test
	public void testListMembers(){
	
		Project mockedProject = mock(Project.class);
		doReturn(new HashSet<MemberInformation>()).when(mockedProject).getMemberInformations();
		PowerMockito.when(Project.findProject((long)1)).thenReturn(mockedProject);
		String result = aut.listMembers((long)1, uiModel);
		
		verify(uiModel).addAttribute(eq("memberinformations"), anyCollection());
	}
	@Test
	public void testGetEvents(){
		Long id = (long)1;
		String encodedMemberIds = "";
		Project mockedProject = mock(Project.class);
		Calendar mockedCalendar = mock(Calendar.class);
		doReturn(mockedCalendar).when(mockedProject).getCalendar();
		Collection<Event> events = new ArrayList<Event>();
		Event event  = new Event();
		events.add(event);
		doReturn(events).when(mockedCalendar).getEvents();
		Collection<Event> eventsOfMember = new HashSet<Event>();
		Event mockedMemberEvent = mock(Event.class);
		eventsOfMember.add(mockedMemberEvent);
		PowerMockito.when(Project.findProject(id)).thenReturn(mockedProject);
		doReturn(eventsOfMember).when(mockedProject).getEventsOfMembers();
		
		DSRestResponse restResponse = aut.getEvents(id, encodedMemberIds);
		
		verify(mockedProject).getEventsOfMembers();
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(2, restResponse.getResponse().getData().size());
	}
	@Test
	public void testCreateEvent(){
		
		Long projectId = (long)1;
		Event mockedEvent = mock(Event.class);
		BindingResult mockedBindingResult = mock(BindingResult.class);
		Project mockedProject = mock(Project.class);
		Calendar mockedCalendar = mock(Calendar.class);
		doReturn(mockedCalendar).when(mockedProject).getCalendar();
		Collection<Event> mockedEvents = mock(Collection.class);
		doReturn(mockedEvents).when(mockedCalendar).getEvents();
		PowerMockito.when(Project.findProject(projectId)).thenReturn(mockedProject);
		
		DSRestResponse restResponse = aut.creatEvent(projectId, mockedEvent, mockedBindingResult);
		
		PowerMockito.verifyStatic();
		Project.findProject(projectId);
		verify(mockedEvents).add(mockedEvent);
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(1, restResponse.getResponse().getData().size());
	}
	@Test
	public void testUpdateEvent(){
		
		Long projectId = (long)1;
		Long id = (long)2;
		BindingResult mockedBindingResult = mock(BindingResult.class);
		Event mockedEvent = mock(Event.class);
		Event mockedResultEvent = mock(Event.class);
		Long eventId = mockedEvent.getId();
		doReturn(mockedResultEvent).when(eventRepository).findOne(eventId);
		
		DSRestResponse restResponse = aut.updateEvent(projectId, id, mockedEvent, mockedBindingResult);
		
		verify(eventRepository).save(mockedEvent);
		verify(mockedEvent).setId(id);
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(1, restResponse.getResponse().getData().size());
		Assert.assertTrue(restResponse.getResponse().getData().contains(mockedResultEvent));
	}
	@Test
	public void testDeleteEvent(){
		Long accountId = (long)1;
		Long id = (long)1;
		Event mockedEvent = mock(Event.class);
		Calendar mockedCalendar = mock(Calendar.class);
		Collection<Calendar> calendars = new ArrayList<Calendar>();
		calendars.add(mockedCalendar);
		doReturn(calendars).when(mockedEvent).getCalendars();
		Collection<Event> events = mock(Collection.class);
		doReturn(events).when(mockedCalendar).getEvents();
		doReturn(mockedEvent).when(eventRepository).findOne(id);
		
		DSRestResponse restResponse = aut.deleteEvent(accountId, id);
		
		verify(events).remove(mockedEvent);
		verify(eventRepository).delete(mockedEvent);
		Assert.assertEquals(0, restResponse.getResponse().getStatus());
		Assert.assertEquals(1, restResponse.getResponse().getData().size());
		Assert.assertTrue(restResponse.getResponse().getData().contains(mockedEvent));
		
	}
	@Test
	@PrepareForTest({Project.class})
	public void testGetRoadmap(){
		Long itemId = (long)1;
		Project mockedProject = mock(Project.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(itemId)).thenReturn(mockedProject);
		
		String result = aut.getPlan(itemId, uiModel);
		verify(uiModel).addAttribute("project", mockedProject);
		Assert.assertEquals("projects/roadmap", result);
	}
	List<SiteMapItem> siteMapItems;
	@Test
	@PrepareForTest({Project.class})
	public void testshowHomepage(){
		long id = (long)2;
		Project mockedProject = mock(Project.class);
		Project mockedParentProject = mock(Project.class);
		doReturn(mockedParentProject).when(mockedProject).getParentContainer();
		doReturn("name").when(mockedProject).getName();
		doReturn((long)1).when(mockedProject).getId();
		doReturn("name parent").when(mockedParentProject).getName();
		doReturn((long)2).when(mockedParentProject).getId();
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(id)).thenReturn(mockedProject);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				siteMapItems = (List<SiteMapItem>) invocation.getArguments()[1];
				return null;
			}
		}).when(uiModel).addAttribute(eq("siteMapItems"), any(List.class));
		String result = aut.showhomepage(id, uiModel);
		
		PowerMockito.verifyStatic();
		Project.findProject(id);
		Assert.assertEquals("projects/homepage", result);
		verify(uiModel).addAttribute(eq("siteMapItems"), any(List.class));
		Assert.assertEquals(2, siteMapItems.size());
		Assert.assertEquals("name parent", siteMapItems.get(0).getName());
		Assert.assertEquals("/projects/2", siteMapItems.get(0).getUrl());
	}
}
