package com.iscte_meta_systems.evoting_server.services.InitializeDBInjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private InitializeDBInjection initializeDBInjection;

    @Override
    public void run(ApplicationArguments args) {
        initializeDBInjection.initializeElections();
    }
}