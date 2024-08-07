package tech.inno

import com.google.protobuf.Empty
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import io.grpc.Status
import io.quarkus.grpc.GrpcClient
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase.persist
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.vertx.VertxContextSupport
import jakarta.inject.Inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import tech.inno.entity.GreetingEntity
import tech.inno.repository.GreetingRepository
import java.util.stream.Stream


@QuarkusTest
class GreetingsServiceTest {

    @GrpcClient
    lateinit var client: GreetingsService

    @Inject
    lateinit var greetingRepository: GreetingRepository


    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("CreateTests")
    inner class CreateTests {
        @Test
        fun addGreetingSuccessTest() {
            val name = "NewTestName"
            val greetingDto = client.addGreeting(CreateGreetingDto.newBuilder().setName(name).build())
                .subscribe().asCompletionStage().join().greetingDto
            assertNotNull(greetingDto.id)
            assertNotEquals(0, greetingDto.id)
            assertEquals("Hello, $name.", greetingDto.greeting)
        }

        @ParameterizedTest
        @MethodSource("addGreetingFailedTestSource")
        fun addGreetingFailedTest(request: CreateGreetingDto?) {
            val errorResponse = client.addGreeting(request).subscribe().asCompletionStage().join().errorResponse
            assertNotNull(errorResponse)
            assertEquals(Status.INVALID_ARGUMENT.code.value(), errorResponse.code)
            assertEquals(Status.INVALID_ARGUMENT.toString(), errorResponse.status)
        }

        private fun addGreetingFailedTestSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(null),
                Arguments.arguments(CreateGreetingDto.newBuilder().setName("").build())
            )
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("UpdateTests")
    inner class UpdateTests {
        @Test
        fun updateGreetingSuccessTest() {
            val updatedName = "UpdatedName"
            val id = 1L
            val updatedEntitiesCount =
                client.updateGreeting(UpdateGreetingDto.newBuilder().setId(id).setName(updatedName).build())
                    .subscribe().asCompletionStage().join().rowCountInt.value
            val updatedEntity = VertxContextSupport.subscribeAndAwait {
                Panache.withTransaction { greetingRepository.findById(id) }
            }
            assertNotNull(updatedEntitiesCount)
            assertEquals(1, updatedEntitiesCount)
            assertNotNull(updatedEntity)
            assertEquals(id, updatedEntity.id)
            assertEquals(updatedName, updatedEntity.name)
            assertEquals("Hello, $updatedName.", updatedEntity.greeting)
        }

        @ParameterizedTest
        @MethodSource("updateGreetingFailedTestSource")
        fun updateGreetingFailedTest(request: UpdateGreetingDto?) {
            val errorResponse = client.updateGreeting(request).subscribe().asCompletionStage().join().errorResponse
            assertNotNull(errorResponse)
            assertEquals(Status.INVALID_ARGUMENT.code.value(), errorResponse.code)
            assertEquals(Status.INVALID_ARGUMENT.toString(), errorResponse.status)
        }

        private fun updateGreetingFailedTestSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(null),
                Arguments.arguments(UpdateGreetingDto.newBuilder().build()),
                Arguments.arguments(UpdateGreetingDto.newBuilder().setName("test").build()),
                Arguments.arguments(UpdateGreetingDto.newBuilder().setId(1).build()),
            )
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("DeleteTests")
    inner class DeleteTests {
        @Test
        fun deleteGreetingByNameSuccessTest() {
            val deletedEntitiesCount = client.deleteGreetingByName(StringValue.of(INIT_NAME_2))
                .subscribe().asCompletionStage().join().rowCountLong.value
            val deletedEntities = VertxContextSupport.subscribeAndAwait {
                Panache.withTransaction { greetingRepository.findByName(INIT_NAME_2) }
            }
            assertNotNull(deletedEntitiesCount)
            assertTrue(deletedEntitiesCount >= 1)
            assertTrue(deletedEntities.isEmpty())
        }

        @Test
        fun deleteGreetingByIdSuccessTest() {
            val id = 3L
            val result = client.deleteGreetingById(Int64Value.of(id))
                .subscribe().asCompletionStage().join().result.value
            val deletedEntity = VertxContextSupport.subscribeAndAwait {
                Panache.withTransaction { greetingRepository.findById(id) }
            }
            assertTrue(result)
            assertNull(deletedEntity)
        }

        @ParameterizedTest
        @MethodSource("deleteGreetingByNameFailedTestSource")
        fun deleteGreetingByNameFailedTest(request: StringValue?, status: Status) {
            val errorResponse = client.deleteGreetingByName(request).subscribe().asCompletionStage().join().errorResponse
            assertNotNull(errorResponse)
            assertEquals(status.code.value(), errorResponse.code)
            assertEquals(status.toString(), errorResponse.status)
        }

        private fun deleteGreetingByNameFailedTestSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(null, Status.INVALID_ARGUMENT),
                Arguments.arguments(StringValue.of(""), Status.INVALID_ARGUMENT),
                Arguments.arguments(StringValue.of("WrongName"), Status.NOT_FOUND)
            )
        }

        @ParameterizedTest
        @MethodSource("deleteGreetingByIdFailedTestSource")
        fun deleteGreetingByIdFailedTest(request: Int64Value?, status: Status) {
            val errorResponse = client.deleteGreetingById(request).subscribe().asCompletionStage().join().errorResponse
            assertNotNull(errorResponse)
            assertEquals(status.code.value(), errorResponse.code)
            assertEquals(status.toString(), errorResponse.status)
        }

        private fun deleteGreetingByIdFailedTestSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(null, Status.INVALID_ARGUMENT),
                Arguments.arguments(Int64Value.of(0), Status.INVALID_ARGUMENT),
                Arguments.arguments(Int64Value.of(Long.MAX_VALUE), Status.NOT_FOUND)
            )
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("FetchTests")
    inner class FetchTests {
        @Test
        fun findAllSuccessTest() {
            val greetings = client.findAll(Empty.getDefaultInstance())
                .subscribe().asCompletionStage().join().greetingListDto
            assertFalse(greetings.greetingList.isEmpty())
        }

        @Test
        fun findByNameSuccessTest() {
            val greetings = client.findByName(StringValue.of(INIT_NAME_1))
                .subscribe().asCompletionStage().join().greetingListDto
            assertFalse(greetings.greetingList.isEmpty())
            assertEquals("Hello, $INIT_NAME_1.", greetings.greetingList[0].greeting)
        }

        @ParameterizedTest
        @MethodSource("findByNameFailedTestSource")
        fun findByNameFailedTest(request: StringValue?, status: Status) {
            val errorResponse = client.findByName(request).subscribe().asCompletionStage().join().errorResponse
            assertNotNull(errorResponse)
            assertEquals(status.code.value(), errorResponse.code)
            assertEquals(status.toString(), errorResponse.status)
        }

        private fun findByNameFailedTestSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(null, Status.INVALID_ARGUMENT),
                Arguments.arguments(StringValue.of(""), Status.INVALID_ARGUMENT),
                Arguments.arguments(StringValue.of("WrongName"), Status.NOT_FOUND)
            )
        }
    }

    companion object {

        private const val INIT_NAME_1 = "TestName1"
        private const val INIT_NAME_2 = "TestName2"
        private const val INIT_NAME_3 = "TestName3"

        @JvmStatic
        @BeforeAll
        fun init() {
            VertxContextSupport.subscribeAndAwait {
                Panache.withTransaction {
                    persist(
                        GreetingEntity(null, INIT_NAME_1, "Hello, $INIT_NAME_1."),
                        GreetingEntity(null, INIT_NAME_2, "Hello, $INIT_NAME_2."),
                        GreetingEntity(null, INIT_NAME_3, "Hello, $INIT_NAME_3.")
                    )
                }
            }
        }
    }

}