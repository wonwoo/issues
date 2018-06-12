package ml.wonwoo.githubissues.admin;

import ml.wonwoo.githubissues.project.GithubProject;
import ml.wonwoo.githubissues.project.GithubProjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GithubProjectRepository repository;

  @Test
  public void adminFormTest() throws Exception {
    GithubProject githubProject = new GithubProject("wonwoo", "dynamodb-spring-boot");
    githubProject.setId(1L);
    GithubProject githubProject1 = new GithubProject("spring", "spring-boot");
    githubProject1.setId(2L);
    given(repository.findAll())
        .willReturn(Arrays.asList(githubProject, githubProject1));

    this.mockMvc.perform(get("/admin"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("wonwoo")))
        .andExpect(content().string(containsString("dynamodb-spring-boot")))
        .andExpect(content().string(containsString("spring")))
        .andExpect(content().string(containsString("spring-boot")));
  }

  @Test
  public void createGithubProjectTest() throws Exception {
    given(repository.save(any(GithubProject.class)))
        .willReturn(new GithubProject("wonwoo", "dynamodb-spring-boot"));

    this.mockMvc.perform(post("/admin"))
        .andExpect(status().isFound());
    verify(this.repository).save(any(GithubProject.class));
  }


  @Test
  public void deleteGithubProjectTest() throws Exception {

    doNothing().when(repository).deleteById(any());
    this.mockMvc.perform(delete("/admin/{id}", "test")
        .param("repoName","dynamodb-spring-boot"))
        .andExpect(status().isFound());
    verify(this.repository).deleteById(any());
  }
}