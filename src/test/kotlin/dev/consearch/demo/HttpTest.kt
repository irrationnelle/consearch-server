package dev.consearch.demo

import com.beust.klaxon.Klaxon
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI

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

    fun <T> retrieveRequest(uriSetting: (uriBuilder: UriBuilder) -> URI, expectClass: Class<T>)
            : EntityExchangeResult<T?> = webTestClient.get()
        .uri(uriSetting)
        .exchange()
        .expectStatus().isOk
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(expectClass)
        .returnResult()

    fun <T> retrieveRequest(uriString: String, expectClass: Class<T>)
            : EntityExchangeResult<T?> = webTestClient.get()
        .uri(uriString)
        .exchange()
        .expectStatus().isOk
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(expectClass)
        .returnResult()


    fun <T> retrieveAllRequest(uriString: String, expectClass: Class<T>): EntityExchangeResult<List<T?>>
        = webTestClient.get().uri(uriString)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(expectClass)
            .returnResult()

}