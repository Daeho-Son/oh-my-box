package machels.ohmybox.controller;

import lombok.AllArgsConstructor;
import machels.ohmybox.domain.User;
import machels.ohmybox.dto.RequestPostCreateUserDto;
import machels.ohmybox.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    // TODO: thymeleaf 를 통해 회원등록이 가능하게 하기
    @PostMapping("/users")
    public Mono<Void> CreateUser(@RequestBody Mono<RequestPostCreateUserDto> userPostDtoMono) {
        return userService.createUser(userPostDtoMono);
    }

    // TODO: Response 를 DTO 로 변경
    @GetMapping("/users/{userId}")
    public Mono<User> FindUser(@PathVariable("userId") String userId) {
        return userService.findUser(userId);
    }
}
