package com.nectar.entity;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.amazonaws.services.dynamodbv2.util.TableUtils.createTableIfNotExists;
import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamoTableCreator {

    private final AmazonDynamoDB amazonDynamoDB;

    public void createTable(CreateTableRequest createTableRequest) {
        if (createTableIfNotExists(amazonDynamoDB, createTableRequest)) {
            try {
                TableUtils.waitUntilActive(amazonDynamoDB, createTableRequest.getTableName());
                log.info(format("Created DynamoDB table=%s", createTableRequest.getTableName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            log.info(format("DynamoDB table=%s already exists. Table creation skipped",
                    createTableRequest.getTableName()));
        }
    }
}
