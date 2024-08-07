package tech.inno.service

import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import io.smallrye.mutiny.Uni
import tech.inno.CreateGreetingDto
import tech.inno.UpdateGreetingDto
import tech.inno.entity.GreetingEntity

interface GreetingService {

    fun addGreeting(request: CreateGreetingDto): Uni<GreetingEntity>

    fun updateGreeting(request: UpdateGreetingDto): Uni<Int>

    fun deleteGreetingByName(request: StringValue): Uni<Long>

    fun deleteGreetingById(request: Int64Value): Uni<Boolean>

    fun findAll(): Uni<List<GreetingEntity>>

    fun findByName(request: StringValue): Uni<List<GreetingEntity>>

}