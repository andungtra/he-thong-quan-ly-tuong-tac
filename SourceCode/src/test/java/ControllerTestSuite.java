

import org.hcmus.tis.controller.AccountControllerTest;
import org.hcmus.tis.controller.AdministratorControllerTest;
import org.hcmus.tis.controller.AttachmentControllerTest;
import org.hcmus.tis.controller.CommentControllerTest;
import org.hcmus.tis.controller.IterationControllerTest;
import org.hcmus.tis.controller.MemberInformationTest;
import org.hcmus.tis.controller.ProjectControllerTest;
import org.hcmus.tis.controller.StudyClassControllerTest;
import org.hcmus.tis.controller.WorkItemControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountControllerTest.class, AdministratorControllerTest.class,
		AttachmentControllerTest.class, CommentControllerTest.class,
		IterationControllerTest.class, MemberInformationTest.class,
		ProjectControllerTest.class, StudyClassControllerTest.class,
		WorkItemControllerTest.class })
public class ControllerTestSuite {

}
