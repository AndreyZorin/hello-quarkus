package tech.inno.controller

import com.google.protobuf.Empty
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import io.grpc.Status
import io.quarkus.grpc.GrpcService
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import tech.inno.*
import tech.inno.mapper.GreetingMapperJava
import tech.inno.service.GreetingService

@GrpcService
class HelloController(
    private val greetingService: GreetingService,
    private val greetingMapper: GreetingMapperJava
) : GreetingsService {

    override fun addGreeting(request: CreateGreetingDto?): Uni<Response> {
        if (request == null || request.name.isEmpty()) {
            Log.error(Status.INVALID_ARGUMENT.toString())
            return getUniErrorResponse(Status.INVALID_ARGUMENT)
        }
        return greetingService.addGreeting(request)
            .map { entity ->
                Log.info("Saved greeting with id: ${entity.id}")
                greetingMapper.mapGreetingEntityToResponse(entity)
            }
    }

    override fun updateGreeting(request: UpdateGreetingDto?): Uni<Response> {
        if (request == null || request.name.isEmpty() || request.id == 0L) {
            return getUniErrorResponse(Status.INVALID_ARGUMENT)
        }
        return greetingService.updateGreeting(request)
            .map { result ->
                if (result == 0) {
                    getErrorResponse(Status.NOT_FOUND)
                }else {
                    greetingMapper.map(result)
                }
            }
    }

    override fun deleteGreetingByName(request: StringValue?): Uni<Response> {
        if (request == null || request.value.isEmpty()) {
            return getUniErrorResponse(Status.INVALID_ARGUMENT)
        }
        return greetingService.deleteGreetingByName(request)
            .map { result ->
                if (result == 0L) {
                    getErrorResponse(Status.NOT_FOUND)
                } else {
                    greetingMapper.map(result)
                }
            }
    }

    override fun deleteGreetingById(request: Int64Value?): Uni<Response> {
        if (request == null || request.value == 0L) {
            return getUniErrorResponse(Status.INVALID_ARGUMENT)
        }
        return greetingService.deleteGreetingById(request)
            .map { result ->
                if (!result) {
                    getErrorResponse(Status.NOT_FOUND)
                } else {
                    greetingMapper.map(result)
                }
            }
    }

    override fun findAll(request: Empty?): Uni<Response> {
        return greetingService.findAll()
            .map { list ->
                greetingMapper.mapListOfGreetingEntitiesToResponse(list)
            }
    }

    override fun findByName(request: StringValue?): Uni<Response> {
        if (request == null || request.value.isEmpty()) {
            return getUniErrorResponse(Status.INVALID_ARGUMENT)
        }
        return greetingService.findByName(request)
            .map { list ->
                if (list.isEmpty()) {
                    getErrorResponse(Status.NOT_FOUND)
                } else {
                    greetingMapper.mapListOfGreetingEntitiesToResponse(list)
                }
            }
    }

    private fun getUniErrorResponse(status: Status): Uni<Response> {
        return Uni.createFrom().item(getErrorResponse(status))
    }

    private fun getErrorResponse(status: Status): Response {
        return Response.newBuilder()
            .setErrorResponse(
                ErrorResponse.newBuilder()
                    .setCode(status.code.value())
                    .setStatus(status.toString())
                    .build()
            ).build()
    }

}