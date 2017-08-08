package com.wiktor.demo;

import java.math.BigDecimal;

public class Employee {

    private String firstName;
    private String secondName;
    private BigDecimal salary;


    public Employee(String firstName, String secondName, BigDecimal salary) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
