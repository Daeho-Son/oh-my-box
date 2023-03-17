package machels.ohmybox.service;

import lombok.RequiredArgsConstructor;
import machels.ohmybox.domain.File;
import machels.ohmybox.repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    // TODO: 나중에 folderId 로 변경.
    // TODO: 어떻게 location 을 파악할지?
    private final Path basePath = Paths.get("./ohmybox");

    @Override
    public Mono<Void> uploadFile(Mono<FilePart> filePartMono, String path) {
        return filePartMono
                .doOnNext(fp -> System.out.println("[" + fp.filename() + "] 의 다운로드를 시작합니다."))
                .flatMap(fp -> {
                    Path filePath = basePath.resolve(fp.filename());
                    System.out.println(fp.filename()); // TEST
                    System.out.println(filePath); // TET
                    return fp.transferTo(filePath)
                            .then(Mono.fromRunnable(() -> {
                                File file = File.builder()
                                        .userId("") // TODO
                                        .folderId("") // TODO
                                        .name(fp.filename())
                                        .type(FilenameUtils.getExtension(fp.filename()) + " 파일")
                                        .location(path)
                                        .size(filePath.toFile().length())
                                        .build();
                                fileRepository.save(file).subscribe();
                            })).log();
                })
                .then().log();
    }


    @Override
    public Mono<File> getFileInfo(Long fileId) {
        return null;
    }

    @Override
    public void downloadFile(Long fileId) {

    }

    @Override
    public void deleteFile(Long fileId) {

    }
}
