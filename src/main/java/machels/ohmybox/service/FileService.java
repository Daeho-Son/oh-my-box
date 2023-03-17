package machels.ohmybox.service;

import machels.ohmybox.domain.File;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileService {

    Mono<Void> uploadFile(Mono<FilePart> filePartMono, String path, String folderId);

    Mono<File> getFileInfo(String fileId);

    Mono<Void> downloadFile(String fileId);

    Mono<Void> deleteFile(String fileId);
}
