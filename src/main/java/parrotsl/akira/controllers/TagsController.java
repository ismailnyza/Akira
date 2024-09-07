package parrotsl.akira.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import parrotsl.akira.entity.TaskTag;
import parrotsl.akira.service.TaskTagService;

@Controller
@RequestMapping("api/Tags/")
public class TagsController {

  private final TaskTagService taskTagService;


  public TagsController(TaskTagService taskTagService) {
    this.taskTagService = taskTagService;
  }

  @PostMapping("/createTag")
  public TaskTag createTaskTag(@RequestBody TaskTag taskTag) {
    return taskTagService.createTaskTag(taskTag.getTagName());
  }
}
