package learn.field_agent.controllers;

import learn.field_agent.models.AppUser;
import learn.field_agent.security.AppUserService;
import learn.field_agent.security.JwtConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtConverter jwtConverter;
    private final AppUserService service;

    public AuthController(AuthenticationManager authManager, JwtConverter jwtConverter, AppUserService service) {
        this.authManager = authManager;
        this.jwtConverter = jwtConverter;
        this.service = service;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authManager.authenticate(token);
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtConverter.getTokenFromUser((User) authentication.getPrincipal());
            HashMap<String, String> whateverMap = new HashMap<>();
            whateverMap.put("jwt_token", jwtToken);
            return new ResponseEntity<>(whateverMap, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PostMapping("/create_account")
    public ResponseEntity<Map<String, Integer>> createAccount(@RequestBody Map<String, String> credentials) {
        AppUser user;

        String username = credentials.get("username");
        String password = credentials.get("password");

        user = service.create(username, password);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("appUserId", user.getAppUserId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}