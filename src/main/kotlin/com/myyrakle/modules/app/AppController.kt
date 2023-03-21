package com.myyrakle.modules.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AppController {
    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }
}