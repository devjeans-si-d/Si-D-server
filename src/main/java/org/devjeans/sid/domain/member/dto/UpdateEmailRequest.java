package org.devjeans.sid.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateEmailRequest {
    @Email
    private String email;
}
