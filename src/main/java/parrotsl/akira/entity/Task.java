package parrotsl.akira.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.TaskVisibility;

import java.time.LocalDateTime;
import java.util.HashSet;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity representing a task in the system.")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Schema(description = "Unique identifier for the task.", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "Title of the task.", example = "Complete Documentation")
    private String title;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Current status of the task.")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Priority level of the task.")
    private Priority priority;

    @Schema(description = "Timestamp when the task was created.", example = "2023-09-01T12:34:56")
    private LocalDateTime taskCreatedAt;

    @Schema(description = "Timestamp when the task was last updated.", example = "2023-09-02T10:15:30")
    private LocalDateTime lastUpdatedAt;

    @Schema(description = "ID of the user who created the task.", example = "123")
    private Long creatorUserId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Visibility of the task.")
    private TaskVisibility taskVisibility;

    @ElementCollection
    @Schema(description = "IDs of the users assigned to the task.")
    private Set<Long> assigneeUserIds;

    @ElementCollection
    @Schema(description = "Tags associated with the task.", example = "[\"urgent\", \"backend\"]")
    private Set<TaskTag> tags;

    @ManyToOne
    @Schema(description = "Comments associated with the task.")
    private Comment comments;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Subtasks related to this task.")
    private ArrayList<Task> subtasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    @Schema(description = "The parent task to which this subtask belongs.")
    private Task parentTask;

    @Schema(description = "Detailed description of the task.", example = "This task involves writing documentation.")
    private String description;

}
