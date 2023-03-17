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

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }


    @GetMapping("/ohmybox")
    public String getRootDirectory(Model model) {
        String path = "./ohmybox";
        return "directory";
    }

    @GetMapping("/ohmybox/{path:.+}")
    public String getDirectory(@PathVariable(value = "path", required = false) String path, Model model) {
        model.addAttribute("path", path);
        return "directory";
    }

}
