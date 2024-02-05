package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicController {

    @GetMapping
    fun index() = "success"

    @GetMapping("ping")
    fun ping() = "pong"
}