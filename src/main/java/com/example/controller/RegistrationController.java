package com.example.controller;

import com.example.domain.User;
import com.example.domain.dto.CaptchaResponseDto;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${captcha.secret}")
    String captchaSecret;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model){
        String url = String.format(CAPTCHA_URL, captchaSecret, captchaResponse);

        CaptchaResponseDto response =
                restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if(!response.isSuccess()){
            model.addAttribute("captchaError", "Fill captcha");
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())){
            model.addAttribute("passwordError", "Passwords are different or empty!");
        }

        if(bindingResult.hasErrors() || !response.isSuccess()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);

            return  "registration";
        }

        if(!userService.addUser(user)){
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }


        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model){
        boolean isActivated = userService.activateUser(code);

        if(isActivated){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");}
        else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code not found!");}

        return "login";
    }
}
