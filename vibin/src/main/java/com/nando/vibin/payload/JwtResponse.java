// src/main/java/com/nando/vibin/payload/JwtResponse.java
package com.nando.vibin.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
}
