package com.essaid.jg.boot.grcp;

import com.essaid.jg.proto.v1.Hello;
import com.essaid.jg.proto.v1.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloServiceBootImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> responseObserver) {
        Hello.HelloResponse response = Hello.HelloResponse.newBuilder().setGreeting("Hello back: "+request.getGreeting()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
