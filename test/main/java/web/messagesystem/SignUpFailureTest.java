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
import web.messagesystem.MsgSignUpUser;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

/**
 * @author d.kildishev
 */
public class SignUpFailureTest {
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
    public void testSignUpFailure() throws Exception {
        Assert.assertFalse(getSignUpCode("test", "test") == 0);
    }

    private Integer getSignUpCode(String login, String password) throws Exception {
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
        final Object lock = new Object();

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                synchronized (lock) {
                    lock.notify();
                }

                return null;
            }
        }).when(frontend).signUpUser(anyString(), argument.capture());

        ms.sendMessage(new MsgSignUpUser(frontend.getAddress(), accountService.getAddress(), login, password, null));

        synchronized (lock) {
            lock.wait();
        }

        return argument.getValue();
    }
}
