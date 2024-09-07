package parrotsl.akira.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The Status of the task.")
public enum Status {
  Todo,
  Pending,
  InProgress,
  InReview,
  Completed
}
