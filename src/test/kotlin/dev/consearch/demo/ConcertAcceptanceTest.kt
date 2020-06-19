package dev.consearch.demo

import com.beust.klaxon.Klaxon
import dev.consearch.demo.domain.Concert
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ConcertAcceptanceTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @DisplayName("공연 정보 등록")
    @Test
    fun createConcert() {
        val concert = Concert("Behemoth");
        val inputJson = Klaxon().toJsonString(concert)

        val response = webTestClient.post()
            .uri("/concerts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(inputJson), String::class.java)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().exists("Location")
            .expectBody(Concert::class.java)
            .returnResult();

        assertThat(response.responseBody?.name).isEqualTo("Behemoth");
    }

    @DisplayName("공연 목록 받아오기")
    @Test
    fun retrieveConcerts() {
        // given
        val firstConcert = Concert("Behemoth");
        val firstInputJson = Klaxon().toJsonString(firstConcert)

        webTestClient.post()
            .uri("/concerts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(firstInputJson), String::class.java)
            .exchange()
            .expectStatus()
            .isCreated()

        val secondConcert = Concert("Shining");
        val secondInputJson = Klaxon().toJsonString(secondConcert)

        webTestClient.post()
            .uri("/concerts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(secondInputJson), String::class.java)
            .exchange()
            .expectStatus()
            .isCreated()

        // when
        val response: EntityExchangeResult<List<Concert>> = webTestClient.get().uri("/concerts")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Concert::class.java)
            .returnResult()

        // then
        assertThat(response.responseBody?.size).isEqualTo(2)
    }
}