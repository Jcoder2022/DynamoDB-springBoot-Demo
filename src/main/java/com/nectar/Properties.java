package com.nectar;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "application")
public class Properties {

  @NotNull
  private Integer httpPoolSize;

  @NotNull
  private Long httpTimeoutMillis;

  @NotNull
  private Long httpKeepAliveMillis;



  @NotNull
  private String yearInReviewBucketName;

  @NotNull
  private Integer insertBatchSize;

  @NotBlank
  private String dynamodbTablePrefix;

  @NotBlank
  private String dynamoDbAutoscalingRoleArn;

  private String dynamoEndpoint;

  @NotNull
  private Integer reviewReportYear;

}
