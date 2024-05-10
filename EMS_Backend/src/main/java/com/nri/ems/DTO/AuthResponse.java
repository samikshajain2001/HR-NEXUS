package com.nri.ems.DTO;

import java.time.Instant;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
      private String accesstoken;
    private String username;
    private String password;
    private String refreshtoken;
    private Date accesstokenexpiry;
    private Instant refreshtokenExpiry;
}
