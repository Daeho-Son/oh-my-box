package machels.ohmybox.dto;

import lombok.Data;
import machels.ohmybox.domain.Directory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

@Data
public class RequestPostCreateDirectoryDto {

    // TODO: jwt 사용하면 그때 수정
    private String userId;

    private String name;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public Directory toEntity() {
        return Directory.builder()
                .userId(userId)
                .name(name)
                .type("Directory")
                .build();
    }
}
