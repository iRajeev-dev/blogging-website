package blogging.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "PostDto Model Information")
public class PostDto {
    private Long id;
    @Schema(description = "blog post title")
    private String title;
    @Schema(description = "blog post description")
    private String description;
    private String content;
    private Long categoryId;
}
