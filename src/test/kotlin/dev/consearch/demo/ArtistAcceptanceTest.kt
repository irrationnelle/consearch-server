package dev.consearch.demo

import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.application.dto.SearchArtistRequestView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ArtistAcceptanceTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    lateinit var artistHttpTest: ArtistHttpTest

    @BeforeEach
    fun setUp() {
        artistHttpTest = ArtistHttpTest(webTestClient)
    }

    val domainUri = "/artists"

    @DisplayName("아티스트 등록을 하지 않았는데 아티스트 이름으로 아티스트 정보 조회를 하면 404 statusCode 를 보여준다.")
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

        val responseArtist = artistHttpTest.createArtistRequest(artist)

        assertThat(responseArtist).hasFieldOrPropertyWithValue("name", artist.name)
            .hasFieldOrPropertyWithValue("genre", artist.genre)
    }

    @DisplayName("등록한 아티스트를 조회해서 가져온다")
    @Test
    fun retrieveArtistRegistered() {
        val searchArtist = SearchArtistRequestView("Behemoth")

        val getWithQueryParameter = { uriBuilder: UriBuilder ->
            uriBuilder.path("${domainUri}/").queryParam("name", searchArtist.name).build()
        }

        webTestClient.get().uri(getWithQueryParameter)
            .exchange()
            .expectStatus().isNotFound

    }
}
