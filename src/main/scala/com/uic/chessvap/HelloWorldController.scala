package com.uic.chessvap

import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody


@Controller class HelloWorldController {
  final private val counter = new AtomicLong
  private val template = "Hello, %s!"

  @GetMapping(Array("/hello-world"))
  @ResponseBody def sayHello(@RequestParam(name = "name", required = false,
    defaultValue = "Stranger") name: String) =
    new Greeting(counter.incrementAndGet, String.format(HelloWorldController.this.template, name))
}