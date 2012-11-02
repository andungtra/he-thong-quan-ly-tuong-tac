package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.ProjectRepository;
import org.hcmus.tis.util.Parameter;
import org.hibernate.mapping.Array;
import static org.junit.Assert.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

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
	@Mock
	private Project mockedProject;
	@Mock
	private ProjectRepository projectRepository;
	@Mock
	private MemberInformationRepository memberInformationRepository;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		aut = new ProjectController();
		aut.setProjectRepository(projectRepository);
		aut.setMemberInformationRepository(memberInformationRepository);
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
	@Test
	public void testListMembers(){
		doReturn(new HashSet<MemberInformation>()).when(mockedProject).getMemberInformations();
		String recentAction = "action";
		Long recentMemberId = (long)1;
		Set<MemberInformation> members = mock(Set.class);
		doReturn(members).when(mockedProject).getMemberInformations();
		Long projectId = mockedProject.getId();
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		doReturn(mockedProject).when(projectRepository).findOne((long)1);	
		MemberInformation recentMember = mock(MemberInformation.class);
		doReturn(recentMember).when(memberInformationRepository).findOne(recentMemberId);
		String result = aut.listMembers(projectId, uiModel, recentAction, recentMemberId);
		verify(uiModel).addAttribute("recentAction", recentAction);
		verify(uiModel).addAttribute("recentMember", recentMember);
		verify(uiModel).addAttribute("memberinformations", members);
		assertEquals("projects/member", result);
	}
	@Test
	public void testGetEvents(){
		Long id = (long)mockedProject.getId();
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
		doReturn(mockedProject).when(projectRepository).findOne(id);
		eventsOfMember.add(mockedMemberEvent);
		doReturn(eventsOfMember).when(mockedProject).getEventsOfMembers();
		
		DSRestResponse restResponse = aut.getEvents(id, encodedMemberIds);
		
		verify(mockedProject).getEventsOfMembers();
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(2, restResponse.getResponse().getData().size());
	}
	@Test
	public void testCreateEvent(){
		
		Long projectId = mockedProject.getId();
		Event mockedEvent = mock(Event.class);
		BindingResult mockedBindingResult = mock(BindingResult.class);
		Project mockedProject = mock(Project.class);
		Calendar mockedCalendar = mock(Calendar.class);
		doReturn(mockedCalendar).when(mockedProject).getCalendar();
		Collection<Event> mockedEvents = mock(Collection.class);
		doReturn(mockedEvents).when(mockedCalendar).getEvents();
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		
		DSRestResponse restResponse = aut.creatEvent(projectId, mockedEvent, mockedBindingResult);
		
		verify(projectRepository).findOne(projectId);
		verify(mockedEvents).add(mockedEvent);
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(1, restResponse.getResponse().getData().size());
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
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(1, restResponse.getResponse().getData().size());
		assertTrue(restResponse.getResponse().getData().contains(mockedResultEvent));
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
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(1, restResponse.getResponse().getData().size());
		assertTrue(restResponse.getResponse().getData().contains(mockedEvent));
		
	}
	@Test
	public void testGetRoadmap(){
		Long itemId = mockedProject.getId();
		Project mockedProject = mock(Project.class);
		doReturn(mockedProject).when(projectRepository).findOne(itemId);
		
		String result = aut.getPlan(itemId, uiModel);
		verify(uiModel).addAttribute("project", mockedProject);
		assertEquals("projects/roadmap", result);
	}
	List<SiteMapItem> siteMapItems;
	@Test
	public void testshowHomepage(){
		long id = (long)2;
		Project mockedProject = mock(Project.class);
		Project mockedParentProject = mock(Project.class);
		doReturn(mockedParentProject).when(mockedProject).getParentContainer();
		doReturn("name").when(mockedProject).getName();
		doReturn((long)1).when(mockedProject).getId();
		doReturn("name parent").when(mockedParentProject).getName();
		doReturn((long)2).when(mockedParentProject).getId();
		doReturn(mockedProject).when(projectRepository).findOne(id);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				siteMapItems = (List<SiteMapItem>) invocation.getArguments()[1];
				return null;
			}
		}).when(uiModel).addAttribute(eq("siteMapItems"), any(List.class));
		String result = aut.showhomepage(id, uiModel);
		
		verify(projectRepository).findOne(id);
		assertEquals("projects/homepage", result);
		verify(uiModel).addAttribute(eq("siteMapItems"), any(List.class));
		assertEquals(2, siteMapItems.size());
		assertEquals("name parent", siteMapItems.get(0).getName());
		assertEquals("/projects/2", siteMapItems.get(0).getUrl());
	}
}
