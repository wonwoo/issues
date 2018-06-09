package ml.wonwoo.githubissues.github;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GithubClient {

  private static final String GITHUB_ROOT = "https://api.github.com";

  private final RestTemplate restTemplate;

  public GithubClient(RestTemplateBuilder builder) {
    this.restTemplate = builder
        .rootUri(GITHUB_ROOT)
        .build();
  }
  public List<RepositoryEvent> fetchEvents(String orgName, String repoName) {
    return this.restTemplate.exchange("/repos/{owner}/{repo}/issues/events", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<RepositoryEvent>>() {},
        orgName, repoName).getBody();
  }

  @Cacheable("events")
  public List<RepositoryEvent> cacheFetchEvents(String orgName, String repoName) {
    return fetchEvents(orgName, repoName);
  }
}
