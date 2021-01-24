package ru.itis.javalab.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.javalab.models.Client;
import ru.itis.javalab.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientsRepositoryJdbcTemplateImpl implements ClientsRepository {

    //language=SQL
    private static final String SQL_FIND_ALL = "SELECT * FROM client";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM client WHERE id = ?";
    //language=SQL
    private static final String SQL_FIND_BY_LOGIN = "SELECT * FROM client WHERE login = ?";
    //language=SQL
    private static final String SQL_FIND_BY_UUID = "SELECT * FROM client WHERE uuid = ?";
    //language=SQL
    private static final String SQL_SAVE_CLIENT = "INSERT INTO client (login, password, uuid) VALUES (:login, :password, :uuid)";
    //language=SQL
    private static final String SQL_FIND_ALL_WITH_PAGES = "SELECT * FROM client ORDER BY id LIMIT :limit OFFSET :offset";
    //language=SQL
    private static final String SQL_UPDATE_CLIENT = "UPDATE client SET login  = ?, password = ?, uuid = ?, is_deleted = ? WHERE id = ?";

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ClientsRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private RowMapper<Client> clientRowMapper = (row, i) -> Client.builder()
            .id(row.getLong("id"))
            .login(row.getString("login"))
            .password(row.getString("password"))
            .Uuid(row.getString("uuid"))
            .isDeleted(row.getBoolean("is_deleted"))
            .build();

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, clientRowMapper);
    }

    @Override
    public List<Client> findAll(int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", size);
        params.put("offset", page * size);
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL_WITH_PAGES, params, clientRowMapper);
    }

    @Override
    public Optional<Client> findById(Long id) {
        Client client;
        try {
            client = jdbcTemplate.queryForObject(SQL_FIND_BY_ID, clientRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            client = null;
        }
        return Optional.ofNullable(client);
    }

    @Override
    public void save(Client entity) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("client")
                .usingColumns("login", "password", "uuid")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("login", entity.getLogin(),
                        "password", passwordEncoder.encode(entity.getPassword()),
                        "uuid", entity.getUuid()))
                .getKeys();

        entity.setId((Long) keys.get("id"));
    }

    @Override
    public void update(Client entity) {
        jdbcTemplate.update(SQL_UPDATE_CLIENT, statement -> {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getUuid());
            statement.setBoolean(4, entity.getIsDeleted());
            statement.setLong(5, entity.getId());
        });
    }

    @Override
    public void delete(Client entity) {

    }


    @Override
    public List<Client> findByUuid(String uuid) {
        return jdbcTemplate.query(SQL_FIND_BY_UUID, clientRowMapper, uuid);
    }

    @Override
    public List<Client> findByData(String login) {
        return jdbcTemplate.query(SQL_FIND_BY_LOGIN, clientRowMapper, login);
    }
}
