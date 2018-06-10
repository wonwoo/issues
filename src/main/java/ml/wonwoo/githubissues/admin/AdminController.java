package ml.wonwoo.githubissues.admin;

import ml.wonwoo.githubissues.project.GithubProject;
import ml.wonwoo.githubissues.project.GithubProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

  private final GithubProjectRepository repository;

  public AdminController(GithubProjectRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/admin")
  public String adminForm(Model model) {
    model.addAttribute("projects", this.repository.findAll());
    return "admin";
  }

  @PostMapping("/admin")
  public String createGithubProject(@ModelAttribute GithubProject githubProject) {
    this.repository.save(githubProject);
    return "redirect:/";
  }

  @DeleteMapping("/admin/{id}")
  public String deleteGithubProject(@PathVariable String id) {
    this.repository.deleteById(id);
    return "redirect:/";
  }
}
