package machels.ohmybox.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "user")
public class User {

    @Id
    private String id;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("name")
    private String name;

    @Field("cloud_usage")
    private Long cloudUsage;

    @Field("cloud_size")
    private Long cloudSize;

    @Field("root_directory_id")
    private String rootDirectoryId;
}
