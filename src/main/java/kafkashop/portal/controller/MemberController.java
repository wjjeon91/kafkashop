package kafkashop.portal.controller;

import kafkashop.portal.domain.Member;
import kafkashop.portal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/signUp")
    public String createForm() {
        return "/member/signUpForm";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute Member member, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        // 비밀번호 확인
        if (!member.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "/member/signUp";
        }

        // 여기에서 비밀번호를 암호화하고 다른 필요한 작업을 수행합니다.

        memberService.signup(member); // 회원 정보를 데이터베이스에 저장하거나 다른 처리를 수행
        return "redirect:/"; // 회원가입이 성공하면 로그인 페이지로 이동
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/member/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model, HttpServletResponse response) {
        Member member = memberService.findByName(name);
        if (member == null || !member.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid membername or password.");
            return "/member/login";
        }

        // 로그인 성공 시 쿠키 생성
        Cookie cookie = new Cookie("loggedInMembername", member.getName());
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/"; // 로그인 성공 시 메인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("loggedInMembername", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 세션 무효화 (선택적으로 사용)
        // request.getSession().invalidate();

        return "redirect:/"; // 로그아웃 후 메인 페이지로 리다이렉트
    }

    @GetMapping("/memberList")
    public String getAllMember(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "/member/memberList";
    }
}

