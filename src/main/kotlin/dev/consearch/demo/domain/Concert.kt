package dev.consearch.demo.domain

import javax.persistence.*

@Entity
class Concert(val name: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @ManyToMany
    lateinit var artists: MutableList<Artist>
}
