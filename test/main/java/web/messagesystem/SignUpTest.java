package web.messagesystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import web.accounts.SignUpCode;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * @author d.kildishev
 */
public class SignUpTest extends MessageSystemTest {
    @Before
    public void setUp() throws Exception {
        super.setUp();

        doNothing().when(frontend).signUpUser(anyString(), any(SignUpCode.class));
    }

    @Test(timeout=20000)
    public void testSignUpSuccess() {
        Assert.assertTrue(getSignUpCode("newtest", "test") == SignUpCode.OK);
    }

    @Test(timeout=20000)
    public void testSignUpFailure() {
        Assert.assertFalse(getSignUpCode("test", "test") == SignUpCode.OK);
    }

    protected SignUpCode getSignUpCode(String login, String password) {
        ArgumentCaptor<SignUpCode> argument = ArgumentCaptor.forClass(SignUpCode.class);
        Msg msg = new MsgSignUpUser(frontend.getAddress(), accountService.getAddress(), login, password, "test_session_id");

        ms.sendMessage(msg);

        while (!Thread.interrupted()) {
            try {
                verify(frontend).signUpUser(anyString(), argument.capture());
                break;
            } catch (WantedButNotInvoked e) { }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }

        return argument.getValue();
    }
}
