package lk.example.springsecurity.entity;

import javax.persistence.*;

@Entity
@Table
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String role;
    @Enumerated(EnumType.STRING)
    @Column
    private AuthenticationProvider authenticationProvider;
    public UserEntity() {

    }

    public UserEntity(long id,
                      String userName,
                      String password,
                      String role,
                      AuthenticationProvider authenticationProvider) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", authenticationProvider=" + authenticationProvider +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
