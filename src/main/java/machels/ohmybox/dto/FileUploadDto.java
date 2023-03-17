package machels.ohmybox.dto;

import lombok.Data;
import machels.ohmybox.domain.File;

@Data
public class FileUploadDto {
    private String name;
    private String type;
    private String location;

    public File toEntity() {
        return File.builder()
                .name(name)
                .type(type)
                .location(location)
                .build();
    }
}
