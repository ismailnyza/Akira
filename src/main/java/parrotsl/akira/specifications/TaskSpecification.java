package parrotsl.akira.specifications;

import org.springframework.data.jpa.domain.Specification;
import parrotsl.akira.entity.Task;
import parrotsl.akira.entity.User;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.Status;

public class TaskSpecification {

  public static Specification<Task> titleContains(String title) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get("title"), "%" + title + "%");
  }

  public static Specification<Task> descriptionContains(String description) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get("description"), "%" + description + "%");
  }

  public static Specification<Task> createdBy(User createdBy) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("createdBy"), createdBy);
  }

  public static Specification<Task> assignedTo(User assignees) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("assignees"), assignees);
  }

  public static Specification<Task> statusIs(Status status) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("status"), status);
  }

  public static Specification<Task> priorityIs(Priority priority) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("priority"), priority);
  }
}
