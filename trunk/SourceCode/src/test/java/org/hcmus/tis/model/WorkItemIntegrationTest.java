package org.hcmus.tis.model;

import org.hibernate.jdbc.Work;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = WorkItem.class)
public class WorkItemIntegrationTest {
	@Autowired
	IterationDataOnDemand iterationDataOnDemand;
	@Autowired
	ProjectDataOnDemand projectDataOnDemand;
	@Autowired
	WorkItemDataOnDemand workItemDataOnDemand;
    @Test
    public void testMarkerMethod() {
    }
    public void testCountWorkItemByProject(){
    	
    }
}
