package dev.consearch.demo.domain

import javax.persistence.*

@Entity
@Table(name = "artist")
data class Artist(
        val name: String,
        val genre: String,

        @ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists", cascade = [CascadeType.ALL])
        var concerts: MutableSet<Concert?> = mutableSetOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    override fun toString() = "name: ${this.name}, genre: ${this.genre}, concerts: ${this.concerts}"
}