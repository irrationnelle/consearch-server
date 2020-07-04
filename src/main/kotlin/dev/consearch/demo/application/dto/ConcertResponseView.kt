package dev.consearch.demo.application.dto

import dev.consearch.demo.domain.Concert

class ConcertResponseView(val id: Long, val title: String, val address: String, val price: Int, val timetable: String, val artists: List<ArtistWihtoutConcertsReponseView>) {
    companion object {
        fun of(concert: Concert): ConcertResponseView {
            val artists = concert.artists.map { it -> ArtistWihtoutConcertsReponseView(it.id, it.name, it.genre)}
            return ConcertResponseView(concert.id, concert.title, concert.address, concert.price, concert.timetable, artists)
        }

        fun listOf(concerts: MutableIterable<Concert>) = concerts.map{it -> of(it)}
    }
}