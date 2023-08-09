package kafkashop.portal.controller;

import kafkashop.portal.domain.Member;
import kafkashop.portal.service.MemberService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    Properties props = new Properties();
    Producer<String, String> producer;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;

        String bootstrapServers = "localhost:9092";
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    @GetMapping("/join")
    public String createForm() {
        return "/member/joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute Member member, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (!member.getPassword().equals(confirmPassword)) {    // 비밀번호 확인
            model.addAttribute("error", "Passwords do not match.");
            return "/member/join";
        }

        memberService.join(member);

        String topic = "member-join-topic";
        String key = member.getName();
        String message = "join id: "+member.getId() +" name: "+member.getName();

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
        producer.send(record);

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
        Cookie cookie = new Cookie("loggedInMemberName", member.getName());
        cookie.setPath("/");
        response.addCookie(cookie);

        String topic = "member-login-topic";
        String key = member.getName();
        String message = "login id: "+member.getId() +" name: "+member.getName();

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
        producer.send(record);

        return "redirect:/"; // 로그인 성공 시 메인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("loggedInMemberName", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/"; // 로그아웃 후 메인 페이지로 리다이렉트
    }

    @GetMapping("/memberList")
    public String getAllMember(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "/member/memberList";
    }
}

