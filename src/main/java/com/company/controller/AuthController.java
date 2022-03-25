package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.EmailUsedStatus;
import com.company.service.AuthService;
import com.company.service.EmailHistoryServise;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailHistoryServise emailHistoryServise;

    @PostMapping
    @ApiOperation(value = "Login method",notes = "Sekinroq ishlamay qolishi mumkin",nickname = "NickName")
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody AuthorizationDTO dto) {
        ProfileDTO response = authService.authorization(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    @ApiOperation(value = "Registration method",notes = "Registrtion method",nickname = "NickName")
    public ResponseEntity<RegistrationDTO> registration(@Valid @RequestBody RegistrationDTO dto) {

        RegistrationDTO response = authService.registration(dto);
        emailHistoryServise.createEmailHistory(dto.getEmail(), EmailUsedStatus.NOT_USED);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/verification/{jwt}")
    @ApiOperation(value = "Registration Verification method",notes = "Verification method",nickname = "NickName")
    public ResponseEntity<RegistrationDTO> verification(@PathVariable("jwt") String jwt) {
        authService.verification(jwt);
        emailHistoryServise.updateStatus(jwt);
        return ResponseEntity.ok().build();

    }
}
