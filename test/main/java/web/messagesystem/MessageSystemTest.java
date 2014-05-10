package web.messagesystem;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import web.accounts.AccountService;
import web.db.AccountDAO;
import web.db.HibernateUtil;
import web.frontend.Frontend;
import static org.mockito.Mockito.*;

/**
 * @author d.kildishev
 */
public class MessageSystemTest {
    protected MessageSystem ms = new MessageSystem();
    protected Frontend frontend =  spy(new Frontend(ms));
    protected AccountService accountService = new AccountService(ms, "hibernate-test.cfg.xml");

    @Before
    public void setUp() throws Exception {
        (new Thread(frontend)).start();
        (new Thread(accountService)).start();

        SessionFactory factory = HibernateUtil.getSessionFactory("hibernate-test.cfg.xml");
        Session session = factory.openSession();

        session.beginTransaction();
        session.createSQLQuery("TRUNCATE TABLE accounts").executeUpdate();
        session.getTransaction().commit();

        AccountDAO dao = new AccountDAO();

        dao.setFactory(factory);
        dao.addAccount("test", "test");
    }
}
