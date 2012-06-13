package org.hcmus.tis.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.TypedQuery;

import org.hcmus.tis.dto.NonEditableEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Project.class)
public class ProjectIntegrationTest {
	@Autowired
	private ProjectDataOnDemand dod;
	@Autowired
	private MemberInformationDataOnDemand memberInformationDataOnDemand;
	@Autowired
	private EventDataOnDemand eventDataOnDemand;
	@Autowired
	private ProjectDataOnDemand projectDataOnDemand;
	@Autowired
	private AccountDataOnDemand accountDataOnDemand;
	@Autowired
	private CalendarDataOnDemand calendarDataOnDemand;
	@Autowired
	private MemberRoleDataOnDemand memberRoleDataOnDemand;

	@Test
	public void testMarkerMethod() {
	}

	@Test
	public void testCountProjectsByNameLike() {

		String name = dod.getRandomProject().getName();
		long result = Project.countProjectsByNameLike(name);
		Assert.assertTrue(result >= 1);
	}

	@Test
	public void testFindProjectsByNameLike() {
		String name = dod.getRandomProject().getName();
		TypedQuery<Project> result = Project.findProjectsByNameLike(name, 0,
				100);
		Assert.assertTrue(result.getResultList().size() > 0);

	}

	@Test
	public void testGetEventsOfMembersWithOneMember() {
		MemberInformation memberInformation1 = memberInformationDataOnDemand
				.getRandomMemberInformation();
		memberInformation1.getAccount().getCalendar()
				.setEvents(new HashSet<Event>());
		memberInformation1.getAccount().getCalendar().getEvents()
				.add(eventDataOnDemand.getNewTransientEvent(0));
		Collection<Event> events = memberInformation1.getProject()
				.getEventsOfMembers();
		org.junit.Assert.assertEquals(1, events.size());

	}

	@Test
	public void testGetEventsOfMembersWithTwoMemberAndSharedEvent() {
		Project project = projectDataOnDemand.getSpecificProject(0);
		
		Account account1 = accountDataOnDemand.getSpecificAccount(0);
		account1.getCalendar().setEvents(new HashSet<Event>());
		Event ev1 = eventDataOnDemand.getNewTransientEvent(11);
		account1.getCalendar().getEvents().add(ev1);
		//ev1.persist();
		
		Account account2 = accountDataOnDemand.getSpecificAccount(1);
		account2.getCalendar().setEvents(new HashSet<Event>());
		Event ev2 = eventDataOnDemand.getNewTransientEvent(12);
		account2.getCalendar().getEvents().add(ev2);
		//ev2.persist();
		MemberInformation memberInformation1 = new MemberInformation();
		memberInformation1.setAccount(account1);
		memberInformation1.setMemberRole(memberRoleDataOnDemand.getRandomMemberRole());
		memberInformation1.setProject(project);
		memberInformation1.persist();
		
		Event sharedEvent = eventDataOnDemand.getNewTransientEvent(13);
		account1.getCalendar().getEvents().add(sharedEvent);
		account2.getCalendar().getEvents().add(sharedEvent);
		//sharedEvent.persist();
		account1.getCalendar().flush();
		account2.getCalendar().flush();
		project.getMemberInformations().add(memberInformation1);
		MemberInformation member2 = new MemberInformation();
		member2.setAccount(account2);
		member2.setMemberRole(memberRoleDataOnDemand.getRandomMemberRole());
		member2.setProject(project);
		member2.persist();
		project.getMemberInformations().add(member2);
		Assert.assertEquals(2, projectDataOnDemand.getSpecificProject(0).getMemberInformations().size());
		Assert.assertEquals(3, project.getEventsOfMembers().size());
	}

	@Test
	public void testGetIterations() {
	}
}
