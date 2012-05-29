

import org.hcmus.tis.model.AccountTest;
import org.hcmus.tis.model.ProjectTest;
import org.hcmus.tis.model.StudyClassTest;
import org.hcmus.tis.model.WorkItemTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountTest.class, ProjectTest.class, StudyClassTest.class,
		WorkItemTest.class })
public class ModelTestSuite {

}
