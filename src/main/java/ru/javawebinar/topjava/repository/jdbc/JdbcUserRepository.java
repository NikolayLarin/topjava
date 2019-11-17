package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(DataSourceTransactionManager transactionManager,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, " +
                        "calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }

        final List<Role> roles = new ArrayList<>(user.getRoles());
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate("" +
                            "INSERT INTO user_roles (user_id, role) " +
                            "VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Role role = roles.get(i);
                            ps.setInt(1, user.getId());
                            ps.setString(2, role.name());
                        }

                        @Override
                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        return getUserWithRoles(id, user, jdbcTemplate);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        return getUserWithRoles(user.getId(), user, jdbcTemplate);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> getAll() {
        Map<Integer, User> userMap = new LinkedHashMap<>();

        jdbcTemplate.query("" +
                "SELECT * FROM users u " +
                "  LEFT JOIN user_roles ur " +
                "    ON u.id=ur.user_id" +
                " ORDER BY name, email", rs -> {
            do {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getTimestamp("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                userMap
                        .computeIfAbsent(user.getId(), id -> user)
                        .addRole(getRole(rs));
                String s = "s";
            } while (rs.next());
        });
        return new ArrayList<>(userMap.values());
    }

    private static User getUserWithRoles(int id, User user, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.query(
                "SELECT * FROM user_roles WHERE user_id=?", (rs, rowNum) -> {
                    assert user != null;
                    user.addRole(getRole(rs));
                    return user;
                }, id);
        return user == null ? null :
                user.getRoles() != null ? user :
                        user.setRoles(Collections.emptySet());
    }

    private static Role getRole(ResultSet rs) throws SQLException {
        return Role.valueOf(rs.getString("role"));
    }
}