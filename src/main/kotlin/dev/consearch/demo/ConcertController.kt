package dev.consearch.demo

import dev.consearch.demo.domain.Concert
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ConcertController {
    @PostMapping("/concerts")
    fun createStation(): ResponseEntity<Concert>? {
        return ResponseEntity
            .created(URI.create("/concerts/1"))
            .build()
    }
}