package com.wiktor.demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping(value = "${urls.company.root}", produces = MediaType.APPLICATION_XML_VALUE) // dzieki temu mozemy
// tylko zobaczyc w xml
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

    @GetMapping("/{companyName}/${urls.company.employees.root}/{firstName}")
    List<Employee> findCompanyEmployeesWithFirstName(
            @PathVariable String companyName, // jezeli jest taka sama nazwa nie musimy pisac
            @PathVariable("firstName") String name) {
        return findOne(companyName)
                .getEmployees()
                .stream()
                .filter(employee -> Objects.equals(employee.getFirstName(), name))
                .collect(Collectors.toList());
    }

    @GetMapping("/{companyName}/${urls.company.employees.root}/{lastName}/{firstName}")
    List<Employee> findCompanyEmployeesWithLastNameAndFirstName(@PathVariable Map<String, String> pathVariable) {
        return findOne(pathVariable.get("companyName"))
                .getEmployees()
                .stream()
                .filter(employee -> Objects.equals(employee.getLastName(), pathVariable.get("lastName")))
                .filter(employee -> Objects.equals(employee.getFirstName(), pathVariable.get("firstName")))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/{companyName}/employees",
            produces = MediaType.APPLICATION_XML_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
        // mozemy dodac kod w JSON a produces zamieni na XML
    Employee addEmployee(
            @PathVariable String companyName,
            @RequestParam String firstName, // gdy mamy taka adnotacje trzeba bedzie dodać wartosci
            @RequestParam String lastName,
            @RequestParam(required = false) BigDecimal salary   // tego pola nie trzeba bedzie dodawac
    ) {
        Company orignal = companyRepository.findOne(companyName);
        List<Employee> employees = new ArrayList<>(orignal.getEmployees());
        Employee employee = new Employee(Employee.getNextEmployeeId(), firstName, lastName, salary);
        employees.add(employee);
        Company newCompany = new Company(orignal.getName(), employees);
        companyRepository.save(newCompany);
        return employee;
    }

    @PostMapping("/{companyName}/employees/create")
    List<Employee> addEmployees(
            @PathVariable String companyName,
            @RequestBody AddEmployeesRequest request

    ) {
        Company orignal = companyRepository.findOne(companyName);
        List<Employee> employees = new ArrayList<>(orignal.getEmployees());
        List<Employee> newEmployees = createEmployees(request.getEmployees());
        employees.addAll(newEmployees);
        Company newCompany = new Company(orignal.getName(), employees);
        companyRepository.save(newCompany);
        return employees;
    }

    /**
     * @param employees
     * @return Ze starej listy literujemy po kazdym z pracownikow. Tworzymy nowke sztukę, przypisujemy mu imie, nazwisko i
     * pensje oraz identyfikator.
     */

    private List<Employee> createEmployees(List<Employee> employees) {
        return employees
                .stream()
                .map(employee -> new Employee(Employee.getNextEmployeeId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getSalary()))
                .collect(Collectors.toList());
    }
}