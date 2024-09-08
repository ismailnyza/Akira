package parrotsl.akira.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import parrotsl.akira.utils.PriorityDeserializer;

@JsonDeserialize(using = PriorityDeserializer.class)
@Schema(description = "The Priority level of the task.")
public enum Priority {
    @JsonProperty("High") HIGH,
    @JsonProperty("Medium") MEDIUM,
    @JsonProperty("LOW") LOW
}
