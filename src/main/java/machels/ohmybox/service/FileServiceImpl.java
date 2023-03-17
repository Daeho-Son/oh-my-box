package machels.ohmybox.service;

import lombok.RequiredArgsConstructor;
import machels.ohmybox.domain.File;
import machels.ohmybox.repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    // TODO: 나중에 folderId 로 변경.
    // TODO: upload -> mybox 로 변경하기
    private final Path rootPath = Paths.get("./ohmybox");

    @Override
    public Mono<Void> uploadFile(Mono<FilePart> filePartMono, String path) {
        return filePartMono
                .doOnNext(fp -> System.out.println("[" + fp.filename() + "] 의 업로드를 시작합니다."))
                .flatMap(fp -> {
                    Path filePath = rootPath.resolve(fp.filename());
                    System.out.println(fp.filename()); // TODO: 삭제.
                    System.out.println(filePath); // TODO: 삭제.
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
    public Mono<File> getFileInfo(String fileId) {
        return fileRepository.findById(fileId);
    }

    @Override
    public Mono<Void> downloadFile(String fileId) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteFile(String fileId) {
        return fileRepository.findById(fileId)
                .flatMap(fp -> {
                    String stringPath = "." + fp.getLocation() + "/" + fp.getName();
                    Path filePath = Paths.get(stringPath);
                    try {
                        Files.deleteIfExists(filePath);
                    } catch (IOException e) {
                        // TODO: 예외처리 어떻게 해야할지?
                        System.out.println("파일 삭제에 실패했습니다: " + e);
                    }
                    fileRepository.delete(fp).subscribe();
                    return Mono.empty();
                });
    }
}
