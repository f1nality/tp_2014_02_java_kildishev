package web.messagesystem;

import web.accounts.AccountService;

public class MsgSignUpUser extends MsgToAccountService {
	private String name;
    private String password;
    private String sessionId;

	public MsgSignUpUser(Address from, Address to, String name, String password, String sessionId) {
		super(from, to);
		this.name = name;
        this.password = password;
        this.sessionId = sessionId;
	}

	void exec(AccountService accountService) {
        if (accountService.accountExists(name)) {
            accountService.getMessageSystem().sendMessage(new MsgSignUpStatus(getTo(), getFrom(), sessionId, 1));
        } else {
            int code = accountService.signUp(name, password);
            accountService.getMessageSystem().sendMessage(new MsgSignUpStatus(getTo(), getFrom(), sessionId, code));
        }
	}
}
