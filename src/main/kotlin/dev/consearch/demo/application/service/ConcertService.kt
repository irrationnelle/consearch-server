package dev.consearch.demo.application.service

import dev.consearch.demo.application.dto.ConcertResponseView
import dev.consearch.demo.application.dto.CreateConcertRequestView
import dev.consearch.demo.domain.Artist
import dev.consearch.demo.domain.Concert
import dev.consearch.demo.repository.ArtistRepository
import dev.consearch.demo.repository.ConcertRepository
import org.springframework.stereotype.Service

@Service
class ConcertService(private val concertRepository: ConcertRepository, private val artistRepository: ArtistRepository) {
    fun addConcert(concert: CreateConcertRequestView): ConcertResponseView {
        val concertFromRequest = concert.toConcert();
        val artistNameToFind = concert.artists[0];
        val artist = artistRepository.findByName(artistNameToFind.name);
        val artistToSave = artist ?: artistRepository.save(Artist(artistNameToFind.name, artistNameToFind.genre))
        val concertToSave = Concert(concertFromRequest.title, concertFromRequest.address, concertFromRequest.price, concertFromRequest.timetable, mutableSetOf(artistToSave));
        val persistConcert = concertRepository.save(concertToSave)
        return ConcertResponseView.of(persistConcert)
    }
}