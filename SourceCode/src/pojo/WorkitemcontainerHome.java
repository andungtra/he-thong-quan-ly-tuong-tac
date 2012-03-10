package pojo;
// Generated Mar 10, 2012 10:58:54 AM by Hibernate Tools 3.4.0.CR1


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Workitemcontainer.
 * @see pojo.Workitemcontainer
 * @author Hibernate Tools
 */
public class WorkitemcontainerHome {

    private static final Log log = LogFactory.getLog(WorkitemcontainerHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();
    
    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        }
        catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }
    
    public void persist(Workitemcontainer transientInstance) {
        log.debug("persisting Workitemcontainer instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Workitemcontainer instance) {
        log.debug("attaching dirty Workitemcontainer instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Workitemcontainer instance) {
        log.debug("attaching clean Workitemcontainer instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Workitemcontainer persistentInstance) {
        log.debug("deleting Workitemcontainer instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Workitemcontainer merge(Workitemcontainer detachedInstance) {
        log.debug("merging Workitemcontainer instance");
        try {
            Workitemcontainer result = (Workitemcontainer) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Workitemcontainer findById( java.lang.Integer id) {
        log.debug("getting Workitemcontainer instance with id: " + id);
        try {
            Workitemcontainer instance = (Workitemcontainer) sessionFactory.getCurrentSession()
                    .get("pojo.Workitemcontainer", id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(Workitemcontainer instance) {
        log.debug("finding Workitemcontainer instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("pojo.Workitemcontainer")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

