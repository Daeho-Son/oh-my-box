package machels.ohmybox.service;

import lombok.RequiredArgsConstructor;
import machels.ohmybox.domain.Directory;
import machels.ohmybox.domain.File;
import machels.ohmybox.repository.DirectoryRepository;
import machels.ohmybox.repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final DirectoryRepository directoryRepository;
    private final String storagePath = "./ohmybox/";

    // TODO: 고민. root Directory 는 NULL or NOT NULL? 유저 등록에서 추가? 아니면 NULL 일때 root 라고 할까?
    @Override
    public Mono<File> uploadFile(Mono<FilePart> monoFilePart, String userId, String directoryId) {
        return monoFilePart
                .zipWith(directoryRepository.findById(directoryId))
                .flatMap(tuple -> {
                    FilePart filePart = tuple.getT1();
                    Directory directory = tuple.getT2();
                    File fileData = File.builder()
                            .userId(userId)
                            .currentDirectoryId(directoryId)
                            .name(filePart.filename())
                            .type(FilenameUtils.getExtension(filePart.filename()))
                            .location(directory.getCurrentDirectoryLocation())
                            .size(filePart.headers().getContentLength())
                            .build();
                    return fileRepository.save(fileData)
                            .zipWith(monoFilePart);
                })
                .flatMap(tuple -> {
                    File file = tuple.getT1();
                    FilePart filePart = tuple.getT2();
                    Path pathWithFileId = Paths.get(storagePath + file.getId());
                    return filePart.transferTo(pathWithFileId)
                            .doOnSuccess(fp -> file.setSize(pathWithFileId.toFile().length()))
                            .thenReturn(file);
                })
                .flatMap(fileRepository::save);
    }

    @Override
    public Mono<File> getFileInfo(String fileId) {
        return fileRepository.findById(fileId);
    }

    @Override
    public Mono<Void> downloadFile(String fileId, ServerHttpResponse response) {
        return fileRepository.findById(fileId)
                .flatMap(fp -> {
                    Path path = Paths.get(storagePath + fileId);
                    System.out.println(fp.getName());
                    try {
                        String encodingFileName = URLEncoder.encode(fp.getName(), "UTF-8").replace("+", "%20");
                        response.getHeaders().setContentDisposition(ContentDisposition.attachment().filename(encodingFileName).build());
//                        response.getHeaders().set("Content-Disposition", "attachment; filename=\"" + encodingFileName  + "");
                        response.getHeaders().set("Content-Transfer-Encoding", "binary");
                        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
                        return response.writeWith(DataBufferUtils.readInputStream(
                                () -> Files.newInputStream(path),
                                response.bufferFactory(),
                                1024 * 1024
                        ));
                    } catch (UnsupportedEncodingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    @Override
    public Mono<Void> deleteFile(String fileId) {
        return fileRepository.findById(fileId)
                .switchIfEmpty(Mono.empty()) // TODO: 파일이 없을 때 어떻게 처리?
                .flatMap(fp -> {
                    Path pathWithFileName = Paths.get(storagePath + fp.getId());
                    try {
                        Files.deleteIfExists(pathWithFileName);
                    } catch (IOException e) {
                        // TODO: 예외처리
                        System.out.println("파일 삭제에 실패했습니다: " + e);
                    }
                    return fileRepository.delete(fp);
                });
    }
}
