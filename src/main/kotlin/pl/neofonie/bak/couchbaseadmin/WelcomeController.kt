package pl.neofonie.bak.couchbaseadmin

import logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WelcomeController {

  private val logger = logger()

  @RequestMapping("/")
  fun welcome(model: Model): String {
    model.addAttribute("syncgateway", SyncGatewayConnection())
    return "welcome"
  }

  @PostMapping("/syncgateway")
  fun syncagtewaySubmit(@ModelAttribute syncgateway: SyncGatewayConnection): String {
    return "result"
  }
}

data class SyncGatewayConnection(var host: String = "http://localhost",
                                 var port: Int? = 4985,
                                 var username: String? = null,
                                 var password: String? = null)