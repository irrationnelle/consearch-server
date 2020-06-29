package dev.consearch.demo

import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.domain.Artist
import dev.consearch.demo.domain.Concert
import dev.consearch.demo.repository.ArtistRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@CrossOrigin(origins = ["https://consearch.rase.dev"])
class ArtistController (val artistRepository: ArtistRepository){
    @PostMapping("/artists")
    fun createStation(@RequestBody artist: CreateArtistRequestView): ResponseEntity<Artist?> {
        val persistArtist = artistRepository.save(artist.toArtist())

        return ResponseEntity
            .created(URI.create("/artists/${persistArtist.id}"))
            .body(persistArtist)
    }

    @GetMapping("/artists")
    fun retrieveArtists(@RequestParam name: String): ResponseEntity<Artist?> {
        val artist = artistRepository.findByName(name)
        return if(artist != null) ResponseEntity.ok(artist) else ResponseEntity.notFound().build();
    }
}