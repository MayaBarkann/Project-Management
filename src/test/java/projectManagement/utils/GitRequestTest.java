package projectManagement.utils;

import org.junit.jupiter.api.Test;
import projectManagement.controller.entities.GitToken;

import static org.junit.jupiter.api.Assertions.*;

class GitRequestTest {

    @Test
    void validateReqGitGetToken_badInputLink_returnNull() {
        assertNull(GitRequest.reqGitGetToken("goodUser"));
    }
    @Test
    void validateGitGetToken_oldLink_access_tokenNull() {
        GitToken gitToken = GitRequest.reqGitGetToken("https://github.com/login/oauth/access_token?client_id=0d6e3c4bb0696a099b5b&client_secret=773813a9d336de56a72cc4e3c30daf027ac0f9ed&code=8d6a4f528a22e70dfe63");
        assertNotNull(gitToken );
        assertNull(gitToken.access_token);
    }
    @Test
    void validateGitGetToken_oldLink_token_typeNull() {
        GitToken gitToken = GitRequest.reqGitGetToken("https://github.com/login/oauth/access_token?client_id=0d6e3c4bb0696a099b5b&client_secret=773813a9d336de56a72cc4e3c30daf027ac0f9ed&code=8d6a4f528a22e70dfe63");
        assertNotNull(gitToken );
        assertNull(gitToken.token_type);
    }
    @Test
    void validateGitGetToken_oldLink_scopeNull() {
        GitToken gitToken = GitRequest.reqGitGetToken("https://github.com/login/oauth/access_token?client_id=0d6e3c4bb0696a099b5b&client_secret=773813a9d336de56a72cc4e3c30daf027ac0f9ed&code=8d6a4f528a22e70dfe63");
        assertNotNull(gitToken );
        assertNull(gitToken.scope);
    }
    @Test
    void validateReqGitGetUser_OldBearerToken_returnNull() {
        assertNull(GitRequest.reqGitGetUser("https://api.github.com/user","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"));
    }
    @Test
    void validateReqGitGetUser_BadLinkInput_returnNull() {
        assertNull(GitRequest.reqGitGetUser("https://com/user3","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"));
    }
    @Test
    void validateReqGitGetUser_BadInput_returnNull() {
        assertNull(GitRequest.reqGitGetUser("",""));
    }

}