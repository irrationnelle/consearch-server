package dev.consearch.demo

import dev.consearch.demo.domain.Concert
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ConcertController {
    @PostMapping("/concerts")
    fun createStation(): ResponseEntity<Concert>? {
        return ResponseEntity
            .created(URI.create("/concerts/1"))
            .build()
    }

    @GetMapping("/concerts")
    fun retrieveConcerts(): ResponseEntity<List<Concert>>? {
        val firstConcert = Concert("Behemoth");
        val secondConcert = Concert("Shining");
        val persistStations: List<Concert> = listOf(firstConcert, secondConcert);

        return ResponseEntity.ok().body(persistStations)
    }
}