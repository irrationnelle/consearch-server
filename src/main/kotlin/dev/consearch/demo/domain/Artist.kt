package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Artist(val name: String, val genre: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    var concertArtists: MutableSet<ConcertArtist?> = mutableSetOf()
}