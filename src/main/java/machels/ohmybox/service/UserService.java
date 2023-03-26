package machels.ohmybox.service;

import machels.ohmybox.domain.User;
import machels.ohmybox.dto.RequestPostCreateUserDto;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Void> createUser(Mono<RequestPostCreateUserDto> usrPostDtoMono);

    Mono<User> findUser(String userId);
}
