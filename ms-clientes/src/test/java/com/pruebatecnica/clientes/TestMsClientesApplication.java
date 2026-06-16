package com.pruebatecnica.clientes;

import org.springframework.boot.SpringApplication;

public class TestMsClientesApplication {

    public static void main(String[] args) {
        SpringApplication.from(MsClientesApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
