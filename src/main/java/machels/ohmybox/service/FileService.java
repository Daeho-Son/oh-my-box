package machels.ohmybox.service;

import machels.ohmybox.domain.File;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public interface FileService {

    Mono<File> uploadFile(Mono<FilePart> monoFilePart, String userId, String directoryId);

    Mono<File> getFileInfo(String fileId);

    Mono<Void> downloadFile(String fileId, ServerHttpResponse response);

    Mono<Void> deleteFile(String fileId);
}
