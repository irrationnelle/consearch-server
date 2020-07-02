package dev.consearch.demo.domain

import javax.persistence.*

@Entity
data class ConcertArtist(
        @ManyToOne(cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "concert_id")
        val concert: Concert?,
        @ManyToOne(cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "artist_id")
        val artist: Artist?
) {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
}