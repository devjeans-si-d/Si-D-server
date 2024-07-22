package org.devjeans.sid.domain.launchedProject.service;

import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaunchedProjectService {

    private final LaunchedProjectRepository launchedProjectRepository;

    @Autowired
    public LaunchedProjectService(LaunchedProjectRepository launchedProjectRepository){
        this.launchedProjectRepository = launchedProjectRepository;
    }



}
