package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.javawebinar.topjava.util.ValidationUtil.validateBean;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final UserRowExtractor userExtractor = new UserRowExtractor();

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validateBean(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            deleteRoles(user.getId());
        }
        saveRoles(user.getId(), new ArrayList<>(user.getRoles()));
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE id=?", userExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE email=?", userExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id ORDER BY name, email", userExtractor);
    }

    private void saveRoles(int userId, List<Role> roles) {
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES(?,?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, userId);
                            ps.setString(2, roles.get(i).name());
                        }

                        public int getBatchSize() {
                            return roles.size();
                        }
                    }
            );
        }
    }

    private void deleteRoles(int userId) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", userId);
    }

    private static class UserRowExtractor implements ResultSetExtractor<List<User>> {
        private final RowMapper<User> userRowMapper = BeanPropertyRowMapper.newInstance(User.class);

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> userMap = new LinkedHashMap<>();
            while (rs.next()) {
                User user = userMap.computeIfAbsent(rs.getInt("id"), u -> {
                    User newUser;
                    try {
                        newUser = userRowMapper.mapRow(rs, rs.getRow());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    newUser.setRoles(new HashSet<>());
                    return newUser;
                });
                Optional.ofNullable(rs.getString("role"))
                        .ifPresent(role -> user.getRoles().add(Role.valueOf(role)));
            }
            return new ArrayList<>(userMap.values());
        }
    }
}
