package ml.wonwoo.githubissues.project;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ml.wonwoo.githubissues.github.GithubClient;
import ml.wonwoo.githubissues.github.RepositoryEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DashBoardController.class)
public class DashBoardControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GithubClient githubClient;

  @MockBean
  private GithubProjectRepository repository;

  private JacksonTester<List<RepositoryEvent>> events;

  @Before
  public void setup() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    JacksonTester.initFields(this, objectMapper);
  }

  @Test
  public void dashBoardTest() throws Exception {
    given(repository.findAll())
        .willReturn(Collections.singletonList(
            new GithubProject("wonwoo", "dynamodb-spring-boot"))
        );

    List<RepositoryEvent> repositoryEvents = events.readObject("project.json");
    given(githubClient.cacheFetchEvents(any(), any()))
        .willReturn(repositoryEvents);

    this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("wonwoo")))
        .andExpect(content().string(containsString("#21 readme update")))
        .andExpect(content().string(containsString("dynamodb-spring-boot")))
        .andExpect(content().string(containsString("readme ")));

  }

}