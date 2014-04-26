package web.db;

import javax.persistence.*;

/**
 * @author d.kildishev
 */
@Entity
@Table(name = "accounts")
public class AccountDataSet {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @Column
    private String login;
    @Column
    private String password;

    public AccountDataSet() {

    }

    public AccountDataSet(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


