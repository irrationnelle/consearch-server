package dev.consearch.demo

import com.beust.klaxon.Klaxon
import dev.consearch.demo.domain.Concert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
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

        webTestClient.post()
            .uri("/concerts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(inputJson), String::class.java)
            .exchange()
            .expectStatus()
            .isCreated()
    }
}