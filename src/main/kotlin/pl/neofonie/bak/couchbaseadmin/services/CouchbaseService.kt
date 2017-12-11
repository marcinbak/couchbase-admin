package pl.neofonie.bak.couchbaseadmin.services

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.getForObject
import pl.neofonie.bak.couchbaseadmin.AuthedRestTemplate
import pl.neofonie.bak.couchbaseadmin.SyncGatewayConnection
import java.net.URI

@Service
class CouchbaseService @Autowired constructor(private val restTemplate: AuthedRestTemplate) {

  private val logger = logger()

  companion object {
    private val ALL_DBS_REQUEST = "_all_dbs"
    private val CONFIG_REQUEST = "_config"
    private val ALL_DOCS_REQUEST = "_all_docs"
  }

  fun info(input: SyncGatewayConnection): List<String> {
    val url = urlFrom(input)
    return restTemplate.restTemplate.getForObject("$url/$ALL_DBS_REQUEST")!!
  }

  fun allDocs(input: SyncGatewayConnection, db: String): AllDocsResponse {
    val url = urlFrom(input)
    try {
      val response = restTemplate.restTemplate.getForEntity<AllDocsResponse>("$url/$db/$ALL_DOCS_REQUEST")
      return response.body!!
    } catch (e: Exception) {
      logger.error("Error during retrieval of documents list.", e)
    }
    return AllDocsResponse(emptyList(), 0, 0)
  }

  private fun urlFrom(input: SyncGatewayConnection): String {
    val port = input.port ?: 0
    return if (port == 0) {
      input.host
    } else {
      "${input.host}:$port"
    }
  }

}

data class AllDocsResponse @JsonCreator constructor(@JsonProperty("rows") val rows: List<SyncGatewayDoc>,
                                                    @JsonProperty("total_rows") val rowsCount: Int,
                                                    @JsonProperty("update_seq") val updateSeq: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SyncGatewayDoc @JsonCreator constructor(@JsonProperty("key") val key: String,
                                                   @JsonProperty("id") val id: String)