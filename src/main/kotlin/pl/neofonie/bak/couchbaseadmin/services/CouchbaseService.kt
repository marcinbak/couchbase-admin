package pl.neofonie.bak.couchbaseadmin.services

import com.fasterxml.jackson.annotation.*
import logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.getForObject
import pl.neofonie.bak.couchbaseadmin.AuthedRestTemplate
import pl.neofonie.bak.couchbaseadmin.SyncGatewayConnection
import pl.neofonie.bak.couchbaseadmin.toUrl
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
    val url = input.toUrl()
    return restTemplate.restTemplate.getForObject("$url/$ALL_DBS_REQUEST")!!
  }

  fun allDocs(input: SyncGatewayConnection, db: String): AllDocsResponse {
    val url = input.toUrl()
    try {
      val response = restTemplate.restTemplate.getForEntity<AllDocsResponse>("$url/$db/$ALL_DOCS_REQUEST")
      return response.body!!
    } catch (e: Exception) {
      logger.error("Error during retrieval of documents list.", e)
    }
    return AllDocsResponse(emptyList(), 0, 0)
  }

  fun getDocument(input: SyncGatewayConnection, db: String, docId: String): DocumentResponse {
    val url = input.toUrl()
    try {
      val response = restTemplate.restTemplate.getForEntity<DocumentResponse>("$url/$db/$docId")
      return response.body!!
    } catch (e: Exception) {
      logger.error("Error during retrieval of documents list.", e)
    }
    return DocumentResponse(emptyMap(), "", "")
  }

}

data class DocumentResponse @JsonCreator constructor(@JsonProperty("_attachments") val attachments: Map<String, AttachmentResponse>,
                                                     @JsonProperty("_id") val id: String,
                                                     @JsonProperty("_rev") val revision: String) {
  val userProperties: MutableMap<String, Any> = mutableMapOf()

  @JsonAnySetter
  fun addUserProperty(name: String, value: Any) {
    userProperties.put(name, value)
  }

}

data class AttachmentResponse @JsonCreator constructor(@JsonProperty("content_type") val contentType: String,
                                                       @JsonProperty("digest") val digest: String,
                                                       @JsonProperty("length") val length: Long,
                                                       @JsonProperty("revpos") val revpos: Int,
                                                       @JsonProperty("stub") val stub: Boolean)

data class AllDocsResponse @JsonCreator constructor(@JsonProperty("rows") val rows: List<SyncGatewayDoc>,
                                                    @JsonProperty("total_rows") val rowsCount: Int,
                                                    @JsonProperty("update_seq") val updateSeq: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SyncGatewayDoc @JsonCreator constructor(@JsonProperty("key") val key: String,
                                                   @JsonProperty("id") val id: String)