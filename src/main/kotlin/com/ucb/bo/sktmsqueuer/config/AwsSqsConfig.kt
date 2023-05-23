package com.ucb.bo.sktmsqueuer.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver

@Configuration
class AwsSqsConfig {

    @Value("\${cloud.aws.credentials.access-key}")
    lateinit var awsAccessKey: String

    @Value("\${cloud.aws.credentials.secret-key}")
    lateinit var awsSecretKey: String

    @Value("\${cloud.aws.region.static}")
    private val region: String? = null

    fun credentials(): AWSCredentials {
        val credentials: AWSCredentials = BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return credentials
    }

    @Bean
    fun queueMessagingTemplate1(): QueueMessagingTemplate {
        return QueueMessagingTemplate(amazonSQSAsync1());
    }

    @Bean
    @Qualifier("publish")
    @Primary
    fun amazonSQSAsync1(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder.standard()
            .withRegion(Regions.US_WEST_2)
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(awsAccessKey, awsSecretKey)
                )
            )
            .build()
    }

    @Bean
    fun simpleMessageListenerContainerFactory1(): SimpleMessageListenerContainerFactory? {
        val factory = SimpleMessageListenerContainerFactory()
        factory.setAmazonSqs(amazonSQSAsync1())
        return factory
    }

    @Bean
    fun queueMessageHandler1(): QueueMessageHandler? {
        val factory = QueueMessageHandlerFactory()
        factory.setAmazonSqs(amazonSQSAsync1())
        val handler = factory.createQueueMessageHandler()
        val list: MutableList<HandlerMethodArgumentResolver> = ArrayList<HandlerMethodArgumentResolver>()
        val resolver: HandlerMethodArgumentResolver = PayloadArgumentResolver(MappingJackson2MessageConverter())
        list.add(resolver)
        handler.setArgumentResolvers(list)
        return handler
    }
}