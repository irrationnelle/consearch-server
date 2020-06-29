package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Artist(val name: String, val genre: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var concerts: MutableList<Concert?> = mutableListOf()
}