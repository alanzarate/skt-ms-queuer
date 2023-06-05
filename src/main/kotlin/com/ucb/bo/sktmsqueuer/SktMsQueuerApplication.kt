package com.ucb.bo.sktmsqueuer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class SktMsQueuerApplication

fun main(args: Array<String>) {
	runApplication<SktMsQueuerApplication>(*args)
}
