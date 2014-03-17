package web;

import org.hibernate.Session;

/**
 * @author d.kildishev
 */
public class DAO {
    protected static Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }
}
