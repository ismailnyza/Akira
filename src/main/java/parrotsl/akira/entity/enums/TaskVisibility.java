package parrotsl.akira.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import parrotsl.akira.utils.TaskVisibilityDeserializer;

@JsonDeserialize(using = TaskVisibilityDeserializer.class)
@Schema(description = "The Visibility of the task.")
public enum TaskVisibility {
    @JsonProperty("Private") PRIVATE,
    @JsonProperty("Group") GROUP,
    @JsonProperty("Public") PUBLIC
}
