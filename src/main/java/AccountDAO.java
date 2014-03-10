import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author d.kildishev
 */
public class AccountDAO extends DAO {
    public static List<Account> getAllAccount() {
        Session session = null;
        List<Account> accounts = null;

        try {
            session = openSession();
            session.beginTransaction();
            accounts = session.createCriteria(Account.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            accounts = new ArrayList<Account>();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return accounts;
    }

    public static Account getAccountByLogin(String login) {
        Session session = null;
        Account account = null;

        try {
            session = openSession();
            session.beginTransaction();
            //List accounts = session.createQuery("FROM Account WHERE login = :login AND password = :password").setString("login", login).setString("password", password).list();
            account = (Account)session.createCriteria(Account.class).add(Restrictions.eq("login", login)).uniqueResult();
            session.getTransaction().commit();
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
            result = false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return result;
    }
}
