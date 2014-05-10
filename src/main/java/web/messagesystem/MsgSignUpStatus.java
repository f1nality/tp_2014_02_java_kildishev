package web.messagesystem;

import web.accounts.SignUpCode;
import web.frontend.Frontend;

public class MsgSignUpStatus extends MsgToFrontend {
	private String sessionId;
	private SignUpCode code;

	public MsgSignUpStatus(Address from, Address to, String sessionId, SignUpCode code) {
		super(from, to);
		this.sessionId = sessionId;
		this.code = code;
	}

	void exec(Frontend frontend) {
		frontend.signUpUser(sessionId, code);
	}
}
