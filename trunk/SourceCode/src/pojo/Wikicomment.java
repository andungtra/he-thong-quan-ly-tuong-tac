package pojo;
// Generated Mar 10, 2012 10:58:54 AM by Hibernate Tools 3.4.0.CR1



/**
 * Wikicomment generated by hbm2java
 */
public class Wikicomment  implements java.io.Serializable {


     private Integer wikiCommentId;
     private Wikipage wikipage;
     private Account account;
     private String content;

    public Wikicomment() {
    }

    public Wikicomment(Wikipage wikipage, Account account, String content) {
       this.wikipage = wikipage;
       this.account = account;
       this.content = content;
    }
   
    public Integer getWikiCommentId() {
        return this.wikiCommentId;
    }
    
    public void setWikiCommentId(Integer wikiCommentId) {
        this.wikiCommentId = wikiCommentId;
    }
    public Wikipage getWikipage() {
        return this.wikipage;
    }
    
    public void setWikipage(Wikipage wikipage) {
        this.wikipage = wikipage;
    }
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }




}


