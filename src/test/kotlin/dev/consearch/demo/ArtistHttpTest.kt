package dev.consearch.demo

import com.beust.klaxon.Klaxon
import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.domain.Artist
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class ArtistHttpTest(private val webTestClient: WebTestClient) {
    private val domainUri = "/artists"

    fun createArtistRequest(artist: CreateArtistRequestView): EntityExchangeResult<Artist?> {
        val inputJson = Klaxon().toJsonString(artist)

        return webTestClient.post()
            .uri(domainUri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(inputJson), String::class.java)
            .exchange()
            .expectStatus()
            .isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().exists("Location")
            .expectBody(Artist::class.java)
            .returnResult()
    }
}

