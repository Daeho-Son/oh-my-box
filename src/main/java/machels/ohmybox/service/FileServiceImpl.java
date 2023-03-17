package machels.ohmybox.service;

import lombok.RequiredArgsConstructor;
import machels.ohmybox.domain.File;
import machels.ohmybox.repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    // TODO: 나중에 root directory 를 ./ohmybox/{userId} 로 변경.
    private final Path rootPath = Paths.get("./ohmybox/1");

    // TODO: file 업로드 시 맨 Location 맨 앞에 '.' 맨 뒤에 '/' 추가
    // TODO: 같은 이름 업로드 막기
    @Override
    public Mono<Void> uploadFile(Mono<FilePart> filePartMono, String path, String folderId, ServerHttpResponse response) {
        return filePartMono
                .doOnNext(fp -> System.out.println("[" + fp.filename() + "] 의 업로드를 시작합니다."))
                .flatMap(fp -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation((URI.create(path)));
                    Path pathWithFileName = rootPath.resolve(fp.filename());
                    java.io.File file = new java.io.File("." + path, fp.filename());
                    if (file.exists()) {
                        // TODO: 이미 존재하는 유저. Controller 에서 어떻게 Error handling 하는지 찾아보기
//                        return Mono.error(new FileAlreadyExistsException("파일이 이미 존재합니다."));
                        return Mono.empty();
                    }
                    return fp.transferTo(pathWithFileName)
                            .then(Mono.fromRunnable(() -> {
                                File fileData = File.builder()
                                        .userId("1") // TODO: 유저 추가 후 변경. (+ index.html, directory.html)
                                        .folderId(folderId)
                                        .name(fp.filename())
                                        .type(FilenameUtils.getExtension(fp.filename()) + " 파일")
                                        .location("." + path + "/")
                                        .size(pathWithFileName.toFile().length())
                                        .build();
                                fileRepository.save(fileData).subscribe();
                            })).log();
                })
                .doOnSuccess(fp -> System.out.println("업로드를 성공했습니다."))
                .then().log();
    }

    @Override
    public Mono<File> getFileInfo(String fileId) {
        return fileRepository.findById(fileId);
    }

    @Override
    public Mono<Void> downloadFile(String fileId, ServerHttpResponse response) {
        return fileRepository.findById(fileId)
                .doOnNext(fp -> System.out.println("[" + fp.getName() + "] 의 다운로드를 시작합니다."))
                .flatMap(fp -> {
                    ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
                    java.io.File file = new java.io.File(fp.getLocation() + fp.getName());
                    response.getHeaders().setContentDisposition(ContentDisposition.attachment().filename(fp.getName()).build());
                    response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    return zeroCopyResponse.writeWith(file, 0, file.length());
                })
                .doOnSuccess(fp -> System.out.println("다운로드를 성공했습니다."))
                .then().log();
    }

    // TODO: bug. 파일이 없어도 "파일을 삭제했습니다." 라는 메시지 출력됨.
    @Override
    public Mono<Void> deleteFile(String fileId) {
        return fileRepository.findById(fileId)
                // TODO: 존재하지 않는 파일. Controller 에서 어떻게 Error handling 하는지 찾아보기
//                .switchIfEmpty(Mono.error(new FileNotFoundException("존재하지 않는 파일입니다.")))
                .switchIfEmpty(Mono.empty())
                .doOnNext(fp -> System.out.println("[" + fp.getName() + "] 의 삭제를 시작합니다."))
                .flatMap(fp -> {
                    Path pathWithFileName = Paths.get(fp.getLocation() + fp.getName());
                    java.io.File file = new java.io.File(fp.getLocation() + fp.getName());
                    if (file.exists()) {
                        System.out.println("OK");
                    } else {
                        System.out.println("KO");
                    }
                    try {
                        Files.deleteIfExists(pathWithFileName);
                    } catch (IOException e) {
                        // TODO: 예외처리
                        System.out.println("파일 삭제에 실패했습니다: " + e);
                    }
                    fileRepository.delete(fp).subscribe();
                    return Mono.empty();
                })
                .then().log();
    }
}
