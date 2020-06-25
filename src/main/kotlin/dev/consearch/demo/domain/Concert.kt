package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Concert(val name: String,
              @ManyToMany(fetch = FetchType.LAZY)
              @JoinColumn(name = "artist_id")
              var artists: MutableList<Artist>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}
