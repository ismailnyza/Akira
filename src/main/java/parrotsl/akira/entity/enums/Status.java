package parrotsl.akira.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import parrotsl.akira.utils.StatusDeserializer;

@JsonDeserialize(using = StatusDeserializer.class)
@Schema(description = "The Status of the task.")
public enum Status {
  @JsonProperty("Todo") TODO,
  @JsonProperty("Pending") PENDING,
  @JsonProperty("InProgress") INPROGRESS,
  @JsonProperty("Completed") COMPLETED,
  @JsonProperty("INREVIEW") INREVIEW
}
