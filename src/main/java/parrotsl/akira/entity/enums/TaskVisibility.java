package parrotsl.akira.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The Visibility of the task.")
public enum TaskVisibility {
    Private,
    Group,
    Public
}
