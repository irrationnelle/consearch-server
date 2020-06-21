package dev.consearch.demo

import com.beust.klaxon.Klaxon
import dev.consearch.demo.domain.Concert
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class ConcertHttpTest(private val webTestClient: WebTestClient) {
    private val domainUri = "/concerts"

    fun createConcertRequest(concert: Concert): EntityExchangeResult<Concert?> {
        val inputJson = Klaxon().toJsonString(concert)

        return webTestClient.post()
                .uri(domainUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String::class.java)
                .exchange()
                .expectStatus()
                .isCreated
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(Concert::class.java)
                .returnResult()
    }

    fun retrieveConcert(id: Long): EntityExchangeResult<Concert?> {
        val uri = "${domainUri}/${id}"

        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Concert::class.java)
                .returnResult()
    }

    fun retrieveConcerts(): EntityExchangeResult<List<Concert?>> {
        return webTestClient.get().uri(domainUri)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Concert::class.java)
                .returnResult()
    }
}