package com.upchina;

import com.orientsec.grpc.common.util.DesEncryptUtils;

public class Test {

    public static void main(String[] args) {

        String password = "bocloud@123";
        String encrypt = DesEncryptUtils.encrypt(password);
        System.out.println(encrypt);

        String cert = "ae1504574695286e";
        String decrypt = DesEncryptUtils.decrypt(cert);
        System.out.println(decrypt);

    }


}
