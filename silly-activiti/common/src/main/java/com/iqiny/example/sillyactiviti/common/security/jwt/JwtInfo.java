package com.iqiny.example.sillyactiviti.common.security.jwt;

import lombok.Data;

@Data
public class JwtInfo {
    String subject;
    Long issueDate;
}