package com.themistech.dasntscam.issues;

import com.themistech.dasntscam.dto.IssueDTO;
import com.themistech.dasntscam.entities.Issue;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.requests.IssueRequest;
import com.themistech.dasntscam.responses.IssuesListResponse;

import java.util.List;

public interface IssuesService {

    List<Issue> getAllIssuesListWithToken(String token);

    void createIssue(IssueRequest issueRequest, User user);

}
