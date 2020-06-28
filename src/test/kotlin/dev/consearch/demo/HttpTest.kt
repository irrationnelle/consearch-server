package dev.consearch.demo

import com.beust.klaxon.Klaxon
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class HttpTest<T>(private val webTestClient: WebTestClient) {
    fun <T> createRequest(businessEntity: Any, domainUri: String, expectClass: Class<T>): EntityExchangeResult<T?> {
        val inputJson = Klaxon().toJsonString(businessEntity)

        return webTestClient.post()
            .uri(domainUri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(inputJson), String::class.java)
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().exists("Location")
            .expectBody(expectClass)
            .returnResult()

    }

}