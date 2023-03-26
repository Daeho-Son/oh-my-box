package machels.ohmybox.service;

import lombok.RequiredArgsConstructor;
import machels.ohmybox.domain.User;
import machels.ohmybox.dto.RequestPostCreateUserDto;
import machels.ohmybox.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> createUser(Mono<RequestPostCreateUserDto> monoUserDto) {
        // TODO: [예외 처리] 이미 존재하는 유저
        // TODO: [고민] password 암호화
        return monoUserDto
                .doOnNext(fp -> System.out.println("회원 가입 진행 중.."))
                .flatMap(fp -> {
                    User user = fp.toEntity();
                    userRepository.save(user).subscribe();
                    return Mono.empty();
                })
                .then(Mono.fromRunnable(() -> System.out.println("회원가입에 성공했습니다.")));
    }

    @Override
    public Mono<User> findUser(String userId) {
        // TODO: [예외 처리] 존재하지 않은 유저
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.fromRunnable(() -> System.out.println("존재하지 않는 유저입니다.")));
    }
}
