package web;

import javax.persistence.*;

/**
 * @author d.kildishev
 */
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column
    private int id;
    @Column
    private String login;
    @Column
    private String password;

    public Account() {

    }

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getId() {
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


