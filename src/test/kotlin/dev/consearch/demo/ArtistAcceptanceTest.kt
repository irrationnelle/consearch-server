package dev.consearch.demo

import com.beust.klaxon.Klaxon
import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.application.dto.SearchArtistRequestView
import dev.consearch.demo.domain.Artist
import dev.consearch.demo.domain.Concert
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ArtistAcceptanceTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    val domainUri = "/artists"

    @DisplayName("아티스트 등록을 하지 않았느데 아티스트 이름으로 아티스트 정보 조회를 하면 404 statusCode 를 보여준다.")
    @Test
    fun retrieveArtistNotRegistered() {
        val searchArtist = SearchArtistRequestView("Behemoth")

        val getWithQueryParameter = { uriBuilder: UriBuilder ->
            uriBuilder.path("${domainUri}/").queryParam("name", searchArtist.name).build()
        }

        webTestClient.get().uri(getWithQueryParameter)
            .exchange()
            .expectStatus().isNotFound
    }

    @DisplayName("아티스트 등록을 한다")
    @Test
    fun registerArtist() {
        val artist = CreateArtistRequestView("Behemoth", "BlackMetal")
        val inputJson = Klaxon().toJsonString(artist)

         webTestClient.post()
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
