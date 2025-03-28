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
package com.orientsec.demo.client;

import com.orientsec.demo.GreeterGrpc;
import com.orientsec.demo.GreeterReply;
import com.orientsec.demo.GreeterRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 服务消费者
 *
 * @author sxp
 * @since 2019/4/16
 */
public class GreeterClient {
    private static final Logger logger = LoggerFactory.getLogger(GreeterClient.class);

    private final ManagedChannel channel;
    private final ManagedChannel channel2;

    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private final io.grpc.examples.helloworld.GreeterGrpc.GreeterBlockingStub blockingStub2;

//  public static GreeterClient getInstance() {
//    return SingletonHolder.INSTANCE;
//  }

    // 懒汉式单例模式--直到使用时才创建对象
//  private static class SingletonHolder {
//    private static final GreeterClient INSTANCE = new GreeterClient();
//  }

    private GreeterClient(String host, int port) {
        //channel = ManagedChannelBuilder.forAddress(host, port)
        //        .usePlaintext()
        //        .build();

        String target = "zookeeper:///" + GreeterGrpc.SERVICE_NAME;

        channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);


        String target2 = "zookeeper:///" + io.grpc.examples.helloworld.GreeterGrpc.SERVICE_NAME;
        channel2 = ManagedChannelBuilder.forTarget(target2)
//        channel2 = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub2 = io.grpc.examples.helloworld.GreeterGrpc.newBlockingStub(channel2);

    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        channel2.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet() {
        try {
            int no = 100;
            String name = "Alice";
            boolean sex = false;// true:male,false:female
            double salary = 6000.0;
            String desc = "我爱夏天";

            GreeterRequest request = GreeterRequest.newBuilder()
                    .setNo(no)
                    .setName(name)
                    .setSex(sex)
                    .setSalary(salary)
                    .setDesc(desc)
                    .build();

            GreeterReply reply = blockingStub.sayHello(request);

            logger.info(String.valueOf(reply.getSuccess()));
            logger.info(reply.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void greet2() {
        try {
            HelloRequest request = HelloRequest.newBuilder()
                    .setName("shawn")
                    .build();

            HelloReply reply = blockingStub2.sayHello(request);

            logger.info(">>> message : {}", reply.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * main
     */
    public static void main(String[] args) throws Exception {


        Options options = new Options();

        Option hostOpt = new Option("h", "host", true, "ip, hostname");
        Option portOpt = new Option("p", "port", true, "port");
        Option intervalOpt = new Option("i", "interval", true, "loop interval");
        Option countOpt = new Option("c", "count", true, "loop count");

        options.addOption(hostOpt);
        options.addOption(portOpt);
        options.addOption(intervalOpt);
        options.addOption(countOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String host = cmd.getOptionValue("host");
        int port = Integer.parseInt(cmd.getOptionValue("port"));
        long interval = Long.parseLong(cmd.getOptionValue("interval", "1000"));
        long count = Long.parseLong(cmd.getOptionValue("count", "3"));

        System.out.println("host : " + host);
        System.out.println("port : " + port);
        System.out.println("interval : " + interval);
        System.out.println("count : " + count);

        GreeterClient client = new GreeterClient(host, port);

        long num = 0;
        while (true) {
            if (num++ == count) {
                break;
            }

            client.greet2();
            TimeUnit.MILLISECONDS.sleep(interval);
        }

        client.shutdown();
    }
}
