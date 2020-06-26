package dev.consearch.demo

import dev.consearch.demo.domain.Concert
import dev.consearch.demo.repository.ConcertRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class ConcertController(val concertRepository: ConcertRepository) {

    @PostMapping("/concerts")
    fun createStation(@RequestBody concert: Concert): ResponseEntity<Concert?> {
        val persistConcert = concertRepository.save(concert)

        val persistArtist = artistRepository.findById(concert.artists[0].id);

        return ResponseEntity
            .created(URI.create("/concerts/${persistConcert.id}"))
            .body(concertRepository.save(concert))
    }

    @GetMapping("/concerts")
    fun retrieveConcerts(): Iterable<Concert?> = concertRepository.findAll()

    @GetMapping("/concerts/{id}")
    fun retrieveConcert(@PathVariable id: Long) = concertRepository.findById(id)
}