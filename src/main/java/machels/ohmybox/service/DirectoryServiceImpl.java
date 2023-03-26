package machels.ohmybox.service;

import lombok.RequiredArgsConstructor;
import machels.ohmybox.domain.Directory;
import machels.ohmybox.domain.User;
import machels.ohmybox.dto.RequestPostCreateDirectoryDto;
import machels.ohmybox.repository.DirectoryRepository;
import machels.ohmybox.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final UserRepository userRepository;
    private final String storagePath = "/";

    @Override
    public Mono<Directory> createRootDirectory(Mono<RequestPostCreateDirectoryDto> monoDirectoryDto) {
        return monoDirectoryDto
                .map(RequestPostCreateDirectoryDto::toEntity)
                .flatMap(fp -> {
                    fp.setParentDirectoryLocation(storagePath);
                    fp.setCurrentDirectoryLocation(storagePath + fp.getName() + "/");
                    return directoryRepository.save(fp)
                            .zipWith(userRepository.findById(fp.getUserId()));
                })
                .flatMap(tuple -> {
                    Directory directory = tuple.getT1();
                    User user = tuple.getT2();
                    user.setRootDirectoryId(directory.getId());
                    return userRepository.save(user)
                            .thenReturn(directory);
                });
    }

    @Override
    public Mono<Directory> createSubDirectory(String parentDirectoryId, Mono<RequestPostCreateDirectoryDto> monoDirectoryDto) {
        return monoDirectoryDto
                .map(RequestPostCreateDirectoryDto::toEntity)
                .zipWith(directoryRepository.findById(parentDirectoryId))
                .flatMap(tuple -> {
                    Directory parentDirectory = tuple.getT2();
                    Directory directory = tuple.getT1();
                    directory.setParentDirectoryId(parentDirectory.getId());
                    directory.setParentDirectoryLocation(parentDirectory.getCurrentDirectoryLocation());
                    directory.setCurrentDirectoryLocation(parentDirectory.getCurrentDirectoryLocation() + directory.getName() + "/");
                    return directoryRepository.save(directory);
                });
    }

    @Override
    public Mono<Directory> getDirectoryInfo(String directoryId) {
        return directoryRepository.findById(directoryId);
    }

    @Override
    public Flux<Directory> getDirectorySubList(String directoryId) {
        return directoryRepository.findByParentDirectoryId(directoryId);
    }

    // TODO: 구현. 디렉토리 다운로드 구현
    @Override
    public Mono<Void> downloadDirectory(String directoryId) {
        return null;
    }

    @Override
    public Mono<Void> deleteDirectory(String directoryId) {
        return directoryRepository.deleteById(directoryId);
    }
}
