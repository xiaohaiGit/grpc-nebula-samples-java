package com.upchina.service;

import cn.com.crsec.agency.chat.ChatBotAgencyGrpc;
import cn.com.crsec.agency.chat.GatewayReply;
import cn.com.crsec.agency.chat.GatewayRequest;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class ChatBotAgencyServerService extends ChatBotAgencyGrpc.ChatBotAgencyImplBase {

    @Override
    public void gatewayAccess(GatewayRequest request, StreamObserver<GatewayReply> responseObserver) {

        GatewayReply reply = GatewayReply.newBuilder()
                .setResStr("Hello, " + request.getUserName())
                .build();
        System.out.println("name: " + request.getUserName());
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
