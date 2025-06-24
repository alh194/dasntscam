package com.themistech.dasntscam.issues;

import com.themistech.dasntscam.auth.AuthService;
import com.themistech.dasntscam.entities.Issue;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.entities.Cliente;
import com.themistech.dasntscam.enums.IssueStatus;
import com.themistech.dasntscam.enums.Rol;
import com.themistech.dasntscam.repositories.IssueRepository;
import com.themistech.dasntscam.requests.IssueRequest;
import com.themistech.dasntscam.repositories.ClienteRepository;
import com.themistech.dasntscam.repositories.PeritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssuesServiceImpl implements IssuesService {

    @Autowired
    private AuthService authService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PeritoRepository peritoRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public List<Issue> getAllIssuesListWithToken(String token) {
        List<Issue> issueList = null;
        User user = authService.getUserWithToken(token);

        if(user.getRol() == Rol.cliente){
            issueList = getAllIssuesListOfUser(user);
        } else if (user.getRol() == Rol.perito) {
            issueList = getAllIssuesUnassignedList(user);
        }
        return issueList;
    }


    @Override
    public void createIssue(IssueRequest issueRequest, User user) {
        Cliente cliente = clienteRepository.findByUsuario(user)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Issue issue = new Issue();
        issue.setVideoId(issueRequest.getVideoId());
        issue.setEstado(IssueStatus.SIN_ASIGNAR);
        issue.setNombre(issueRequest.getNombre());
        issue.setDescripcion(issueRequest.getDescripcion());
        issue.setCliente(cliente);
        issue.setFechaCreacion(LocalDateTime.now());
        issueRepository.save(issue);
    }

    @Override
    public List<Issue> getAllIssuesListOfUser(User user){
        return issueRepository.findAllByUsuario(user);
    }

    @Override
    public List<Issue> getAllIssuesUnassignedList(User user){
        return issueRepository.findByEstado(IssueStatus.SIN_ASIGNAR);
    }

}
