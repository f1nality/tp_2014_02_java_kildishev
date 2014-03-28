package web.db;

import com.sun.jna.platform.win32.Secur32;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author d.kildishev
 */
public class AccountDAO extends DAO {
    public static Account getAccountByLogin(String login) {
        Session session = null;
        Account account = null;

        try {
            session = openSession();
            session.beginTransaction();
            account = (Account)session.createCriteria(Account.class).add(Restrictions.eq("login", login)).uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return account;
    }

    public static boolean addAccount(String login, String password) {
        Session session = null;
        boolean result = true;

        try {
            session = openSession();
            session.beginTransaction();
            session.save(new Account(login, password));
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            result = false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return result;
    }

    public static boolean removeAccount(String login) {
        Session session = null;
        boolean result = true;

        try {
            session = openSession();
            session.beginTransaction();
            Account account = (Account)session.createCriteria(Account.class).add(Restrictions.eq("login", login)).uniqueResult();
            session.delete(account);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            result = false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return result;
    }
}
