package com.company.controller;

import com.company.dto.*;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Api(tags = "profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/createadmin")
    @ApiOperation(value = "create Admin method ", notes = "Bunda Admin create qilinadi ", nickname = "NickName")
    public ResponseEntity<ProfileDTO> createAdmin(@Valid @RequestBody ProfileDTO dto) {
        ProfileDTO response = profileService.createAdmin(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    @ApiOperation(value = "create method ", notes = "Bunda profile create qilinadi Admin orqali", nickname = "NickName")
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileDTO dto,
                                             HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ProfileDTO response = profileService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{email}")
    @ApiOperation(value = "update method ", notes = "Bunda profile update qilinadi email orqali ", nickname = "NickName")
    public ResponseEntity<ProfileDTO> updateUserByAdmin(HttpServletRequest request, @PathVariable String email,
                                                        @Valid @RequestBody ProfileDTO dto) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ProfileDTO entity = profileService.updateProfile(dto, email);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/delete/{email}")
    @ApiOperation(value = "Delete method ", notes = "Bunda profile Delete qilinadi email orqali ", nickname = "NickName")
    public String DeleteUserByAdmin(HttpServletRequest request, @PathVariable String email) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        profileService.deleteProfile(email);
        return "Succesfuly";
    }

    @GetMapping("allprofile")
    @ApiOperation(value = "GetProfileList method ", notes = "Bunda profillar  olinadi ", nickname = "NickName")
    public ResponseEntity<?> getProfileList(HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<ProfileDTO> list = profileService.getAllProfile();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getprofilebyid/{id}")
    @ApiOperation(value = "GetProfile method ", notes = "Bunda profile olinadi id bo'yicha ", nickname = "NickName")
    public ResponseEntity<?> getProfileById(HttpServletRequest request, @PathVariable Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ProfileEntity response = profileService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    @ApiOperation(value = "filter method ", notes = "Bunda profile filter orqali olinadi ", nickname = "NickName")
    public ResponseEntity<?> filter(@Valid @RequestBody ProfileFilterDTO dto,
                                    @RequestParam("page") int page, @RequestParam("size") int size) {
//        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        PageImpl<ProfileDTO> profileDTOS = profileService.filterSpe(page, size, dto);
        return ResponseEntity.ok(profileDTOS);
    }

}
