package toby.web_mvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import javax.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractDispatcherServletTest implements AfterRunService {
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletConfig config = new MockServletConfig("spring");
    protected MockHttpSession session;

    private ConfigurableDispatcherServlet dispatcherServlet;
    private Class<?>[] classes;
    private String[] locations;
    private String[] relativeLocations;
    private String servletPath;  // servlet이 해당 요청을 처리하려 내장한 경로를 뜻함(spring servlet의 경우 web.xml에 기입한 /app)

    public AbstractDispatcherServletTest setLocations(String ...locations) {
        this.locations = locations;
        return this;
    }

    public AbstractDispatcherServletTest setRelativeLocations(String ...relativeLocations) {
        this.relativeLocations = relativeLocations;
        return this;
    }

    public AbstractDispatcherServletTest setClasses(Class<?> ...classes) {
        this.classes = classes;
        return this;
    }

    public AbstractDispatcherServletTest setServletPath(String servletPath) {
        updateServletPath(servletPath);
        return this;
    }

    private void updateServletPath(String servletPath) {
        if (Objects.isNull(this.request)) {
            this.servletPath = servletPath;
            return;
        }

        this.request.setServletPath(servletPath);
    }

    public AbstractDispatcherServletTest initRequest(String requestUri, String method) {
        this.request = new MockHttpServletRequest(method, requestUri);
        this.response = new MockHttpServletResponse();

        if (Objects.nonNull(this.servletPath)) {
            this.setServletPath(this.servletPath);
        }

        return this;
    }

    public AbstractDispatcherServletTest initRequest(String requestUri, RequestMethod method) {
        return this.initRequest(requestUri, method.toString());
    }

    public AbstractDispatcherServletTest initRequest(String requestUri) {
        return this.initRequest(requestUri, RequestMethod.GET);
    }

    public AbstractDispatcherServletTest addParameter(String name, String value) {
        if (Objects.isNull(this.request)) {
            throw new IllegalStateException("request가 초기화되지 않았습니다.");
        }

        this.request.addParameter(name, value);
        return this;
    }

    public AbstractDispatcherServletTest buildDispatcherServlet() throws ServletException {
        if (Objects.isNull(this.classes) && Objects.isNull(this.locations) && Objects.isNull(this.relativeLocations)) {
            throw new IllegalStateException("classes 혹은 locations가 설정되어 있어야 합니다.");
        }

        this.dispatcherServlet = new ConfigurableDispatcherServlet();
        this.dispatcherServlet.setClasses(this.classes);
        this.dispatcherServlet.setLocations(this.locations);

        if (Objects.nonNull(this.relativeLocations)) {
            this.dispatcherServlet.setRelativeLocations(getClass(), this.relativeLocations);
        }

        this.dispatcherServlet.init(this.config);

        return this;
    }

    public AfterRunService runService() throws ServletException, IOException {
        if (Objects.isNull(this.dispatcherServlet)) {
            buildDispatcherServlet();
        }

        if (Objects.isNull(this.request)) {
            throw new IllegalStateException("request가 준비되지 않았습니다.");
        }

        this.dispatcherServlet.service(this.request, this.response);

        return this;
    }

    public AfterRunService runService(String requestUri) throws ServletException, IOException {
        initRequest(requestUri);
        runService();
        return this;
    }

    @AfterEach
    void tearDown() {
        if (Objects.nonNull(this.dispatcherServlet)) {
            ((ConfigurableWebApplicationContext)this.dispatcherServlet.getWebApplicationContext()).close();
        }
    }

    @Override
    public String getContentAsString() throws UnsupportedEncodingException {
        return this.response.getContentAsString();
    }

    @Override
    public WebApplicationContext getContext() {
        if (Objects.isNull(this.dispatcherServlet)) {
            throw new IllegalStateException("DispatcherServlet이 준비되지 않았습니다.");
        }

        return this.dispatcherServlet.getWebApplicationContext();
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        if (Objects.isNull(this.dispatcherServlet)) {
            throw new IllegalStateException("DispatcherServlet이 준비되지 않았습니다.");
        }

        return this.getContext().getBean(beanType);
    }

    @Override
    public ModelAndView getModelAndView() {
        return this.dispatcherServlet.getModelAndView();
    }

    @Override
    public AfterRunService assertViewName(String viewName) {
        assertThat(this.getModelAndView().getViewName()).isEqualTo(viewName);
        return this;
    }

    @Override
    public AfterRunService assertModel(String name, Object value) {
        assertThat(this.getModelAndView().getModel().get(name)).isEqualTo(value);
        return this;
    }
}
