package parrotsl.akira.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import parrotsl.akira.entity.enums.Status;
import parrotsl.akira.entity.enums.Priority;
import parrotsl.akira.entity.enums.TaskVisibility;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Long id;
    @NotNull
    public String title;
    public Enum<Status> status;
    public Enum<Priority> priority;
    public LocalDateTime taskCreatedAt;
    public LocalDateTime lastUpdatedAt;
    @OneToOne
    @JoinColumn(name = "created_by_id")
    public User createdBy;
    public Enum<TaskVisibility> taskVisibility;
    @ManyToOne
    public User assignees;
    @ElementCollection
    public ArrayList<String> tags;
    @ManyToOne
    public Comment comments;
    @ManyToOne
    public Task subtasks;
    public String description;


}
