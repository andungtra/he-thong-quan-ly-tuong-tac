package pojo;
// Generated Mar 10, 2012 10:58:54 AM by Hibernate Tools 3.4.0.CR1



/**
 * Query generated by hbm2java
 */
public class Query  implements java.io.Serializable {


     private Integer queryId;
     private Workitemcontainer workitemcontainer;
     private String name;
     private String condition;

    public Query() {
    }

    public Query(Workitemcontainer workitemcontainer, String name, String condition) {
       this.workitemcontainer = workitemcontainer;
       this.name = name;
       this.condition = condition;
    }
   
    public Integer getQueryId() {
        return this.queryId;
    }
    
    public void setQueryId(Integer queryId) {
        this.queryId = queryId;
    }
    public Workitemcontainer getWorkitemcontainer() {
        return this.workitemcontainer;
    }
    
    public void setWorkitemcontainer(Workitemcontainer workitemcontainer) {
        this.workitemcontainer = workitemcontainer;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getCondition() {
        return this.condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }




}


