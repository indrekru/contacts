package com.assignment.kontaktid.model;

import lombok.Builder;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Contact (
        UUID id,
        @With String name,
        @With String codeName,
        @With String phone,
        Instant createTime,
        Instant updateTime,
        Instant deleteTime
) {}
