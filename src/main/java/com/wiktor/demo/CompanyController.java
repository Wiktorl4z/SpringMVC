package com.wiktor.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping(value = "companies")
@RestController
public class CompanyController {
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    List<Company> findAll() {
        return companyRepository.findAll();
    }

    //   @RequestMapping(method = RequestMethod.GET)
    @GetMapping("/{companyName}")
    Company findOne(@PathVariable("companyName") String name) {
        return companyRepository.findOne(name);
    }

    @GetMapping("/{companyName}/employees/{firstName}")
    List<Employee> findCompanyEmployeesWithFirstName(
            @PathVariable String companyName, // jezeli jest taka sama nazwa nie musimy pisac
            @PathVariable("firstName") String name) {
        return findOne(companyName)
                .getEmployees()
                .stream()
                .filter(employee -> Objects.equals(employee.getFirstName(), name))
                .collect(Collectors.toList());
    }
    @GetMapping("/{companyName}/employees/{lastName}/{firstName}")
    List<Employee> findCompanyEmployeesWithLastNameAndFirstName(@PathVariable Map<String,String> pathVariable){
            return findOne(pathVariable.get("companyName"))
                    .getEmployees()
                    .stream()
                    .filter(employee -> Objects.equals(employee.getLastName(), pathVariable.get("lastName")))
                    .filter(employee -> Objects.equals(employee.getFirstName(), pathVariable.get("firstName")))
                    .collect(Collectors.toList());
    }
}


