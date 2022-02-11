package com.nectar.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfiguration {

    @Bean
    public DynamoDBMapper dynamoDBMapper(){
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }
    @Bean
    public AmazonDynamoDB buildAmazonDynamoDB() {
       return  AmazonDynamoDBClientBuilder
               .standard()
               .withEndpointConfiguration(
                       new AwsClientBuilder.EndpointConfiguration("dynamodb.eu-west-2.amazonaws.com"
                               ,"eu-west-2")
                       ).withCredentials(new AWSStaticCredentialsProvider(
                               new BasicAWSCredentials(
                                       "AKIA3BPCPQ7F6VSFWE7X",
                                       "JinYpEPo8asKds1VJgJGlkdqSzqgesqWd22RFQ8b"
                               ))).build();
    }

}
