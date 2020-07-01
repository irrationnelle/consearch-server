package dev.consearch.demo.domain

import javax.persistence.*

@Entity
@Table(name= "concert")
data class Concert(
        val title: String,
        val address: String,
        val price: Int,
        val timetable: String,

        @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinTable(name = "concert_artist",
                joinColumns = [JoinColumn(name = "concert_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "artist_id", referencedColumnName = "id")])
//        @JoinColumn("artist_id")
        var artists: MutableSet<Artist?> = mutableSetOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    fun addArtist(artist: Artist) {
        artists.add(artist)
    }
    override fun toString() = "title: ${this.title}, address: ${this.address}, price: ${this.price}, timetable: ${this.timetable}, artists: ${this.artists}"
}
