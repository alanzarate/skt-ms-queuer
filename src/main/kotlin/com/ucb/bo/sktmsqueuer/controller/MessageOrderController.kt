package com.ucb.bo.sktmsqueuer.controller

import com.ucb.bo.sktmsqueuer.bl.AwsQueueBl
import com.ucb.bo.sktmsqueuer.dto.MessageOrderDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/email")
class MessageOrderController @Autowired constructor(
    private val awsQueueBl: AwsQueueBl
){
    /*
    POST /api/v1/email/order
    request body:
    {
      orderId: String,
      userMail: String,
      userName: String,
      statusCode: SKT-200
    }

    EXAMPLE:
    {
      "orderId": "456789-98749",
      "userMail": "alan.zarate@ucb.edu.bo",
      "userName": "lanplay",
      "statusCode": "SKT-200"
    }
     */
    @PostMapping("/order")
    fun sendOrderEmail(
        @RequestBody body: MessageOrderDto,
    ):ResponseEntity<String>{
        try{
            val res = awsQueueBl.sendMessage(body)
                ?: throw Exception("fallos en la matrix XD")
            return ResponseEntity(res, HttpStatus.OK)
        }catch (ex: Exception){
            return ResponseEntity(ex.message, HttpStatus.FAILED_DEPENDENCY)
        }


    }
}