package web.accounts;

import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import web.db.AccountDataSet;
import web.db.HibernateUtil;
import web.messagesystem.*;
import helpers.TimeHelper;
import web.db.AccountDAO;

/**
 * @author d.kildishev
 */
public class AccountService implements Abonent, Runnable {
    private Address address = new Address();
    private MessageSystem ms;
    private AccountDAO accountDAO = null;
    private boolean dbWorking = false;
    private String dbConfig;

    public AccountService(MessageSystem ms, String dbConfig) {
        this.ms = ms;
        this.dbConfig = dbConfig;

        ms.addService(this);
        ms.getAddressService().addAccountService(address);

        accountDAO = new AccountDAO();
    }

    public void run() {
        while(true) {
            if (!dbWorking) {
                recreateFactory();
            }

            ms.execForAbonent(this);
            TimeHelper.sleep(10);
        }
    }

    public void recreateFactory() {
        try {
            accountDAO.setFactory(getSessionFactory());
            dbWorking = true;
        } catch (JDBCConnectionException ignored) { }
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return ms;
    }

    private SessionFactory getSessionFactory() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(dbConfig);

        if (sessionFactory == null) {
            throw new JDBCConnectionException("Connection failed", null);
        }

        return sessionFactory;
    }

    public Long getUserId(String login, String password) {
        TimeHelper.sleep(5000);

        AccountDataSet accountDataSet = null;

        try {
            accountDataSet = accountDAO.getAccountByLogin(login);
        } catch (JDBCConnectionException e) {
            dbWorking = false;
            return -2L;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (accountDataSet != null && accountDataSet.getPassword().equals(password)) {
            return accountDataSet.getId();
        } else {
            return -1L;
        }
    }

    public boolean accountExists(String login) {
        AccountDataSet accountDataSet = null;

        try {
            accountDataSet = accountDAO.getAccountByLogin(login);
        } catch (JDBCConnectionException e) {
            e.printStackTrace();
            dbWorking = false;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountDataSet != null;
    }

    public int signUp(String login, String password) {
        try {
            accountDAO.addAccount(login, password);
        } catch (JDBCConnectionException e) {
            dbWorking = false;
            return 2;
        } catch (Exception e) {
            e.printStackTrace();
            dbWorking = false;
            return 2;
        }

        return 0;
    }

    public boolean removeAccount(String login) {
        try {
            return accountDAO.removeAccount(login);
        } catch (JDBCConnectionException e) {
            dbWorking = false;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
