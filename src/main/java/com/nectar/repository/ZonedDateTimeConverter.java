package com.nectar.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.ZonedDateTime;

public class ZonedDateTimeConverter implements DynamoDBTypeConverter<String, ZonedDateTime> {


    @Override
    public String convert(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toString();
    }

    @Override
    public ZonedDateTime unconvert(String dateStr) {
        return ZonedDateTime.parse(dateStr);
    }
}