package web.messagesystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author d.kildishev
 */
public class SignInTest extends MessageSystemTest {
    //TODO: добавить инкапсулировать, смотреть не пуста ли очередь (test only method)
    @Before
    public void setUp() throws Exception {
        super.setUp();

        doNothing().when(frontend).setId(anyString(), anyLong());
    }

    @Test(timeout=20000)
    public void testSignInSuccess() {
        Assert.assertTrue(getSignInUserId("test", "test") != -1);
    }

    @Test(timeout=20000)
    public void testSignInFailure() {
        Assert.assertFalse(getSignInUserId("test", "wrong_password") != -1);
    }

    protected Long getSignInUserId(String login, String password) {
        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        Msg msg = new MsgGetUserId(frontend.getAddress(), accountService.getAddress(), login, password, "test_session_id");

        ms.sendMessage(msg);

        while (!Thread.interrupted()) {
            try {
                verify(frontend).setId(anyString(), argument.capture());
                break;
            } catch (WantedButNotInvoked e) { }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }

        System.out.println(argument.getValue());
        return argument.getValue();
    }
}
