package com.nectar.controller;

import com.nectar.entity.Employee;
import com.nectar.entity.Employees;
import com.nectar.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @PostMapping
    public Employee saveEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") String employeeId){
        return employeeRepository.getEmployee(employeeId);
    }

    @PutMapping("/{employeeId}")
    public String updateEmployee(@RequestBody Employee employee,@PathVariable("employeeId") String employeeId) {
        return employeeRepository.updateEmployee(employeeId,employee);
    }

    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") String employeeId){
       return employeeRepository.deleteEmployee(employeeId);
    }

//    @GetMapping("/year/{year}")
//    public Employees getEmployeesByYear(@PathVariable("year") String year){
//        System.out.println("year = " + year);
//        return Employees.builder().employees(employeeRepository.findByYear(year)).build();
//
//    }




}
