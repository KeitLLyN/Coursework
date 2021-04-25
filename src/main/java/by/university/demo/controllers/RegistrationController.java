package by.university.demo.controllers;

import by.university.demo.entity.User;
import by.university.demo.entity.dto.CaptchaResponseDto;
import by.university.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService USER_SERVICE;
    private final RestTemplate REST_TEMPLATE;

    @Value("${google.captcha.url}")
    private String captchaUrl;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        String url = String.format(captchaUrl, secret, captchaResponse);
        CaptchaResponseDto response = REST_TEMPLATE.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (response != null && !response.isSuccess()) {
            model.addAttribute("captchaError", true);
            return "registration";
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!USER_SERVICE.addUser(user)) {
            model.addAttribute("isExist", true);
            return "registration";
        }
        return "redirect:/";
    }
}
