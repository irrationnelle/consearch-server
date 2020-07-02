package dev.consearch.demo.repository

import dev.consearch.demo.domain.ConcertArtist
import org.springframework.data.repository.CrudRepository

interface ConcertArtistRepository : CrudRepository<ConcertArtist?, Long?> {
}