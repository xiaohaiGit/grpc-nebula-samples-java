package com.upchina.service;

import cn.com.crsec.agency.chat.ChatBotAgencyGrpc;
import cn.com.crsec.agency.chat.GatewayReply;
import cn.com.crsec.agency.chat.GatewayRequest;
import com.upchina.config.ServerConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

@Service
public class ChatBotAgencyClientService {
    private static final Logger logger = LoggerFactory.getLogger(ChatBotAgencyClientService.class);


    @Resource
    private ServerConfig serverConfig;
    private ManagedChannel channel;
    private ChatBotAgencyGrpc.ChatBotAgencyBlockingStub blockingStub;


    @PostConstruct
    public void init() {
        //String target = "zookeeper:///" + ChatBotAgencyGrpc.SERVICE_NAME;
        //String target = "127.0.0.1:9070";
        //String target = "172.16.11.33:29909";
        String target = serverConfig.getTarget();

        this.channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();

        this.blockingStub = ChatBotAgencyGrpc.newBlockingStub(channel);


        //test
        while (true) {
            try {
                gatewayAccess();
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    public void gatewayAccess() {
        try {

            GatewayRequest request = GatewayRequest.newBuilder()
                    .setTraceId(UUID.randomUUID().toString())
                    .setFuncNo("1001")
                    .setTrdTermcode("1001")
                    .setRequestIp("192.168.6.210")
                    .setFundAccount("1001")
                    .setUserCode("1001")
                    .setUserName("shawn")
                    .setKhBranch("1001")
                    .setCustCert("1001")
                    .setParamStr("{\"name\":\"shawn\"}")
                    .build();

            GatewayReply reply = blockingStub.gatewayAccess(request);

            System.out.println(reply);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
