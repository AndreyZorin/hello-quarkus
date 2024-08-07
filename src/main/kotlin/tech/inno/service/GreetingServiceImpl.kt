package tech.inno.service

import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import tech.inno.CreateGreetingDto
import tech.inno.UpdateGreetingDto
import tech.inno.entity.GreetingEntity
import tech.inno.repository.GreetingRepository

@ApplicationScoped
@WithTransaction
class GreetingServiceImpl(
    private val greetingRepository: GreetingRepository
) : GreetingService {

    override fun addGreeting(request: CreateGreetingDto): Uni<GreetingEntity> {
        return greetingRepository.persist(GreetingEntity(null, request.name, "Hello, ${request.name}."))
    }

    override fun updateGreeting(request: UpdateGreetingDto): Uni<Int> {
        return greetingRepository.updateGreeting(request)
    }

    override fun deleteGreetingByName(request: StringValue): Uni<Long> {
        return greetingRepository.deleteByName(request.value)
    }

    override fun deleteGreetingById(request: Int64Value): Uni<Boolean> {
        return greetingRepository.deleteById(request.value)
    }

    override fun findAll(): Uni<List<GreetingEntity>> {
        return greetingRepository.listAll()
    }

    override fun findByName(request: StringValue): Uni<List<GreetingEntity>> {
        return greetingRepository.findByName(request.value)
    }

}