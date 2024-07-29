package org.devjeans.sid.domain.auth.entity;
import lombok.Data;

@Data
public class KakaoProfile {
    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;

    public class KakaoAccount{
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
    }
}


