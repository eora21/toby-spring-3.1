package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import user.domain.User;

public class UserDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id},
                (ResultSet rs, int rowNum) -> {

            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

            return user;
        });
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", int.class);
    }
}
