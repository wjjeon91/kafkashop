package kafkashop.portal.controller;

import kafkashop.portal.domain.Board;
import kafkashop.portal.domain.Member;
import kafkashop.portal.service.BoardService;
import kafkashop.portal.service.MemberService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Properties;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    Properties props = new Properties();
    Producer<String, String> producer;

    @Autowired
    public BoardController(BoardService boardService, MemberService memberService) {
        this.boardService = boardService;
        this.memberService = memberService;

        String bootstrapServers = "localhost:9092";
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    @GetMapping("/list")
    public String list(Model model, @CookieValue(value = "loggedInMemberName", required = false) String loggedInMemberName) {
        if (loggedInMemberName == null) {
            System.out.println("로그인 후 이용하세요");
            return "redirect:/";
        }
        model.addAttribute("boards", boardService.getAllBoards());
        return "/board/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, @CookieValue(value = "loggedInMemberName", required = false) String loggedInMemberName) {
        Board board = boardService.findById(id);
        if (board == null) {
            return "redirect:/board/list"; // 해당 게시물이 없으면 목록 페이지로 이동
        } else if( board.getWriterName().equals(loggedInMemberName) ){
            model.addAttribute("board", board);
            model.addAttribute("isEditable", true);
        } else{
            model.addAttribute("board", board);
        }
        return "/board/view";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "/board/writeForm";
    }

    @PostMapping("/write")
    public String write(@RequestParam String title, @RequestParam String content, @CookieValue(value = "loggedInMemberName", required = false) String loggedInMemberName) {
        if (loggedInMemberName == null) {
            System.out.println("로그인 후 이용하세요");
            return "redirect:/";
        } else {
            Board board = new Board();
            board.setTitle(title);
            board.setContent(content);
            board.setCreatedAt(LocalDateTime.now());

            Member writer = memberService.findByName(loggedInMemberName);
            board.setWriter(writer);
            board.setWriterName(writer.getName());

            boardService.saveBoard(board);

            String topic = "board-topic";
            String key = board.getTitle();
            String message = "write id: "+board.getId() +" title: "+board.getTitle();

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
            producer.send(record);

            return "redirect:/board/list";
        }
    }

    // 수정 버튼을 눌렀을 때의 동작
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, @CookieValue(value = "loggedInMemberName", required = false) String loggedInMemberName) {
        if (loggedInMemberName == null) {
            System.out.println("로그인 후 이용하세요");
            return "redirect:/";
        }

        Board board = boardService.findById(id);
        if (board == null) {
            return "redirect:/board/list";
        }

        // 현재 로그인한 사용자가 작성자인 경우에만 수정 가능하도록 체크
        if (!board.getWriter().getName().equals(loggedInMemberName)) {
            System.out.println("본인 글만 수정 가능합니다.");
            return "redirect:/board/list";
        }

        model.addAttribute("board", board);
        return "/board/editForm";
    }

    // 수정 내용을 제출했을 때의 동작
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @RequestParam String title, @RequestParam String content, @CookieValue(value = "loggedInMemberName", required = false) String loggedInMemberName) {
        if (loggedInMemberName == null) {
            System.out.println("로그인 후 이용하세요");
            return "redirect:/";
        }

        Board board = boardService.findById(id);
        if (board == null) {
            return "redirect:/board/list";
        }

        if (!board.getWriter().getName().equals(loggedInMemberName)) {
            System.out.println("본인 글만 수정 가능합니다.");
            return "redirect:/board/list";
        }

        board.setTitle(title);
        board.setContent(content);
        boardService.saveBoard(board);

        String topic = "board-topic";
        String key = board.getTitle();
        String message = "edit id: "+board.getId() +" title: "+board.getTitle();

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
        producer.send(record);

        return "redirect:/board/view/" + id;
    }

    // 삭제 버튼을 눌렀을 때의 동작
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @CookieValue(value = "loggedInMemberName", required = false) String loggedInMemberName) {
        if (loggedInMemberName == null) {
            System.out.println("로그인 후 이용하세요");
            return "redirect:/";
        }

        Board board = boardService.findById(id);
        if (board == null) {
            return "redirect:/board/list";
        }

        if (!board.getWriter().getName().equals(loggedInMemberName)) {
            System.out.println("본인 글만 삭제 가능합니다.");
            return "redirect:/board/list";
        }

        boardService.deleteBoard(id);

        String topic = "board-topic";
        String key = board.getTitle();
        String message = "delete id: "+board.getId() +" title: "+board.getTitle();

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
        producer.send(record);

        return "redirect:/board/list";
    }
}
