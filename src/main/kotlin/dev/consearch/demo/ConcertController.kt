package dev.consearch.demo

import dev.consearch.demo.domain.Concert
import dev.consearch.demo.repository.ConcertRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@CrossOrigin(origins = ["https://consearch.rase.dev"])
class ConcertController(val concertRepository: ConcertRepository) {

    @PostMapping("/concerts")
    fun createConcert(@RequestBody concert: Concert): ResponseEntity<Concert?> {
        val persistConcert = concertRepository.save(concert)

        return ResponseEntity
            .created(URI.create("/concerts/${persistConcert.id}"))
            .body(persistConcert)
    }

    @GetMapping("/concerts")
    fun retrieveConcerts(): Iterable<Concert?> = concertRepository.findAll()

    @GetMapping("/concerts/{id}")
    fun retrieveConcert(@PathVariable id: Long) = concertRepository.findById(id)
}