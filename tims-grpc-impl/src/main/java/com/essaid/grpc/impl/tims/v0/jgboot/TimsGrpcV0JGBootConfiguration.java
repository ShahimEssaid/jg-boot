package com.essaid.grpc.impl.tims.v0.jgboot;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({TimsService.class})
public class TimsGrpcV0JGBootConfiguration {

}
