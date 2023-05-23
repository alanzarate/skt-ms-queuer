package com.ucb.bo.sktmsqueuer.bl

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ucb.bo.sktmsqueuer.dto.MessageOrderDto
import com.ucb.bo.sktmsqueuer.model.MessageOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class AwsQueueBl @Autowired constructor(
    @Qualifier("publish")
    private val sqs: AmazonSQSAsync,
    private val queueMessagingTemplate: QueueMessagingTemplate
){
    private val mapper = jacksonObjectMapper()
    @Value("\${cloud.aws.end-point.uri}")
    lateinit var endpoint:String

    fun sendMessage(messageOrder: MessageOrderDto): String? {
        var finMess = ""
        if(messageOrder.statusCode == "SKT-200"){
            val newMessage = MessageOrder(
                message = "La orden ${messageOrder.orderId} ha sido regsitrada con exito",
                emailReceiver = messageOrder.userMail,
                subject = "Orden ${messageOrder.orderId} para ${messageOrder.userName}"
            )
            val jsonObj = mapper.writeValueAsString(newMessage)
            val res =  queueMessagingTemplate.send(
                endpoint,
                MessageBuilder.withPayload(jsonObj).build()
            )
            println(res)
            return "MESSAGE SENT"


        }else{
            return null
        }




    }
}