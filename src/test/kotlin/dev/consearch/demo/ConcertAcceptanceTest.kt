package dev.consearch.demo

import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.application.dto.SearchArtistRequestView
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
import org.springframework.web.util.UriBuilder

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ConcertAcceptanceTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    lateinit var concertHttpTest: HttpTest<Concert>
    lateinit var artistHttpTest: HttpTest<Artist>

    val domainUri = "/concerts"

    @BeforeEach
    fun setUp() {
        concertHttpTest = HttpTest(webTestClient)
        artistHttpTest = HttpTest(webTestClient)
    }

    @DisplayName("등록한 아티스트를 조회하여 공연 정보 등록")
    @Test
    fun createConcertWhenArtistIsAlreadyRegistered() {
        // given
        val artist = Artist("Behemoth", "BlackMetal")

        val concert = CreateConcertRequestView("Behemoth", "Mariboes gate 3-5, 0179 Oslo, Norway", 20000,"2020-06-21T21:30:00+09:00", mutableListOf(artist));
        val response = concertHttpTest.createRequest(concert, domainUri, CreateConcertRequestView::class.java)

        // then
        assertThat(response.responseBody?.title).isEqualTo(concert.title)
    }

    @DisplayName("아티스트를 조회한 이후 아티스트가 없어 아티스트를 등록한 이후 공연 정보 등록")
    @Test
    fun createConcertWithoutRegisteredArtist() {
        // given
        val artist = CreateArtistRequestView("Behemoth", "BlackMetal")
        val searchArtist = SearchArtistRequestView(artist.name);
        val getWithQueryParameter = { uriBuilder: UriBuilder ->
            uriBuilder.path("/artists/").queryParam("name", searchArtist.name).build()
        }
         webTestClient.get().uri(getWithQueryParameter).exchange().expectStatus().isNotFound

        // when
        val registeredArtist = artistHttpTest.createRequest(artist, "/artists", Artist::class.java).responseBody
        val concert = Concert("Behemoth", "Mariboes gate 3-5, 0179 Oslo, Norway", 20000,"2020-06-21T21:30:00+09:00", mutableListOf(registeredArtist) );
        val response = concertHttpTest.createRequest(concert,domainUri, Concert::class.java)

        // then
        assertThat(response.responseBody?.title).isEqualTo(concert.title)
    }

    @DisplayName("공연 목록 받아오기")
    @Test
    fun retrieveConcerts() {
        // given
        val artist = CreateArtistRequestView("Behemoth", "BlackMetal")
        val responseArtist = artistHttpTest.createRequest(artist, "/artists", Artist::class.java).responseBody
        val firstConcert = Concert("Behemoth", "Mariboes gate 3-5, 0179 Oslo, Norway", 20000,"2020-06-21T21:30:00+09:00", mutableListOf(responseArtist));
        concertHttpTest.createRequest(firstConcert, domainUri, Concert::class.java)

        val secondArtist = CreateArtistRequestView("Shining", "SuicidalBlackMetal")
        val responseSecondArtist = artistHttpTest.createRequest(secondArtist, "/artists", Artist::class.java).responseBody
        val secondConcert = Concert("Shining", "Mariboes gate 3-5, 0179 Oslo, Norway", 20000,"2020-06-21T21:30:00+09:00", mutableListOf(responseSecondArtist))
        concertHttpTest.createRequest(secondConcert, domainUri, Concert::class.java)

        // when
        val response = concertHttpTest.retrieveAllRequest(domainUri, Concert::class.java);

        // then
        assertThat(response.responseBody?.size).isEqualTo(2)
        assertThat(response.responseBody?.get(0)?.title).isEqualTo(firstConcert.title)
        assertThat(response.responseBody?.get(0)?.artists?.get(0)).isEqualToComparingFieldByField(responseArtist)
        assertThat(response.responseBody?.get(1)?.title).isEqualTo(secondConcert.title)
    }


    @DisplayName("공연 디테일 정보 받기")
    @Test
    fun retrieveConcert() {
        // given
        val artist = CreateArtistRequestView("Behemoth", "BlackMetal")
        val createdArtist = artistHttpTest.createRequest(artist, "/artists", Artist::class.java).responseBody
        val concert = Concert("Behemoth", "Mariboes gate 3-5, 0179 Oslo, Norway", 20000,"2020-06-21T21:30:00+09:00", mutableListOf(createdArtist) );
        val concertId = concertHttpTest.createRequest(concert, "/concerts", Concert::class.java).responseBody?.id;

        // when
        val response = concertHttpTest.retrieveRequest("/concerts/${concertId}", Concert::class.java)

        // then
        assertThat(response.responseBody).isNotNull
            .hasFieldOrPropertyWithValue("title", "Behemoth")
            .hasFieldOrPropertyWithValue("address", "Mariboes gate 3-5, 0179 Oslo, Norway")
            .hasFieldOrPropertyWithValue("price", 20000)
            .hasFieldOrPropertyWithValue("timetable", "2020-06-21T21:30:00+09:00")

        assertThat(response.responseBody?.artists).isNotEmpty.extracting("name").contains(artist.name)
    }
}