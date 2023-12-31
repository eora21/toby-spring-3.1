package toby.aop_ltw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import toby.aop_ltw.dto.HelloDto;

@RestController
public class GreetingController {
    @GetMapping
    public String greeting(@ModelAttribute HelloDto helloDto) {
        System.out.println();
        System.out.println("=========================");

        if ("err".equals(helloDto.name())) {
            System.out.println("Exception Throw");
            System.out.println("=========================");
            System.out.println();
            throw new IllegalArgumentException();
        }

        System.out.println("GreetingController Active");
        System.out.println("=========================");
        System.out.println();
        return "Hi";
    }
}
