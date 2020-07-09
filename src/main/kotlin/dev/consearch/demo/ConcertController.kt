package dev.consearch.demo

import dev.consearch.demo.application.dto.ConcertResponseView
import dev.consearch.demo.application.dto.CreateConcertRequestView
import dev.consearch.demo.application.exception.NoDataException
import dev.consearch.demo.application.service.ConcertService
import dev.consearch.demo.repository.ConcertRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@CrossOrigin(origins = ["https://consearch.rase.dev"])
class ConcertController(val concertRepository: ConcertRepository, val concertService: ConcertService) {
    @PostMapping("/concerts")
    fun createConcert(@RequestBody concert: CreateConcertRequestView): ResponseEntity<ConcertResponseView?> {
        val concertResponse = concertService.addConcert(concert)
        return ResponseEntity
            .created(URI.create("/concerts/${concertResponse.id}"))
            .body(concertResponse)
    }

    @GetMapping("/concerts")
    fun retrieveConcerts(): ResponseEntity<List<ConcertResponseView>> = ResponseEntity.ok().body(ConcertResponseView.listOf(concertRepository.findAll()))

    @GetMapping("/concerts/available")
    fun retrieveFilteredConcertsByDate(): ResponseEntity<List<ConcertResponseView>> {
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val formatted: String = currentDate.format(formatter)

        return ResponseEntity.ok().body(ConcertResponseView.listOf(concertRepository.findByTimetableAfter(formatted)))
    }

    @GetMapping("/concerts/{id}")
    fun retrieveConcert(@PathVariable id: Long): ResponseEntity<ConcertResponseView?> = try {
            val persistConcert = concertRepository.findById(id).orElseThrow { NoDataException() };
            ResponseEntity.ok().body(ConcertResponseView.of(persistConcert))
        } catch(e: EmptyResultDataAccessException) {
            ResponseEntity.notFound().build();
        }
}