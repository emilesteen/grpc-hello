package hello

import hello.HelloGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Holder for Kotlin coroutine-based client and server APIs for hello.Hello.
 */
object HelloGrpcKt {
  @JvmStatic
  val serviceDescriptor: ServiceDescriptor
    get() = HelloGrpc.getServiceDescriptor()

  val sayHelloMethod: MethodDescriptor<HelloOuterClass.sayHelloRequest,
      HelloOuterClass.sayHelloResponse>
    @JvmStatic
    get() = HelloGrpc.getSayHelloMethod()

  /**
   * A stub for issuing RPCs to a(n) hello.Hello service as suspending coroutines.
   */
  @StubFor(HelloGrpc::class)
  class HelloCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT
  ) : AbstractCoroutineStub<HelloCoroutineStub>(channel, callOptions) {
    override fun build(channel: Channel, callOptions: CallOptions): HelloCoroutineStub =
        HelloCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][io.grpc.Status].  If the RPC completes with another status, a
     * corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun sayHello(request: HelloOuterClass.sayHelloRequest): HelloOuterClass.sayHelloResponse
        = unaryRpc(
      channel,
      HelloGrpc.getSayHelloMethod(),
      request,
      callOptions,
      Metadata()
    )}

  /**
   * Skeletal implementation of the hello.Hello service based on Kotlin coroutines.
   */
  abstract class HelloCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for hello.Hello.sayHello.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [io.grpc.Status].  If this method fails with a [java.util.concurrent.CancellationException],
     * the RPC will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun sayHello(request: HelloOuterClass.sayHelloRequest):
        HelloOuterClass.sayHelloResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method hello.Hello.sayHello is unimplemented"))

    final override fun bindService(): ServerServiceDefinition = builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = HelloGrpc.getSayHelloMethod(),
      implementation = ::sayHello
    )).build()
  }
}
