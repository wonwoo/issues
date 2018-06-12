package ml.wonwoo.githubissues;

import ml.wonwoo.githubissues.project.GithubProject;
import ml.wonwoo.githubissues.project.GithubProjectRepository;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitiailizer implements SmartInitializingSingleton {

  private final GithubProjectRepository githubProjectRepository;

  public DataInitiailizer(GithubProjectRepository githubProjectRepository) {
    this.githubProjectRepository = githubProjectRepository;
  }

  @Override
  public void afterSingletonsInstantiated() {
    this.githubProjectRepository.deleteAll();
    this.githubProjectRepository.saveAll(List.of(
        new GithubProject("wonwoo", "dynamodb-spring-boot"),
        new GithubProject("mybatis", "spring-boot-starter"),
        new GithubProject("spring-projects", "spring-boot")
    ));
  }
}
