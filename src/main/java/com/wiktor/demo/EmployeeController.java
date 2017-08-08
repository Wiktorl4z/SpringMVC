package com.wiktor.demo;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("employees")
@RestController
public class EmployeeController {
    @PostMapping("getEmployees")
    public List<Employee> getEmployees() {

        List<Employee> result = new ArrayList<>();

        result.add(new Employee("Wiktor", "Kalinowski", new BigDecimal(1337)));
        result.add(new Employee("Michał", "Trynki", new BigDecimal(434)));
        result.add(new Employee("Grzego", "Loskko", new BigDecimal(123)));

        return result;
    }

    @RequestMapping(value = "findAny", method = {RequestMethod.POST , RequestMethod.GET})
    public Employee findAny() {
        return new Employee("Michał", "Trynki", new BigDecimal(434));
    }
}