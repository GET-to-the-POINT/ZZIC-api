package point.zzicback.todo.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import point.zzicback.todo.domain.Todo;

@Data
@Schema(description = "To-Do 업데이트 요청")
public class UpdateTodoRequest {
    @Schema(description = "To-Do 항목의 제목", example = "장보기 수정")
    private String title;

    @Schema(description = "To-Do 항목의 상세 설명", example = "우유, 빵, 계란, 치즈 구입")
    private String description;

    @Schema(description = "To-Do 항목의 완료 여부", example = "false")
    private Boolean done;

    public Todo ToEntity() {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDone(done);
        return todo;
    }
}