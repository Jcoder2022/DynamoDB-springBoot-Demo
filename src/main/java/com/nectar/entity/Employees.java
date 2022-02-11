package com.nectar.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Value
public class Employees  {
     List<Employee> employees;
 }
