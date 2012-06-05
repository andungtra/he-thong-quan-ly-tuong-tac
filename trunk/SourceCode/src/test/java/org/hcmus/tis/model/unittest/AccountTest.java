package org.hcmus.tis.model.unittest;

import org.hcmus.tis.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

@RunWith(JUnit4.class)
@MockStaticEntityMethods
public class AccountTest {

    @Test
    public void testMethod() {
        int expectedCount = 13;
        Account.countAccounts();
        org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl.expectReturn(expectedCount);
        org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl.playback();
        org.junit.Assert.assertEquals(expectedCount, Account.countAccounts());
    }
}
