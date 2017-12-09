package pl.neofonie.bak.couchbaseadmin

import logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WelcomeController {

  private val logger = logger()

  // inject via application.properties
  @Value("\${welcome.message:test}")
  private val message = "Hello World"

  @RequestMapping("/")
  fun welcome(model: MutableMap<String, Any>): String {
    model.put("message", this.message)
    return "welcome"
  }

}