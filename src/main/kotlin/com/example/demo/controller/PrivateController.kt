package com.example.demo.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PrivateController {

    @GetMapping("secured")
    @PreAuthorize("hasRole('USER')")
    fun secured() = "secured"
}