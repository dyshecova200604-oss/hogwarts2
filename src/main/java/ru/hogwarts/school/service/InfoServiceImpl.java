package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class InfoServiceImpl implements InfoService {

    @Value("${server.port}")
    private String port;

    @Override
    public String getInfoPort() {
        return port;
    }
}