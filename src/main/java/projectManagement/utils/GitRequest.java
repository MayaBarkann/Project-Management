package projectManagement.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectManagement.controller.entities.GitToken;
import projectManagement.controller.entities.GitUser;
import projectManagement.controller.entities.GithubEmail;

import java.util.List;

public class GitRequest {


    // refactoring to generic function ???
    public static ResponseEntity<GitToken> reqGitGetToken(String link) {
        ResponseEntity<GitToken> response = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        try {
            return restTemplate.exchange(link, HttpMethod.POST, entity, GitToken.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static ResponseEntity<GitUser> reqGitGetUser(String link, String bearerToken) {

        ResponseEntity<GitUser> response = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            GitUser gitUser = restTemplate.exchange(link, HttpMethod.GET, entity, GitUser.class).getBody();
            gitUser.accessToken=bearerToken;
            if(gitUser.getEmail()==null){
                GithubEmail[] githubEmail = restTemplate.exchange(link+"/emails", HttpMethod.GET, entity, GithubEmail[].class).getBody();
                for (GithubEmail gEmail: githubEmail) {
                    if(gEmail.isPrimary()){
                        gitUser.email = gEmail.getEmail();
                        break;
                    }
                }
            }
            return ResponseEntity.ok(gitUser);
        } catch (Exception e) {
            return null;
        }
    }
}
