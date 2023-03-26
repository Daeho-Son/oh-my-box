package machels.ohmybox.controller;

import lombok.AllArgsConstructor;
import machels.ohmybox.domain.Directory;
import machels.ohmybox.dto.RequestPostCreateDirectoryDto;
import machels.ohmybox.service.DirectoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class DirectoryController {

    private final DirectoryService directoryService;

    // TODO: 구현. Response DTO 만들기
    @PostMapping("/directories")
    public Mono<Directory> CreateRootDirectory(@RequestBody Mono<RequestPostCreateDirectoryDto> monoDirectoryDto) {
        return directoryService.createRootDirectory(monoDirectoryDto);
    }

    @PostMapping("/directories/{directoryId}")
    public Mono<Directory> CreateSubDirectory(@PathVariable("directoryId") String parentDirectoryId,
                                         @RequestBody Mono<RequestPostCreateDirectoryDto> monoDirectoryDto) {
        return directoryService.createSubDirectory(parentDirectoryId, monoDirectoryDto);
    }

    @GetMapping("/directories/{directoryId}/info")
    public Mono<ResponseEntity<Directory>> GetDirectoryInfo(@PathVariable("directoryId") String directoryId) {
        return directoryService.getDirectoryInfo(directoryId)
                .map(directory -> ResponseEntity.ok(directory))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/directories/{directoryId}/sub-list")
    public Flux<Directory> GetDirectorySubList(@PathVariable("directoryId") String directoryId) {
        return directoryService.getDirectorySubList(directoryId);
    }

    @GetMapping("/directories/{directoryId}")
    public Mono<Void> DownloadDirectory(@PathVariable("directoryId") String directoryId) {
        return directoryService.downloadDirectory(directoryId);
    }

    @DeleteMapping("/directories/{directoryId}")
    public Mono<Void> DeleteDirectory(@PathVariable("directoryId") String directoryId) {
        return directoryService.deleteDirectory(directoryId);
    }
}
