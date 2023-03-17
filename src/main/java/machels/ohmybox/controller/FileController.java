package machels.ohmybox.controller;

import lombok.AllArgsConstructor;
import machels.ohmybox.domain.File;
import machels.ohmybox.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload/single")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Void> UploadFile(@RequestPart("fileToUpload") Mono<FilePart> filePartMono,
                                 ServerHttpResponse response,
                                 @RequestHeader("Referer") URI referer) {
        return fileService.uploadFile(filePartMono, referer.getPath())
                .then(Mono.fromRunnable(() -> {
                    System.out.println("다운로드가 완료되었습니다.");
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation((URI.create("/")));
                }));
    }

    @GetMapping("/{id}")
    public Mono<File> GetFileInfo(@PathVariable("id") Long fileId) {
        return fileService.getFileInfo(fileId);
    }

    @GetMapping("/{id}/download")
    public void DownloadFile(@PathVariable("id") Long fileId) {
        fileService.downloadFile(fileId);
    }

    @DeleteMapping("/{id}")
    public void DeleteFile(@PathVariable("id") Long fileId) {
        fileService.deleteFile(fileId);
    }
}
