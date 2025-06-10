// src/main/java/com/nando/vibin/payload/LoginRequest.java
package com.nando.vibin.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
