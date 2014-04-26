package web.messagesystem;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import web.accounts.AccountService;
import web.db.AccountDAO;
import web.db.HibernateUtil;
import web.frontend.Frontend;
import web.messagesystem.MessageSystem;
import web.messagesystem.MsgGetUserId;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author d.kildishev
 */
public class SignInSuccessTest {
    private MessageSystem ms = new MessageSystem();
    private Frontend frontend =  spy(new Frontend(ms));
    private AccountService accountService = new AccountService(ms, "hibernate-test.cfg.xml");

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

    @Test
    public void testSignInSuccess() throws Exception {
        Assert.assertTrue(getSignInUserId("test", "test") != -1);
    }

    private Long getSignInUserId(String login, String password) throws Exception {
        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        final Object lock = new Object();

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                synchronized (lock) {
                    lock.notify();
                }

                return null;
            }
        }).when(frontend).setId(anyString(), argument.capture());

        ms.sendMessage(new MsgGetUserId(frontend.getAddress(), accountService.getAddress(), login, password, null));

        synchronized (lock) {
            lock.wait();
        }

        return argument.getValue();
    }
}
