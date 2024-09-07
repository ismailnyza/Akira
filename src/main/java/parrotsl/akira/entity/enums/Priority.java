package parrotsl.akira.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The Priority level of the task.")
public enum Priority {
    High,
    Medium,
    Low,
}
