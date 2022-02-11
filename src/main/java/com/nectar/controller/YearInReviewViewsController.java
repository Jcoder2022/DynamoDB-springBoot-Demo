package com.nectar.controller;

import com.nectar.entity.Employee;
import com.nectar.entity.Employees;
import com.nectar.repository.EmployeeRepository;
import com.nectar.repository.YearInReviewViewsRecord;
import com.nectar.repository.YearInReviewViewsRecordRepository;
import com.nectar.repository.YearInReviewViewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/views")
public class YearInReviewViewsController {

    private final YearInReviewViewsRecordRepository repository;

    @PostMapping
    public void saveYearInReviewView(@RequestBody YearInReviewViewsRecord record){
         repository.save(record);
    }

    @GetMapping("/{yirId}")
    public YearInReviewViewsRecord getYearInReviewViewsRecord(@PathVariable("yirId") String yirId){
        return repository.findById(yirId).get();
    }


    @GetMapping("/yearly-count/{year}")
    public Integer getEmployeesByYear(@PathVariable("year") String year){
        Integer count = repository.getViewsCountForYear(Integer.valueOf(year));
        System.out.println("count = " + count);
         return count;
    }


    @GetMapping("/year/{year}")
    public void getViewsForYear(@PathVariable("year") String year) throws IOException {
        repository.getViewsForYear(Integer.valueOf(year));
        //repository.retrieveMultipleItemsBatchGet();
    }




}
