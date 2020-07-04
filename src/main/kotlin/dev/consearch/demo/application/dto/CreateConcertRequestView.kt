package dev.consearch.demo.application.dto

import dev.consearch.demo.domain.Concert

class CreateConcertRequestView(val title: String,
                               val address: String,
                               val price: Int,
                               val timetable: String,
                               val artists: List<CreateArtistRequestView>) {
    fun toConcert(): Concert = Concert(title, address, price, timetable)
}