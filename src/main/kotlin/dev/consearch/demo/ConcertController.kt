package dev.consearch.demo

import dev.consearch.demo.application.dto.CreateConcertRequestView
import dev.consearch.demo.domain.Concert
import dev.consearch.demo.repository.ArtistRepository
import dev.consearch.demo.repository.ConcertRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@CrossOrigin(origins = ["https://consearch.rase.dev"])
class ConcertController(val concertRepository: ConcertRepository, val artistRepository: ArtistRepository) {
    @PostMapping("/concerts")
    fun createConcert(@RequestBody concert: CreateConcertRequestView): ResponseEntity<Concert?> {
        val concertToSave = concert.toConcert();
        val artistNameToFind = concert.artists[0].name;
        val artist = artistRepository.findByName(artistNameToFind);
        val artistToSave = artist ?: artistRepository.save(concert.artists[0])

        concertToSave.addArtist(artistToSave)

        val persistConcert = concertRepository.save(concertToSave)

        val artists = artistRepository.findAll()
        artists.forEach { println(it) }

        val savedConcert = concertRepository.findByTitle(persistConcert.title)

        println(savedConcert);

        return ResponseEntity
             .created(URI.create("/concerts/${persistConcert.id}"))
            .body(persistConcert)
    }

    @GetMapping("/concerts")
    fun retrieveConcerts(): Iterable<Concert?> = concertRepository.findAll()

    @GetMapping("/concerts/{id}")
    fun retrieveConcert(@PathVariable id: Long) = concertRepository.findById(id)
}