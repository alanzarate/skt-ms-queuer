package com.ucb.bo.sktmsqueuer.model

data class MessageOrder(
    var message:String,
    var emailReceiver: String,
    var subject: String,
) {
}