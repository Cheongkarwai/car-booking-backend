package com.volvo.carbookingbackend.configuration;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CustomPageableResolver());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public BeanUtilsBean beanUtilsBean() {
        return new BeanUtilsBean(new ConvertUtilsBean() {
            @Override
            public Object convert(String value, Class clazz) {

                System.out.println(clazz);

                if (clazz.isEnum()) {
                    return Enum.valueOf(clazz, value);
                } else {
                    return super.convert(value, clazz);
                }
            }
        });
    }

//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry){
//        corsRegistry.addMapping("*").allowedOrigins("*")
//                .allowCredentials(true).allowedMethods("*")
//                .allowedHeaders("*");
//    }

//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("*"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(toList()));
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

    @Bean
    public S3TransferManager awsS3TransferManager(AwsS3Configuration awsS3Configuration){
        S3AsyncClient s3AsyncClient =
                S3AsyncClient.crtBuilder()
                        .region(Region.AP_SOUTHEAST_1)
                        .endpointOverride(URI.create(awsS3Configuration.getEndpoint()))
                        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Configuration.getAccessKey(),awsS3Configuration.getSecretKey())))
                        .minimumPartSizeInBytes(8 * MB)
                        .build();

        return S3TransferManager.builder()
                        .s3Client(s3AsyncClient)
                        .build();
    }

    @Bean
    public S3Client awsClient(AwsS3Configuration awsS3Configuration) {
        return S3Client.builder()
                .region(Region.of(awsS3Configuration.getRegion()))
                .endpointOverride(URI.create(awsS3Configuration.getEndpoint()))
                .forcePathStyle(true)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Configuration.getAccessKey(),awsS3Configuration.getSecretKey())))
                .build();
    }
}
