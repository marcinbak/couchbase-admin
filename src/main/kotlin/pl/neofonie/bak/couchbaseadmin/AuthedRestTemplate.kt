package pl.neofonie.bak.couchbaseadmin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
class AuthedRestTemplate {

  @Autowired
  private lateinit var builder: RestTemplateBuilder

  val restTemplate: RestTemplate by lazy { createRestTemplate() }
  var basicAuth: BasicAuth? = null

  private fun createRestTemplate(): RestTemplate {
    val builderLocal = basicAuth?.let {
      builder.basicAuthorization(it.username, it.password)
    } ?: builder
    return builderLocal.build()
  }

}

data class BasicAuth(val username: String, val password: String)