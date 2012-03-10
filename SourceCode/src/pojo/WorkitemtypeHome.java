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
 * Home object for domain model class Workitemtype.
 * @see pojo.Workitemtype
 * @author Hibernate Tools
 */
public class WorkitemtypeHome {

    private static final Log log = LogFactory.getLog(WorkitemtypeHome.class);

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
    
    public void persist(Workitemtype transientInstance) {
        log.debug("persisting Workitemtype instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Workitemtype instance) {
        log.debug("attaching dirty Workitemtype instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Workitemtype instance) {
        log.debug("attaching clean Workitemtype instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Workitemtype persistentInstance) {
        log.debug("deleting Workitemtype instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Workitemtype merge(Workitemtype detachedInstance) {
        log.debug("merging Workitemtype instance");
        try {
            Workitemtype result = (Workitemtype) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Workitemtype findById( java.lang.Integer id) {
        log.debug("getting Workitemtype instance with id: " + id);
        try {
            Workitemtype instance = (Workitemtype) sessionFactory.getCurrentSession()
                    .get("pojo.Workitemtype", id);
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
    
    public List findByExample(Workitemtype instance) {
        log.debug("finding Workitemtype instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("pojo.Workitemtype")
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

