/*
 * Copyright 2019 Orient Securities Co., Ltd.
 * Copyright 2019 BoCloud Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientsec.demo.server;

import com.orientsec.demo.common.Constants;
import com.orientsec.demo.service.GreeterImpl;
import com.orientsec.demo.service.GreeterImpl2;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 服务提供者
 *
 * @author sxp
 * @since 2019/4/16
 */
public class GreeterServer {
  private static final Logger logger = LoggerFactory.getLogger(GreeterServer.class);

  private Server server;
  private int port = Constants.Port.GREETER_SERVICE_SERVER;

  private void start() throws IOException {

    //server = NettyServerBuilder.forAddress(new InetSocketAddress("172.16.11.248", 9999))
    //        .addService(new GreeterImpl())
    //        .addService(new GreeterImpl2())
    //        .build()
    //        .start();


    server = ServerBuilder.forPort(port)
            .addService(new GreeterImpl())
            .addService(new GreeterImpl2())
            .build()
            .start();

    logger.info("GreeterServer start...");

    Runtime.getRuntime().addShutdownHook(new Thread() {

      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        GreeterServer.this.stop();
        System.err.println("*** GreeterServer shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      logger.info("stop GreeterServer...");
      server.shutdown();
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }


  public static void main(String[] args) throws Exception {
    GreeterServer server = new GreeterServer();
    server.start();

    server.blockUntilShutdown();
  }
}
