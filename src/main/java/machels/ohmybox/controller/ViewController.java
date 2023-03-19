package machels.ohmybox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {

    @GetMapping("/")
    public String Home(Model model) {
        String rootDirectory = "/ohmybox";
        model.addAttribute("rootDirectory", rootDirectory);
        return "index";
    }

    @GetMapping("/signup")
    public String SignupPage() {
        return "signup";
    }

    // TODO: User 생성 후, rootDirectory 가 생기면 이 함수 삭제
    @GetMapping("/ohmybox")
    public String rootDirectory(Model model) {
        System.out.println("ohmybox");
        model.addAttribute("path", null);
        model.addAttribute("directoryId", 0);
        return "directory";
    }

    @GetMapping("/ohmybox/{directoryId}")
    public String Directory(@PathVariable(value = "directoryId", required = false) String directoryId, Model model) {
        // TODO: 구현. directoryId 를 통해 현재 경로 찾아서 path 에 넣기
        System.out.println(directoryId);
        String path = "";
        model.addAttribute("path", path);
        model.addAttribute("directoryId", directoryId);
        return "directory";
    }

}
