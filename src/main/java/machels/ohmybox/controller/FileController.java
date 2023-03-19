package machels.ohmybox.controller;

import lombok.AllArgsConstructor;
import machels.ohmybox.domain.File;
import machels.ohmybox.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    // TODO: user 생성 후, 해당 ./ohmybox 에 userId 디렉토리 추가하기. ./ohmybox/{userId} 가 root directory.
    @PostMapping("/directorys/{directoryId}/files")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Void> UploadFile(@PathVariable("directoryId") String directoryId,
                                 @RequestPart("fileToUpload") Mono<FilePart> filePartMono,
                                 ServerHttpResponse response,
                                 @RequestHeader("Referer") URI referer) {
        // TODO: uploadFile() 의 매개변수를 한 개로 할 수 있는 방법 찾아보기
        System.out.println(directoryId);
        return fileService.uploadFile(filePartMono, referer.getPath(), directoryId, response);
    }

    @GetMapping("/files/{fileId}/info")
    public Mono<File> GetFileInfo(@PathVariable("fileId") String fileId) {
        return fileService.getFileInfo(fileId);
    }

    @GetMapping("/files/{fileId}")
    public Mono<Void> DownloadFile(@PathVariable("fileId") String fileId,
                                   ServerHttpResponse response) {
        return fileService.downloadFile(fileId, response);
    }

    @DeleteMapping("/files/{fileId}")
    public Mono<Void> DeleteFile(@PathVariable("fileId") String fileId) {
        return fileService.deleteFile(fileId);
    }
}

