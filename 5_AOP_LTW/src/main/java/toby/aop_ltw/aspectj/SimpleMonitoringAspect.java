package toby.aop_ltw.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SimpleMonitoringAspect {
    @Pointcut("within(toby.aop_ltw.controller..*)")
    private void controllerLayer() {
    }

    @Pointcut("this(toby.aop_ltw.controller.HelloController) && args()")
    private void CGLibProxy() {
    }

    @Pointcut("execution(* toby..* (..)) && @target(org.springframework.web.bind.annotation.RestController)")
    private void controllerAnnotation() {
    }

    @Pointcut("execution(* toby..* (..)) && @target(toby.aop_ltw.annotation.HelloAnnotation)")
    private void helloAnnotation() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void controllerAnnotationExactly() {
    }

    @Pointcut("execution(* toby..* (..)) && @args(toby.aop_ltw.annotation.HelloAnnotation)")
    private void parameterAnnotation() {
    }

    @Pointcut("@annotation(toby.aop_ltw.annotation.MethodAnnotation)")
    private void methodAnnotation() {
    }

    @Pointcut("bean(*Controller)")
    private void controllerBean() {
    }

    @Pointcut("bean(greetingController)")
    private void greetingControllerAOP() {
    }

    @Around("methodAnnotation()")
    public Object printParametersAndReturnVal(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("before");
        Object ret = pjp.proceed();
        System.out.println("after");
        return ret;
    }

    @Before("greetingControllerAOP()")
    public void before(JoinPoint jp) throws Throwable {
        String before = "[before]: ";
        System.out.println(before + jp.getSignature().getDeclaringTypeName());
        System.out.println(before + jp.getSignature().getName());
        for (Object arg : jp.getArgs()) {
            System.out.println(before + arg);
        }
    }

    @AfterReturning(value = "greetingControllerAOP()", returning = "greeting")
    public void afterReturning(String greeting) throws Throwable {
        System.out.println("[afterReturning]: " + greeting);
    }

    @AfterThrowing(value = "greetingControllerAOP()", throwing = "ex")
    public void afterThrowing(IllegalArgumentException ex) {
        System.out.println("[afterThrowing]: " + ex.getClass());
    }

    @After(value = "greetingControllerAOP()")
    public void after() {
        System.out.println("[after]: " + "리소스 반환 및 메서드 실행 결과 로그 등, finally 역할");
    }
}
