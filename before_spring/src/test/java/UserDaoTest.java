import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.dao.CountingConnectionMaker;
import user.dao.CountingDaoFactory;
import user.dao.DaoFactory;
import user.dao.UserDao;
import user.domain.User;

public class UserDaoTest {
    @Test
    void daoTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        User user = new User();
        user.setId("eora21");
        user.setName("김주호");
        user.setPassword("pswd");

        dao.add(user);
        assertThat(dao.getCount()).isOne();

        User getUser = dao.get(user.getId());
        assertThat(user.getId()).isEqualTo(getUser.getId());
        assertThat(user.getPassword()).isEqualTo(getUser.getPassword());
    }

    @Test
    void connectionCountingTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("eora21");
        user.setName("김주호");
        user.setPassword("pswd");

        dao.add(user);

        User getUser = dao.get(user.getId());
        assertThat(user.getId()).isEqualTo(getUser.getId());
        assertThat(user.getPassword()).isEqualTo(getUser.getPassword());

        CountingConnectionMaker connectionMaker =
                context.getBean("connectionMaker", CountingConnectionMaker.class);

        assertThat(connectionMaker.getCounter()).isEqualTo(2);
    }

    @Test
    void applicationContextTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("eora21");
        user.setName("김주호");
        user.setPassword("pswd");

        dao.add(user);

        User getUser = dao.get(user.getId());
        assertThat(user.getId()).isEqualTo(getUser.getId());
        assertThat(user.getPassword()).isEqualTo(getUser.getPassword());
    }

    @Test
    void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User userA = new User("A", "에이", "PA");
        User userB = new User("B", "비", "PB");
        User userC = new User("C", "씨", "PC");

        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        dao.add(userA);
        assertThat(dao.getCount()).isOne();

        dao.add(userB);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(userC);
        assertThat(dao.getCount()).isEqualTo(3);
    }
}