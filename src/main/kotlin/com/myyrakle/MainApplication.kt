package com.myyrakle

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.myyrakle", "com.myyrakle.config")
class MainApplication

fun main(args: Array<String>) {
	runApplication<MainApplication>(*args)
}
