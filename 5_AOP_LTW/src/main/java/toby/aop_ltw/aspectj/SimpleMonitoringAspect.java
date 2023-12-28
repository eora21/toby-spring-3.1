package toby.aop_ltw.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    @Around("controllerBean()")
    public Object printParametersAndReturnVal(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("before");
        Object ret = pjp.proceed();
        System.out.println("after");
        return ret;
    }
}
