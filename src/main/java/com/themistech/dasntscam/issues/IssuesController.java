package com.themistech.dasntscam.issues;

import com.themistech.dasntscam.auth.AuthService;
import com.themistech.dasntscam.dto.IssueDTO;
import com.themistech.dasntscam.entities.Issue;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.requests.IssueRequest;
import com.themistech.dasntscam.responses.IssuesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/issues")

public class IssuesController {

    @Autowired
    private IssuesService issuesService;
    @Autowired
    private AuthService authService;

   @PostMapping(value = "create")
    public ResponseEntity<String> createIssue(@RequestBody IssueRequest issueRequest,
                                              @RequestHeader("Authorization") String token )
    {
        try {
            // Verificar si el token es válido
            User user = authService.getUserWithToken(token);
            if (user == null) {
                return ResponseEntity.status(401).body("Token inválido o expirado");
            } else {
                issuesService.createIssue(issueRequest, user);
                return ResponseEntity.status(201).body("Caso creado exitosamente");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el caso: " + e.getMessage());
        }
    }

    //Recuperar la lista de incidencias segun el token
    @GetMapping(value = "getAllIssuesList")
    public ResponseEntity<IssuesListResponse> getAllIssues(@RequestHeader("Authorization") String token) {
        List<Issue> issues = issuesService.getAllIssuesListWithToken(token);
        List<IssueDTO> issueDTOs = IssueDTO.toIssueDTOList(issues);
        return ResponseEntity.ok(new IssuesListResponse(issueDTOs));
    }



}
