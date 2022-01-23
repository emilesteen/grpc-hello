import hello.HelloGrpcKt
import hello.HelloOuterClass
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService

class HelloServer(private val port: Int) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(ProtoReflectionService.newInstance())
        .addService(BackendService())
        .build()

    fun start(): HelloServer {
        server.start()

        println("Server started, listening on $port")

        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this.stop()
                println("*** server shut down")
            }
        )

        return this
    }

    private fun stop(): HelloServer {
        server.shutdown()

        return this
    }

    fun blockUntilShutdown(): HelloServer {
        server.awaitTermination()

        return this
    }
}

class BackendService : HelloGrpcKt.HelloCoroutineImplBase() {
    override suspend fun sayHello(request: HelloOuterClass.sayHelloRequest): HelloOuterClass.sayHelloResponse {
        return HelloOuterClass.sayHelloResponse.newBuilder()
            .setHello("Hello, ${request.name}")
            .build()
    }
}

fun main() {
    HelloServer(8001)
        .start()
        .blockUntilShutdown()
}