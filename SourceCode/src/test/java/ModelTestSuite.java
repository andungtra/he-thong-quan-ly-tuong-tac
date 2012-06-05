

import org.hcmus.tis.model.unittest.AccountTest;
import org.hcmus.tis.model.unittest.ProjectTest;
import org.hcmus.tis.model.unittest.StudyClassTest;
import org.hcmus.tis.model.unittest.WorkItemTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountTest.class, ProjectTest.class, StudyClassTest.class,
		WorkItemTest.class })
public class ModelTestSuite {

}
