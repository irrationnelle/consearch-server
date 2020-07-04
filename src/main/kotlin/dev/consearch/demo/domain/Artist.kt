package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Artist(
        var name: String,
        var genre: String,
        @ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
        var concerts: MutableSet<Concert?> = mutableSetOf(),
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
}