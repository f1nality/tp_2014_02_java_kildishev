package web.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 * @author d.kildishev
 */
public class AccountDAO {
    private SessionFactory factory = null;

    public AccountDAO() {
    }

    public void setFactory(SessionFactory factory) {
        this.factory = factory;
    }

    public AccountDataSet getAccountByLogin(String login) {
        Session session = null;
        AccountDataSet accountDataSet = null;

        try {
            session = factory.openSession();
            session.beginTransaction();
            accountDataSet = (AccountDataSet)session.createCriteria(AccountDataSet.class).add(Restrictions.eq("login", login)).uniqueResult();
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return accountDataSet;
    }

    public boolean addAccount(String login, String password) {
        Session session = null;
        boolean result = true;

        try {
            session = factory.openSession();
            session.beginTransaction();
            session.save(new AccountDataSet(login, password));
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return result;
    }

    public boolean removeAccount(String login) {
        Session session = null;
        boolean result = true;

        try {
            session = factory.openSession();
            session.beginTransaction();
            AccountDataSet accountDataSet = (AccountDataSet)session.createCriteria(AccountDataSet.class).add(Restrictions.eq("login", login)).uniqueResult();
            session.delete(accountDataSet);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return result;
    }
}
