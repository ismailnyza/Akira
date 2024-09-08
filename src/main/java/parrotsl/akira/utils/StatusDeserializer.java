package parrotsl.akira.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import parrotsl.akira.entity.enums.Status;

public class StatusDeserializer extends StdDeserializer<Status> {
  public StatusDeserializer() {
    super(Status.class);
  }

  @Override
  public Status deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    return null;
  }

}
