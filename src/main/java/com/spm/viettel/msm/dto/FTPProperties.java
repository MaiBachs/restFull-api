package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FTPProperties {
    private String server;
    private String username;
    private String password;
    private int port;
    private int keepAliveTimeout;
    private boolean autoStart;
}
