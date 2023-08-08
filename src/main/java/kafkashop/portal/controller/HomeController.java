package kafkashop.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model, @CookieValue(value = "loggedInMembername", required = false) String loggedInMembername) {
        if (loggedInMembername != null) {
            // 로그인 상태인 경우, 로그아웃 링크를 보여줌
            model.addAttribute("loggedInMembername", loggedInMembername);
            model.addAttribute("isAuthenticated", true);
        } else {
            // 비로그인 상태인 경우, 로그인 링크를 보여줌
            model.addAttribute("isAuthenticated", false);
        }
        return "home";
    }

}