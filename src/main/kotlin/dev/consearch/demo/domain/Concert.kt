package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Concert(val title: String,
              val address: String,
              val price: Int,
              val timetable: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "concert")
    var concertArtists: MutableSet<ConcertArtist?> = mutableSetOf()
}
