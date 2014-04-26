package web.frontend;

import web.messagesystem.Address;
import web.messagesystem.AddressService;

public class UserSession {
    private Address accountService;
    private String name;
    private String sessionId;
    private Long userId;
    private Boolean isRegistering = false;
    private int registrationCode;

    public UserSession(String sessionId, String name, AddressService addressService) {
        this.sessionId = sessionId;
        this.name = name;
        this.accountService = addressService.getAccountService();
    }

    public Address getAccountService() {
        return accountService;
    }

    public String getName(){
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean isRegistering() {
        return isRegistering;
    }

    public void setRegistering(Boolean isRegistering) {
        this.isRegistering = isRegistering;
    }

    public int getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(int registrationCode) {
        this.registrationCode = registrationCode;
    }
}
