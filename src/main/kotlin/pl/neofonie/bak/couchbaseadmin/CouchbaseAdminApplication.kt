package pl.neofonie.bak.couchbaseadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CouchbaseAdminApplication

fun main(args: Array<String>) {
    runApplication<CouchbaseAdminApplication>(*args)
}
