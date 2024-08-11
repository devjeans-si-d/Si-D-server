package org.devjeans.sid.domain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobField {

    DESIGNER("Designer"),
    FRONTEND("Frontend"),
    BACKEND("Backend"),
    APP("App"),
    PM("PM");

    private final String jobName;
}
