package machels.ohmybox.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "file")
public class File {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("current_directory_id")
    private String currentDirectoryId;

    @Field("name")
    private String name;

    @Field("type")
    private String type;

    @Field("location")
    private String location;

    @Field("size")
    private Long size;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Field("version")
    private Integer version;
}
