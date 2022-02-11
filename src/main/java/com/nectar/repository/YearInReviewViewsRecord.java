package com.nectar.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = YearInReviewViewsRecord.TABLE_NAME)
public class YearInReviewViewsRecord {

    public static final String TABLE_NAME = "year-in-review-views-V1";
    public static final String YEAR_INDEX_NAME = "yearIndex";

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "yirId")
    private String yirId;

    @DynamoDBAttribute(attributeName = "year")
    private Integer year;

    private String cardNumber;

    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime firstViewDate;
}