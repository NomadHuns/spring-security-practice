package shop.mtcoding.securityapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<?> hello() {

        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/joinForm")
    public String joinForm() {

        return "joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {

        return "loginForm";
    }

    @PostMapping("/join")
    public String join() {

        return "redirect:/";
    }
}
