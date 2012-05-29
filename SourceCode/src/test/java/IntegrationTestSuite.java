

import org.hcmus.tis.model.AccountIntegrationTest;
import org.hcmus.tis.model.AccountStatusIntegrationTest;
import org.hcmus.tis.model.AttachmentIntegrationTest;
import org.hcmus.tis.model.CalendarIntegrationTest;
import org.hcmus.tis.model.CommentIntegrationTest;
import org.hcmus.tis.model.EventIntegrationTest;
import org.hcmus.tis.model.IterationIntegrationTest;
import org.hcmus.tis.model.MemberInformationIntegrationTest;
import org.hcmus.tis.model.MemberRoleIntegrationTest;
import org.hcmus.tis.model.PermissionIntegrationTest;
import org.hcmus.tis.model.PriorityIntegrationTest;
import org.hcmus.tis.model.ProjectIntegrationTest;
import org.hcmus.tis.model.ProjectProcessIntegrationTest;
import org.hcmus.tis.model.StudyClassIntegrationTest;
import org.hcmus.tis.model.WorkItemIntegrationTest;
import org.hcmus.tis.model.WorkItemStatusIntegrationTest;
import org.hcmus.tis.model.WorkItemTypeIntegrationTest;
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
