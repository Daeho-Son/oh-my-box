package machels.ohmybox.service;

import machels.ohmybox.domain.Directory;
import machels.ohmybox.dto.RequestPostCreateDirectoryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectoryService {

    Mono<Directory> createRootDirectory(Mono<RequestPostCreateDirectoryDto> monoDirectoryDto);

    Mono<Directory> createSubDirectory(String parentDirectoryId, Mono<RequestPostCreateDirectoryDto> monoDirectoryDto);

    Mono<Directory> getDirectoryInfo(String directoryId);

    Flux<Directory> getDirectorySubList(String directoryId);

    Mono<Void> downloadDirectory(String directoryId);

    Mono<Void> deleteDirectory(String directoryId);
}
