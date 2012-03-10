package pojo;
// Generated Mar 10, 2012 10:58:54 AM by Hibernate Tools 3.4.0.CR1


import java.util.HashSet;
import java.util.Set;

/**
 * Wikipage generated by hbm2java
 */
public class Wikipage  implements java.io.Serializable {


     private Integer wikiPageId;
     private Project project;
     private Account account;
     private String name;
     private String summary;
     private String content;
     private Set wikicomments = new HashSet(0);

    public Wikipage() {
    }

	
    public Wikipage(Project project, Account account) {
        this.project = project;
        this.account = account;
    }
    public Wikipage(Project project, Account account, String name, String summary, String content, Set wikicomments) {
       this.project = project;
       this.account = account;
       this.name = name;
       this.summary = summary;
       this.content = content;
       this.wikicomments = wikicomments;
    }
   
    public Integer getWikiPageId() {
        return this.wikiPageId;
    }
    
    public void setWikiPageId(Integer wikiPageId) {
        this.wikiPageId = wikiPageId;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getSummary() {
        return this.summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    public Set getWikicomments() {
        return this.wikicomments;
    }
    
    public void setWikicomments(Set wikicomments) {
        this.wikicomments = wikicomments;
    }




}


