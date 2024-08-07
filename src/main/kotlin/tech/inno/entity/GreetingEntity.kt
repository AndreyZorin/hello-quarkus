package tech.inno.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class GreetingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
    var name: String,
    var greeting: String
) {
    constructor() : this(null, "", "") {}
}