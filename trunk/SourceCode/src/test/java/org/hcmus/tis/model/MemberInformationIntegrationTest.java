package org.hcmus.tis.model;

import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = MemberInformation.class)
public class MemberInformationIntegrationTest {
	@Autowired
	MemberInformationDataOnDemand dod;
    @Test
    public void testMarkerMethod() {
    }
    @Test
    public void testCustomRemove(){
    	MemberInformation memberInformation = dod.getRandomMemberInformation();
    	Project mockedProject = Mockito.mock(Project.class);
    	memberInformation.setProject(mockedProject);
    	Set mockedMemberInformation = Mockito.mock(Set.class);
    	Mockito.doReturn(mockedMemberInformation).when(mockedProject).getMemberInformations();
    	memberInformation.entityManager = Mockito.mock(EntityManager.class);
    	
    	memberInformation.remove();

    	Mockito.verify(mockedMemberInformation).remove(Mockito.any(MemberInformation.class));
    }
}
