package pl.neofonie.bak.couchbaseadmin

import logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import pl.neofonie.bak.couchbaseadmin.services.CouchbaseService
import java.io.ByteArrayOutputStream

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
    model["databases"] = bdsList
    model["syncgateway"] = syncgateway
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
    val docInfo = couchbase.getDocument(syncgateway, dbName, docId)

    model.addAttribute("databaseDetails", docInfo)
    model.addAttribute("attachments", docInfo.attachments)

    return "document"
  }

  @GetMapping("/database/{dbName}/{docId}/{attachId}")
  fun serveFile(@PathVariable dbName: String, @PathVariable docId: String, @PathVariable attachId: String, @ModelAttribute syncgateway: SyncGatewayConnection): ModelAndView {
    return ModelAndView(RedirectView("${syncgateway.toUrl()}/$dbName/$docId/$attachId"))
  }
}

data class SyncGatewayConnection(var host: String = "http://nyx.neofonie.de",
                                 var port: Int? = 4985,
                                 var username: String? = null,
                                 var password: String? = null)

//data class SyncDatabase(val name: String, val couchbase: String, val bucket: String, val users: Int)

fun SyncGatewayConnection.toUrl(): String {
  val portTmp = port ?: 0
  return if (portTmp == 0) {
    host
  } else {
    "$host:$portTmp"
  }
}