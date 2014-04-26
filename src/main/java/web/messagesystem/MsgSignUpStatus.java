package web.messagesystem;

import web.frontend.Frontend;

public class MsgSignUpStatus extends MsgToFrontend {
	private String sessionId;
	private int code;

	public MsgSignUpStatus(Address from, Address to, String sessionId, int code) {
		super(from, to);
		this.sessionId = sessionId;
		this.code = code;
	}

	void exec(Frontend frontend) {
		frontend.signUpUser(sessionId, code);
	}
}
