package kafkabook.kafkashop.controller;

import kafkabook.kafkashop.domain.User;
import kafkabook.kafkashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/signup") // localhost:8080/user/new
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("user/signup")
    public String signUp(@ModelAttribute User user, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        // 비밀번호 확인
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "signup";
        }

        // 여기에서 비밀번호를 암호화하고 다른 필요한 작업을 수행합니다.

        userService.save(user); // 회원 정보를 데이터베이스에 저장하거나 다른 처리를 수행
        return "redirect:/"; // 회원가입이 성공하면 로그인 페이지로 이동
    }
}

