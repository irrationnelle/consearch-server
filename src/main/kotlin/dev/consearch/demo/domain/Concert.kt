package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Concert(
        var title: String,
        var address: String,
        var price: Int,
        var timetable: String,
        @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "artist_id")
        var artists: MutableSet<Artist> = mutableSetOf(),
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
)
{
}
