package web.db;

/**
 * Created by Denis on 28.03.14.
 */
public class AccountService {
    public static int getUserId(String login, String password) {
        Account account = AccountDAO.getAccountByLogin(login);

        if (account != null && account.getPassword().equals(password)) {
            return account.getId();
        } else {
            return -1;
        }
    }

    public static boolean accountExists(String login) {
        Account account = AccountDAO.getAccountByLogin(login);

        return account != null;
    }

    public static boolean signUp(String login, String password) {
        return AccountDAO.addAccount(login, password);
    }

    public static boolean removeAccount(String login) {
        return AccountDAO.removeAccount(login);
    }
}
