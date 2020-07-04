package dev.consearch.demo

import dev.consearch.demo.application.dto.ConcertResponseView
import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.application.dto.CreateConcertRequestView
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
    lateinit var concertHttpTest: HttpTest<ConcertResponseView>
    lateinit var artistHttpTest: HttpTest<Artist>

    val domainUri = "/concerts"
    lateinit var createArtist: CreateArtistRequestView
    lateinit var createConcert: CreateConcertRequestView

    @BeforeEach
    fun setUp() {
        concertHttpTest = HttpTest(webTestClient)
        artistHttpTest = HttpTest(webTestClient)

        createArtist = CreateArtistRequestView("Behemoth", "BlackMetal")
        createConcert = CreateConcertRequestView("Behemoth", "Mariboes gate 3-5, 0179 Oslo, Norway", 20000, "2020-06-21T21:30:00+09:00", listOf(createArtist));
    }

    @DisplayName("아티스트와 함께 공연 정보 등록")
    @Test
    fun createConcert() {
        // given
        // BeforeEach 에서 작업

        // when
        val response = concertHttpTest.createRequest(createConcert, domainUri, ConcertResponseView::class.java)

        // then
        assertThat(response.responseBody?.title).isEqualTo(createConcert.title)
        assertThat(response.responseBody?.artists).element(0).hasFieldOrPropertyWithValue("name", createArtist.name)
    }

    @DisplayName("공연 목록 받아오기")
    @Test
    fun retrieveConcerts() {
        // given
        concertHttpTest.createRequest(createConcert, domainUri, ConcertResponseView::class.java)
        val createSecondArtist = CreateArtistRequestView("Shining", "SuicidalBlackMetal")
        val createSecondConcert = CreateConcertRequestView("Shining", "Stanisława Noakowskiego 16, 00-666 Warszawa, Poland", 15000, "2020-07-06T20:00:00+09:00", listOf(createSecondArtist));
        concertHttpTest.createRequest(createSecondConcert, domainUri, ConcertResponseView::class.java)

        // when
        val response = concertHttpTest.retrieveAllRequest(domainUri, Concert::class.java);

        // then
        assertThat(response.responseBody?.size).isEqualTo(2)
        assertThat(response.responseBody?.get(0)?.title).isEqualTo(createConcert.title)
        assertThat(response.responseBody?.get(1)?.title).isEqualTo(createSecondConcert.title)
        assertThat(response.responseBody?.get(1)?.artists).element(0).hasFieldOrPropertyWithValue("name", createSecondArtist.name)
    }


    @DisplayName("공연 디테일 정보 받기")
    @Test
    fun retrieveConcert() {
        // given
        val responseConcert = concertHttpTest.createRequest(createConcert, domainUri, ConcertResponseView::class.java)
        val concertId = responseConcert.responseBody?.id

        // when
        val response = concertHttpTest.retrieveRequest("/concerts/${concertId}", Concert::class.java)

        // then
        assertThat(response.responseBody).isNotNull
            .hasFieldOrPropertyWithValue("title", "Behemoth")
            .hasFieldOrPropertyWithValue("address", "Mariboes gate 3-5, 0179 Oslo, Norway")
            .hasFieldOrPropertyWithValue("price", 20000)
            .hasFieldOrPropertyWithValue("timetable", "2020-06-21T21:30:00+09:00")

        assertThat(response.responseBody?.artists).element(0).hasFieldOrPropertyWithValue("name", createArtist.name)
    }
}