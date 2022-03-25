package com.company.controller;

import com.company.dto.ProfileJwtDTO;
import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.service.RegionServise;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/region")
@Api(tags = "Region")
public class RegionController {
    @Autowired
    private RegionServise regionServise;

    @PostMapping("/creatregion")
    @ApiOperation(value = "create method ",notes = "Bunda Region create qilinadi Admin orqali ",nickname = "NickName")
    public ResponseEntity<RegionDTO> createRegionByAdmin(HttpServletRequest request,@Valid @RequestBody RegionDTO dto) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        RegionDTO regionDTO = regionServise.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(regionDTO);
    }


    @PostMapping("/update/{region}")
    @ApiOperation(value = "update method ",notes = "Bunda Region uodate qilinadi id orqali faqat Adminlar",nickname = "NickName")
    public ResponseEntity<RegionDTO> updateRegionByAdmin(HttpServletRequest request, @PathVariable String region,
                                                         @Valid @RequestBody RegionDTO dto) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        RegionDTO entity = regionServise.updateRegion(dto, region, jwtDTO.getId());
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/delete/{region}")
    @ApiOperation(value = "Delete method ",notes = "Bunda Region delete qilinadi id orqali faqat Adminlar",nickname = "NickName")
    public String DeleteRegionByAdmin(HttpServletRequest request, @PathVariable String region) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        regionServise.deleteRegion(region);
        return "Succesfully";
    }


    @GetMapping("allregion")
    @ApiOperation(value = "Get Regions method ",notes = "Bunda Regionlar olinadi faqat Admin orqali",nickname = "NickName")
    public ResponseEntity<?> getRegionList(HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request,ProfileRole.ADMIN_ROLE);
        List<RegionDTO> list = regionServise.getAllRegion();
        return ResponseEntity.ok(list);

    }


    @GetMapping("/getregionbyid/{id}")
    @ApiOperation(value = "GetRegionById method ",notes = "Bunda Region olinadi id orqali faqat Admin orqali",nickname = "NickName")
    public ResponseEntity<?> getRgionById(HttpServletRequest request,@PathVariable Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request,ProfileRole.ADMIN_ROLE);
        RegionEntity response = regionServise.get(id);
        return ResponseEntity.ok(response);
    }

}
