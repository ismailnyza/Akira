package parrotsl.akira.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parrotsl.akira.customExceptions.NoTaskTagFoundException;
import parrotsl.akira.entity.TaskTag;
import parrotsl.akira.repository.TaskTagRepository;

@Service
@Transactional
public class TaskTagService {

  private final TaskTagRepository taskTagRepository;

  public TaskTagService(TaskTagRepository taskTagRepository) {
    this.taskTagRepository = taskTagRepository;
  }

  public TaskTag findTaskTagName(String taskTagName) {
    return taskTagRepository.findByTagName(taskTagName)
        .orElseThrow(() -> new NoTaskTagFoundException("No Task Tag Found"));
  }

  public TaskTag createTaskTag(String tag) {
    TaskTag taskTag = new TaskTag();
    taskTag.setTagName(tag);
    return taskTagRepository.save(taskTag);
  }
}
