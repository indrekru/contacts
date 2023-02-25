package com.assignment.kontaktid.repository;

import com.assignment.kontaktid.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Slf4j
@Repository
public class ContactRepositoryImpl extends NamedParameterJdbcDaoSupport implements ContactRepository {

    public static final String TABLE                = "contact";

    private static final String ID                  = "id";
    private static final String NAME                = "name";
    private static final String CODE_NAME           = "code_name";
    private static final String PHONE               = "phone";
    private static final String CREATE_TIME         = "create_time";
    private static final String UPDATE_TIME         = "update_time";
    private static final String DELETE_TIME         = "delete_time";

    private static final String DELETE_TIME_IS_NULL = DELETE_TIME + " is null";

    private static final String[] INSERT_COLS = {
            ID,
            NAME,
            CODE_NAME,
            PHONE,
            CREATE_TIME,
            UPDATE_TIME
    };

    private static final String[] UPDATE_COLS = {
            NAME,
            CODE_NAME,
            PHONE,
            UPDATE_TIME,
            DELETE_TIME
    };

    public ContactRepositoryImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public List<Contact> findAll() {
        final String sql = "select * from " + TABLE + " where " + DELETE_TIME_IS_NULL;
        return getJdbcTemplate().query(sql, this::mapRow);
    }

    @Override
    public void save(Contact entity) {
        try {
            SqlParameterSource source = createSource(entity);
            String sql = "insert into " + TABLE + " ("
                    + StringUtils.join(INSERT_COLS, ", ")
                    + ") values (" + Arrays.stream(INSERT_COLS).map(p -> ":" + p).collect(Collectors.joining(", ")) +
                    ") on conflict (" + ID + ") do update set " + Arrays.stream(UPDATE_COLS)
                    .map(p -> p + " =:" + p).collect(Collectors.joining(", "));
            getNamedParameterJdbcTemplate().update(sql, source);
        } catch (Exception e) {
            log.error("Failed saving new contact: {}", entity, e);
            throw e;
        }
    }

    private SqlParameterSource createSource(Contact entity) {
        return new MapSqlParameterSource()
                .addValue(ID, (entity.id() == null ? randomUUID() : entity.id()))
                .addValue(NAME, entity.name())
                .addValue(CODE_NAME, entity.codeName())
                .addValue(PHONE, entity.phone())
                .addValue(CREATE_TIME, Optional.ofNullable(entity.createTime()).orElse(Instant.now()).atOffset(ZoneOffset.UTC))
                .addValue(UPDATE_TIME, Instant.now().atOffset(ZoneOffset.UTC))
                .addValue(DELETE_TIME, entity.deleteTime() == null ? null : entity.deleteTime().atOffset(ZoneOffset.UTC));
    }

    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Contact.builder()
                .id(rs.getObject(ID, UUID.class))
                .name(rs.getString(NAME))
                .codeName(rs.getString(CODE_NAME))
                .phone(rs.getString(PHONE))
                .createTime(rs.getTimestamp(CREATE_TIME).toInstant())
                .updateTime(rs.getTimestamp(UPDATE_TIME).toInstant())
                .deleteTime(rs.getTimestamp(DELETE_TIME) == null ? null : rs.getTimestamp(DELETE_TIME).toInstant())
                .build();
    }
}
