package dev.consearch.demo.application.dto

import dev.consearch.demo.domain.Artist

class CreateArtistRequestView(val name: String, val genre: String) {
    fun toArtist(): Artist = Artist(name, genre)
}