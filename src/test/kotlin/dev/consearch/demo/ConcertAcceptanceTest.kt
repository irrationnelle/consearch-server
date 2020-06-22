package dev.consearch.demo

import dev.consearch.demo.domain.Artist
import dev.consearch.demo.domain.Concert
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ConcertAcceptanceTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    lateinit var concertHttpTest: ConcertHttpTest

    @BeforeEach
    fun setUp() {
        this.concertHttpTest = ConcertHttpTest(webTestClient)
    }

    @DisplayName("공연 정보 등록")
    @Test
    fun createConcert() {
        val concert = Concert("Behemoth")
        val response = concertHttpTest.createConcertRequest(concert)

        assertThat(response.responseBody?.name).isEqualTo(concert.name)
    }

    @DisplayName("공연 목록 받아오기")
    @Test
    fun retrieveConcerts() {
        // given
        val firstConcert = Concert("Behemoth")
        concertHttpTest.createConcertRequest(firstConcert)

        val secondConcert = Concert("Shining")
        concertHttpTest.createConcertRequest(secondConcert)

        // when
        val response = concertHttpTest.retrieveConcerts();

        // then
        assertThat(response.responseBody?.size).isEqualTo(2)
        assertThat(response.responseBody?.get(0)?.name).isEqualTo(firstConcert.name)
        assertThat(response.responseBody?.get(1)?.name).isEqualTo(secondConcert.name)
    }


    @DisplayName("공연 디테일 정보 받기")
    @Test
    fun retrieveConcert() {
        // given
        val concert = Concert("Behemoth")
        val concertId = concertHttpTest.createConcertRequest(concert).responseBody?.id;
        val artist = Artist()

        // when
        val response = concertHttpTest.retrieveConcert(concertId);

        // then
        assertThat(response.responseBody).isNotNull
            .hasFieldOrPropertyWithValue("title", "Behemoth")
            .hasFieldOrPropertyWithValue("address", "Mariboes gate 3-5, 0179 Oslo, Norway")
            .hasFieldOrPropertyWithValue("price", 20000)
            .hasFieldOrPropertyWithValue("timetable", "2020-06-21T21:30:00+09:00")

        assertThat(response.responseBody?.artists).isNotEmpty.containsExactly(artist)
    }
}