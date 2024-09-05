package parrotsl.akira.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;
    public Enum<Status> status;
    public Enum<Priority> priority;
    public LocalDateTime taskCreatedAt;
    public LocalDateTime lastUpdatedAt;
    public User createdBy;
//    visible based on user roles
    public Enum<TaskVisibility> taskVisibility;
    public User assignees;
//    maintain this in the db too
    public ArrayList<String> tags;

    public ArrayList<Comments> comments;
    public ArrayList<Task> subtasks;
    public String description;
}
