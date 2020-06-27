package dev.consearch.demo.repository

import dev.consearch.demo.domain.Artist
import org.springframework.data.repository.CrudRepository

interface ArtistRepository : CrudRepository<Artist?, Long?> {
    fun findByName(name: String): Artist?
}