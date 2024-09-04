package org.devjeans.sid.domain.common;

import net.bytebuddy.build.AccessControllerPlugin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "ok";
    }
}
