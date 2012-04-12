package org.hcmus.tis.model;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Account.class)
public class AccountIntegrationTest {
	@Autowired
	AccountDataOnDemand dod;
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
    @Test
    public void testFindAccountFullEmail(){
    	Account account = dod.getRandomAccount();
    	Collection<Account> result = Account.findAccount(account.getEmail(),0, 1);
    	Assert.assertTrue(result.size() > 0);
    }
    @Test
    public void testFindAccountSubEmail(){
    	Account account = dod.getRandomAccount();
    	Collection<Account> result = Account.findAccount(account.getEmail().substring(1),0, 1);
    	Assert.assertTrue(result.size() > 0);
    }
}
