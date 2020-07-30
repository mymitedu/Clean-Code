package com.epam.engx.cleancode.dry.task1;

import java.math.BigDecimal;
import java.util.Date;

public class AccountDetails {
    private Date dateOfbirth;
    private int age;
    private BigDecimal accountBalance;
    private Date startDate;

    public Date getBirth() {
        return dateOfbirth;
    }

    public void setBirth(Date dateOfbirth) {
        this.dateOfbirth = dateOfbirth;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getBalance() {
        return accountBalance;
    }

    public void setBalance(BigDecimal balance) {
        this.accountBalance = balance;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
