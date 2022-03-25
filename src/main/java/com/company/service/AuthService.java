package com.company.service;

import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.BadRequestException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private EmailServise servise;

    public ProfileDTO authorization(AuthorizationDTO dto) {
        String pswd = DigestUtils.md5Hex(dto.getPassword());

        Optional<ProfileEntity> optional = profileRepository
                .findByLoginAndPswd(dto.getLogin(), pswd);
        if (!optional.isPresent()) {
            throw new RuntimeException("Login or Password incorrect");
        }
        if (!optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new BadRequestException("You are Not Alowed");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(optional.get().getName());
        profileDTO.setSurname(optional.get().getSurname());
        profileDTO.setJwt(JwtUtil.createJwt(optional.get().getId(), optional.get().getRole()));
        return profileDTO;
    }

    public RegistrationDTO registration(RegistrationDTO dto) {
        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already exsists");
        }
        if (profileRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new BadRequestException("Login already exists");
        }
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        System.out.println(pswd);

        ProfileEntity profileDTO = new ProfileEntity();
        profileDTO.setName(dto.getName());
        profileDTO.setSurname(dto.getSurname());
        profileDTO.setEmail(dto.getEmail());
        profileDTO.setLogin(dto.getLogin());
        profileDTO.setPswd(pswd);
        profileDTO.setRole(ProfileRole.USER_ROLE);
        profileDTO.setStatus(ProfileStatus.CREATED);
        profileRepository.save(profileDTO);

        String jwt = JwtUtil.createJwt(profileDTO.getId());
        StringBuilder builder = new StringBuilder();
        builder.append("Salom jigar qalaysan\n");
        builder.append("Agar bu sen bo'lsang shu linkka bos  ");
        builder.append("   http://localhost:8080/auth/verification/" + jwt);
        servise.sendEmail(dto.getEmail(), "Registration ArzonApteka ", builder.toString());

        return dto;
    }

    public void verification(String jwt) {
        Integer id = JwtUtil.decodeJwtAndId(jwt);

        Optional<ProfileEntity> entity = profileRepository.findById(id);
        if (entity.get().getStatus().equals(ProfileStatus.BLOCK)){
            throw new BadRequestException("You are is BLOCK");
        }

        if (!entity.isPresent()) {
            throw new BadRequestException("User Not Found");
        }
        entity.get().setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity.get());
    }

}
