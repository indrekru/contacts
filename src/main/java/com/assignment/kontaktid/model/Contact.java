package com.assignment.kontaktid.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Contact (
        UUID id,
        String name,
        String codeName,
        String phone,
        Instant createTime,
        Instant updateTime,
        Instant deleteTime
) {}
