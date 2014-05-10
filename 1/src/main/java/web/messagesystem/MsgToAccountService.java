package web.messagesystem;

import web.accounts.AccountService;

public abstract class MsgToAccountService extends Msg {

	public MsgToAccountService(Address from, Address to) {
		super(from, to);		
	}

	void exec(Abonent abonent) {
		if (abonent instanceof AccountService){
			exec((AccountService)abonent);
		}
	}

	abstract void exec(AccountService accountService);
}
