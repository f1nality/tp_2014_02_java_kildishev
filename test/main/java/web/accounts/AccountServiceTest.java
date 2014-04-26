package web.accounts;

import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import web.accounts.AccountService;
import web.db.AccountDAO;
import web.db.AccountDataSet;
import web.db.HibernateUtil;
import web.messagesystem.MessageSystem;

/**
 * @author d.kildishev
 */
public class AccountServiceTest {
    private MessageSystem ms = new MessageSystem();
    private AccountService accountService = new AccountService(ms, "hibernate-test.cfg.xml");

    @Before
    public void setUp() throws Exception {
        SessionFactory factory = HibernateUtil.getSessionFactory("hibernate-test.cfg.xml");
        Session session = factory.openSession();

        session.beginTransaction();
        session.createSQLQuery("TRUNCATE TABLE accounts").executeUpdate();
        session.getTransaction().commit();

        AccountDAO dao = new AccountDAO();

        dao.setFactory(factory);
        dao.addAccount("test", "test");

        accountService.recreateFactory();
    }

    @Test
    public void testSignInSuccess() throws Exception {
        Assert.assertTrue(accountService.getUserId("test", "test") != -1);
    }

    @Test
    public void testSignInFailure() throws Exception {
        Assert.assertFalse(accountService.getUserId("test", "wrong_password") != -1);
    }

    @Test
    public void testSignUpSuccess() throws Exception {
        Assert.assertFalse(accountService.accountExists("newtest"));
        Assert.assertTrue(accountService.signUp("newtest", "test") == 0);
    }

    @Test
    public void testSignUpFailure() throws Exception {
        Assert.assertTrue(accountService.accountExists("test"));
    }
}
