package dev.consearch.demo.repository

import dev.consearch.demo.domain.Concert
import org.springframework.data.repository.CrudRepository

interface ConcertRepository : CrudRepository<Concert, Long?> {
    fun findByTitle(title: String): Concert?

    fun findByTimetableAfter(now: String): MutableIterable<Concert>
}
