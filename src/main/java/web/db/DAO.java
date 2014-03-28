package web.db;

import org.hibernate.Session;

/**
 * @author d.kildishev
 */
public abstract class DAO {
    protected static Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }
}
