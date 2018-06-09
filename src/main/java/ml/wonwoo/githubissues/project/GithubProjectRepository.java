package ml.wonwoo.githubissues.project;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GithubProjectRepository extends MongoRepository<GithubProject, String> {
}
