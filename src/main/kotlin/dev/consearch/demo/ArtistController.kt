package dev.consearch.demo

import dev.consearch.demo.application.dto.CreateArtistRequestView
import dev.consearch.demo.domain.Artist
import dev.consearch.demo.repository.ArtistRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ArtistController (val artistRepository: ArtistRepository){
    @PostMapping("/artists")
    fun createStation(@RequestBody artist: CreateArtistRequestView): ResponseEntity<Artist?> {
        val persistArtist = artistRepository.save(artist.toArtist())

        return ResponseEntity
            .created(URI.create("/artists/${persistArtist.id}"))
            .body(persistArtist)
    }
}