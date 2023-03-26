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

    @PostMapping("/users/{userId}/directories/{directoryId}/files")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<File> UploadFile(@PathVariable("userId") String userId,
                                 @PathVariable("directoryId") String directoryId,
                                 @RequestPart("fileToUpload") Mono<FilePart> monoFilePart) {
        return fileService.uploadFile(monoFilePart, userId, directoryId);
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

