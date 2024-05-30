package com.cabapp.cabappspringboot.driver.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class S3Config {
    @Value("\${accessKey}")
    private val accessKey: String? = null

    @Value("\${secret}")
    private val secret: String? = null

    @Value("\${region}")
    private val region: String? = null

    @Bean
    fun s3(): AmazonS3 {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secret)


        return AmazonS3ClientBuilder.standard().withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials)).build()
    }
}