package org.hcmus.tis.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountIntegrationTest.class,
		AccountStatusIntegrationTest.class, AttachmentIntegrationTest.class,
		CalendarIntegrationTest.class, CommentIntegrationTest.class,
		EventIntegrationTest.class, IterationIntegrationTest.class,
		MemberInformationIntegrationTest.class,
		MemberRoleIntegrationTest.class, PermissionIntegrationTest.class,
		PriorityIntegrationTest.class, ProjectIntegrationTest.class,
		ProjectProcessIntegrationTest.class, StudyClassIntegrationTest.class,
		WorkItemIntegrationTest.class, WorkItemStatusIntegrationTest.class,
		WorkItemTypeIntegrationTest.class })
public class IntegrationTestSuite {

}
