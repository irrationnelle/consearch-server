package dev.consearch.demo.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}