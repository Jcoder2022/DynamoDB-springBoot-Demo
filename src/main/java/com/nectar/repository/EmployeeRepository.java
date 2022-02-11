package com.nectar.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.nectar.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class EmployeeRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public Employee save (Employee employee)
    {
        dynamoDBMapper.save(employee);
        return employee;
    }

    public Employee getEmployee(String employeeId){
       return dynamoDBMapper.load(Employee.class,employeeId);
    }

    public String deleteEmployee(String employeeId){
        Employee employee = getEmployee(employeeId);
        dynamoDBMapper.delete(employee);
        return employeeId;
    }

    public String updateEmployee(String employeeId,Employee employee){
        DynamoDBSaveExpression query = new DynamoDBSaveExpression();
        query.withExpectedEntry("employeeId",new ExpectedAttributeValue(
                new AttributeValue().
                        withS(employeeId)
                )
        );
        dynamoDBMapper.save(employee,query);
        return employeeId;
    }


    public List<Employee> findByYirId(String yirId) {


//        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
//        eav.put(":year", new AttributeValue().withS("2021"));

        Employee yearInReviewViewsRecord = new Employee();

        int limit = 2;
        DynamoDBQueryExpression<Employee> queryExpression = new DynamoDBQueryExpression<Employee>()
                .withHashKeyValues(yearInReviewViewsRecord)
                .withIndexName("year")
                .withConsistentRead(true);

        QueryResultPage<Employee> results = new QueryResultPage<Employee>();
        List<Employee> objects = new ArrayList<Employee>();
        do {
            if (results.getLastEvaluatedKey() != null) {
                queryExpression.setExclusiveStartKey(results.getLastEvaluatedKey());
            }
            queryExpression.setLimit(limit - objects.size());
            results = dynamoDBMapper.queryPage(Employee.class,queryExpression);
            for (Employee object : results.getResults()) {
                System.out.println(object.getYear());
                objects.add(object);

            }
            //write objects in file
        } while (results.getLastEvaluatedKey() != null);

        return null;
    }


}


