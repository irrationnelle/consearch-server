package dev.consearch.demo.domain

import javax.persistence.*

@Entity
data class ConcertArtist(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        @ManyToOne
        @JoinColumn(name = "concert_id")
        val concert: Concert?,
        @ManyToOne
        @JoinColumn(name = "artist_id")
        val artist: Artist?
) {
}