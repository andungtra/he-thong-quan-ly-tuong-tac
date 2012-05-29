package org.hcmus.tis.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountTest.class, ProjectTest.class, StudyClassTest.class,
		WorkItemTest.class })
public class ModelTestSuite {

}
