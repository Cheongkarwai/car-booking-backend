package com.volvo.carbookingbackend.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3Configuration {

    private String secretKey;

    private String accessKey;

    private String endpoint;

    private String bucketName;

    private String region;
}
