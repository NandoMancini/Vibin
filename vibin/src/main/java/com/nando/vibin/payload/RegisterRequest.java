// src/main/java/com/nando/vibin/payload/RegisterRequest.java
package com.nando.vibin.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
