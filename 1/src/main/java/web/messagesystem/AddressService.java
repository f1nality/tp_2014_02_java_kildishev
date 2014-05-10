package web.messagesystem;

import java.util.ArrayList;
import java.util.List;

public class AddressService {
	private List<Address> accountServices = new ArrayList<Address>();
    private Address frontendService = null;
    private int currentAccountService = 0;

    public Address getAccountService() {
        Address service = accountServices.get(currentAccountService++);

        if (currentAccountService >= accountServices.size()) {
            currentAccountService = 0;
        }

        return service;
    }

    public void addAccountService(Address accountService) {
        accountServices.add(accountService);
    }

    public Address getFrontendService() {
        return frontendService;
    }

    public void setFrontendService(Address frontendService) {
        this.frontendService = frontendService;
    }
}
