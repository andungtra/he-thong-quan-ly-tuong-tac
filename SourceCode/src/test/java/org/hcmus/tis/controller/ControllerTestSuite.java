package org.hcmus.tis.controller;

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
