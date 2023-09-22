package com.essaid.grpc.impl.tims.v0.jgboot;

import com.essaid.grpc.tims.v0.TimsGrpc;
import com.essaid.grpc.tims.v0.TimsMsg;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TimsService extends TimsGrpc.TimsImplBase  {

    @Override
    public void n3CPaths(TimsMsg.PathRequest request, StreamObserver<TimsMsg.Path> responseObserver) {
        TimsMsg.Path path = TimsMsg.Path.newBuilder().addConcepts(TimsMsg.Concept.newBuilder().setCode("Hello code").setLabel("Concpet label")).build();
        responseObserver.onNext(path);
        responseObserver.onCompleted();
    }
}
