package com.assignment.kontaktid.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ContactDto (
        UUID id,
        String name,
        String codeName,
        String phone,
        Instant createTime,
        Instant updateTime,
        Instant deleteTime
) {}
