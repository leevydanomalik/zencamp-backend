package com.bitozen.zencamp.backend.svc.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ipan Taupik Rahman
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {
    private String username;
    private String password;
}
