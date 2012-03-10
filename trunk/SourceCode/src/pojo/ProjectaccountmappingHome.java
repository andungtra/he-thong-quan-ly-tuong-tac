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
 * Home object for domain model class Projectaccountmapping.
 * @see pojo.Projectaccountmapping
 * @author Hibernate Tools
 */
public class ProjectaccountmappingHome {

    private static final Log log = LogFactory.getLog(ProjectaccountmappingHome.class);

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
    
    public void persist(Projectaccountmapping transientInstance) {
        log.debug("persisting Projectaccountmapping instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Projectaccountmapping instance) {
        log.debug("attaching dirty Projectaccountmapping instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Projectaccountmapping instance) {
        log.debug("attaching clean Projectaccountmapping instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Projectaccountmapping persistentInstance) {
        log.debug("deleting Projectaccountmapping instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Projectaccountmapping merge(Projectaccountmapping detachedInstance) {
        log.debug("merging Projectaccountmapping instance");
        try {
            Projectaccountmapping result = (Projectaccountmapping) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Projectaccountmapping findById( java.lang.Integer id) {
        log.debug("getting Projectaccountmapping instance with id: " + id);
        try {
            Projectaccountmapping instance = (Projectaccountmapping) sessionFactory.getCurrentSession()
                    .get("pojo.Projectaccountmapping", id);
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
    
    public List findByExample(Projectaccountmapping instance) {
        log.debug("finding Projectaccountmapping instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("pojo.Projectaccountmapping")
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

