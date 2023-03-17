package machels.ohmybox.service;

import machels.ohmybox.domain.File;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileService {

    Mono<Void> uploadFile(Mono<FilePart> filePartMono, String path);

    Mono<File> getFileInfo(Long fileId);

    void downloadFile(Long fileId);

    void deleteFile(Long fileId);
}
