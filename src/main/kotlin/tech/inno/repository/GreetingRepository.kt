package tech.inno.repository

import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import tech.inno.UpdateGreetingDto
import tech.inno.entity.GreetingEntity

@ApplicationScoped
class GreetingRepository :PanacheRepository<GreetingEntity> {

    fun updateGreeting(request: UpdateGreetingDto) = update(
        "name= :name, greeting = :greeting where id = :id",
        Parameters.with("name", request.name)
            .and("greeting", "Hello, ${request.name}.")
            .and("id", request.id))

    fun findByName(name: String) = find("name", name).list()

    fun deleteByName(name: String) = delete("name", name)

}