package pl.neofonie.bak.couchbaseadmin

import logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pl.neofonie.bak.couchbaseadmin.services.CouchbaseService

@Controller
class WelcomeController @Autowired constructor(private val couchbase: CouchbaseService) {

  private val logger = logger()

  @RequestMapping("/")
  fun welcome(model: Model): String {
    model.addAttribute("syncgateway", SyncGatewayConnection())
    return "welcome"
  }

  @PostMapping("/syncgateway")
  fun syncagtewaySubmit(model: Model, @ModelAttribute syncgateway: SyncGatewayConnection): String {
    logger.info(syncgateway.toString())
    val bdsList = couchbase.info(syncgateway)
    model.addAttribute("databases", bdsList)
    return "databases_list"
  }

  @GetMapping("/database/{dbName}")
  fun databaseInfo(@PathVariable dbName: String, model: Model, @ModelAttribute syncgateway: SyncGatewayConnection): String {
    val allDocs = couchbase.allDocs(syncgateway, dbName)
    model.addAttribute("database", dbName)
    model.addAttribute("allDocs", allDocs)

    return "database"
  }

  @GetMapping("/database/{dbName}/{docId}")
  fun documentInfo(@PathVariable dbName: String, @PathVariable docId: String, model: Model, @ModelAttribute syncgateway: SyncGatewayConnection): String {
    val allDocs = couchbase.allDocs(syncgateway, dbName)

    return "document"
  }
}

data class SyncGatewayConnection(var host: String = "http://localhost",
                                 var port: Int? = 4985,
                                 var username: String? = null,
                                 var password: String? = null)

//data class SyncDatabase(val name: String, val couchbase: String, val bucket: String, val users: Int)