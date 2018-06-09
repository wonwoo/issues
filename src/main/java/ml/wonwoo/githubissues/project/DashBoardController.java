package ml.wonwoo.githubissues.project;

import ml.wonwoo.githubissues.github.GithubClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableCaching
public class DashBoardController {

  private final GithubClient githubClient;
  private final GithubProjectRepository repository;

  public DashBoardController(GithubClient githubClient, GithubProjectRepository repository) {
    this.githubClient = githubClient;
    this.repository = repository;
  }

  @GetMapping("/")
  public String dashBoard(Model model) {
    List<DashboardEntry> dashboardEntries = this.repository.findAll()
        .stream()
        .map(githubProject -> new DashboardEntry(githubProject,
            githubClient.cacheFetchEvents(githubProject.getOrgName(), githubProject.getRepoName())))
        .collect(Collectors.toList());
    model.addAttribute("entries", dashboardEntries);
    return "index";
  }
}
