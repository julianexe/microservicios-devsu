package com.pruebatecnica.cuentas;

import org.springframework.boot.SpringApplication;

public class TestMsCuentasApplication {

    public static void main(String[] args) {
        SpringApplication.from(MsCuentasApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
