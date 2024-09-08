package parrotsl.akira.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import parrotsl.akira.entity.enums.TaskVisibility;

public class TaskVisibilityDeserializer extends StdDeserializer<TaskVisibility> {
  public TaskVisibilityDeserializer() {
    super(TaskVisibility.class);
  }

  @Override
  public TaskVisibility deserialize(final JsonParser  jsonParser, final DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    return null;
  }

}
