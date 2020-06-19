package dev.consearch.demo

import dev.consearch.demo.domain.Concert
import dev.consearch.demo.repository.ConcertRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ConcertController {
    @Autowired
    lateinit var concertRepository: ConcertRepository

    @PostMapping("/concerts")
    fun createStation(): ResponseEntity<Concert>? {
        val concert = Concert("Behemoth");
        val persistConcert = concertRepository.save(concert);

        return ResponseEntity
            .created(URI.create("/concerts/${persistConcert.id}"))
            .body(persistConcert);
    }

    @GetMapping("/concerts")
    fun retrieveConcerts(): ResponseEntity<List<Concert>>? {
        val firstConcert = Concert("Behemoth");
        val secondConcert = Concert("Shining");
        val persistStations: List<Concert> = listOf(firstConcert, secondConcert);

        return ResponseEntity.ok().body(persistStations)
    }
}