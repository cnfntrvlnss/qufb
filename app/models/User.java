package models;

import javax.persistence.*;

@Entity
@NamedQueries(
        @NamedQuery(name = "User.findByNumber",
        query = "select u from User u where u.number = :number")
)
public class User {
    private String userId;//用户id
    private String number;//用户编号
    private String name;//用户名称
    private String password;//密码

    private Unit unit;
    private String phone;
    private String email;

    @Id
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(unique=true, nullable=false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
