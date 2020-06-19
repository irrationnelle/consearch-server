package dev.consearch.demo.repository

import dev.consearch.demo.domain.Concert
import org.springframework.data.repository.CrudRepository

interface ConcertRepository : CrudRepository<Concert?, Long?> {
    fun findByName(name: String): Concert?
}
