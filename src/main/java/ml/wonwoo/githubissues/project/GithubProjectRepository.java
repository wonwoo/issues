package ml.wonwoo.githubissues.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubProjectRepository extends JpaRepository<GithubProject, Long> {
}
