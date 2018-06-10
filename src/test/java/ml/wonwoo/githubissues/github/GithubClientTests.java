package ml.wonwoo.githubissues.github;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static ml.wonwoo.githubissues.github.RepositoryEvent.Type.HEAD_REF_DELETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(GithubClient.class)
public class GithubClientTests {

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private GithubClient githubClient;

  @Test
  public void cacheFetchEventsTest() {
    this.server.expect(
        requestTo("/repos/spring/spring-boot/issues/events"))
        .andRespond(withSuccess(
            new ClassPathResource("issues.json", getClass()),
            MediaType.APPLICATION_JSON));

    List<RepositoryEvent> repositoryEvents = this.githubClient.cacheFetchEvents("spring", "spring-boot");
    assertThat(repositoryEvents).hasSize(2);
    assertThat(repositoryEvents.get(0).getType()).isEqualTo(HEAD_REF_DELETED);
    assertThat(repositoryEvents.get(0).getIssue().getHtmlUrl()).isEqualTo("https://github.com/wonwoo/dynamodb-spring-boot/pull/22");
    assertThat(repositoryEvents.get(0).getIssue().getTitle()).isEqualTo("#21 readme update");
    assertThat(repositoryEvents.get(0).getActor().getLogin()).isEqualTo("wonwoo");
    assertThat(repositoryEvents.get(0).getActor().getHtmlUrl()).isEqualTo("https://github.com/wonwoo");
  }

}