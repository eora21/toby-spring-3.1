package learningtest.factory_bean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:FactoryBeanTest-context.xml")
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message).isExactlyInstanceOf(Message.class);
        assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
    }

    @Test
    void getFactoryBean() {
        Object factory = context.getBean("&message");
        assertThat(factory).isExactlyInstanceOf(MessageFactoryBean.class);
    }
}
