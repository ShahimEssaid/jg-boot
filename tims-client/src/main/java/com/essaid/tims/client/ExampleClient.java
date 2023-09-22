package com.essaid.tims.client;

import com.essaid.grpc.tims.v0.TimsGrpc;
import com.essaid.grpc.tims.v0.TimsMsg;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ExampleClient {

    public static void main(String[] args) {
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        TimsGrpc.TimsBlockingStub timsService = TimsGrpc.newBlockingStub(localhost);
        TimsMsg.PathRequest request = TimsMsg.PathRequest.newBuilder().build();
        timsService.n3CPaths(request).forEachRemaining(path -> {
            System.out.println(path);
        });
    }
}
