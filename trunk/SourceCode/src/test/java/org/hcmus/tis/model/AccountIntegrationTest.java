package org.hcmus.tis.model;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Account.class)
public class AccountIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
/*    @Test
    public void testToString()
    {
    	Account account = dod.getRandomAccount();
        Assert.assertNotNull("Data on demand for 'Account' failed to initialize correctly", account);
        String email = account.getEmail();
        String resultString = account.toString();
        Assert.assertEquals("toString method for 'Account' return wrong result",email, resultString);
    }*/
    @Test
    public void testToString()
    {
    	Account account = new Account();
    	account.setId((long) 1);
    	account.setEmail("foo@yahoo.com");
    	 String email = account.getEmail();
         String resultString = account.toString();
         Assert.assertEquals("toString method for 'Account' return wrong result",email, resultString);
    }
}
