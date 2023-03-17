package machels.ohmybox.repository;

import machels.ohmybox.domain.File;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FileRepository extends ReactiveMongoRepository<File, String> {
}
