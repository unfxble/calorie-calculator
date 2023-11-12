package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRowExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Integer, User> userMap = new LinkedHashMap<>();
        while (rs.next()) {
            int userId = rs.getInt("id");
            User user = userMap.computeIfAbsent(userId, k -> {
                User newUser = new User();
                newUser.setId(userId);
                try {
                    newUser.setName(rs.getString("name"));
                    newUser.setEmail(rs.getString("email"));
                    newUser.setPassword(rs.getString("password"));
                    newUser.setRegistered(rs.getDate("registered"));
                    newUser.setEnabled(rs.getBoolean("enabled"));
                    newUser.setCaloriesPerDay(rs.getInt("calories_per_day"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newUser.setRoles(new HashSet<>());
                return newUser;
            });

            String roleName = rs.getString("role");
            if (roleName != null) {
                user.getRoles().add(Role.valueOf(roleName));
            }
        }

        return new ArrayList<>(userMap.values());
    }
}
