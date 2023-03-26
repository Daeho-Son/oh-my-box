package machels.ohmybox.repository;

import machels.ohmybox.domain.Directory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DirectoryRepository extends ReactiveMongoRepository<Directory, String> {
    Flux<Directory> findByParentDirectoryId(String parentDirectoryId);
}
