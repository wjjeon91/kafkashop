package kafkabook.kafkashop.controller;

import kafkabook.kafkashop.domain.User;
import kafkabook.kafkashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String createForm() {
        return "/user/signUpForm";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute User user, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        // 비밀번호 확인
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "/user/signUp";
        }

        // 여기에서 비밀번호를 암호화하고 다른 필요한 작업을 수행합니다.

        userService.signup(user); // 회원 정보를 데이터베이스에 저장하거나 다른 처리를 수행
        return "redirect:/"; // 회원가입이 성공하면 로그인 페이지로 이동
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/user/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model, HttpServletResponse response) {
        User user = userService.findByName(name);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password.");
            return "/user/login";
        }

        // 로그인 성공 시 쿠키 생성
        Cookie cookie = new Cookie("loggedInUsername", user.getName());
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/"; // 로그인 성공 시 메인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("loggedInUsername", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 세션 무효화 (선택적으로 사용)
        // request.getSession().invalidate();

        return "redirect:/"; // 로그아웃 후 메인 페이지로 리다이렉트
    }

    @GetMapping("/userList")
    public String getAllUser(Model model) {
        List<User> users = userService.findUsers();
        model.addAttribute("users",users);
        return "/user/userList";
    }
}

