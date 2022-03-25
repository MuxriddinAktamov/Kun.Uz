package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.EmailHistoryDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.EmailHistoryEntity;
import com.company.enums.ProfileRole;
import com.company.service.EmailHistoryServise;
import com.company.service.EmailServise;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/email")
@Api(tags = "Email History")
public class EmailController {
    @Autowired
    private EmailHistoryServise emailHistoryServise;

    @GetMapping("/allEmailHistory")
    @ApiOperation(value = "getEmail List method",notes = "Bunda Email List olinadi faqat Adminlar pagention bilan ",nickname = "NickName")
    public ResponseEntity<?> getEmailList(HttpServletRequest request, Integer size, Integer page) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<EmailHistoryDTO> list = emailHistoryServise.getAllEmail(size, page);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getAllTodaySendEmail/{year}/{month}/{day}")
    @ApiOperation(value = "getALLTodaySendEmail method",notes = "Bunda nugun yozilgan emaillar olinadi faqat Adminlar",nickname = "NickName")
    public ResponseEntity<?> getAllTodaySendEmail(HttpServletRequest request, @PathVariable("year") Integer year,
                                                  @PathVariable("month") Integer month, @PathVariable("day") Integer day) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<EmailHistoryDTO> list = emailHistoryServise.getAllTodaySendEmail(year, month, day);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getLastEmail")
    @ApiOperation(value = "GetLastSendEmail method",notes = "Bunda ohirgi jo'natilgan emailni olinadi user orqali ",nickname = "NickName")
    public ResponseEntity<?> getLastSendEmail(HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.USER_ROLE);
        EmailHistoryEntity entity = emailHistoryServise.getLastEmail();
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/getNotUsedEmail/{size}/{page}")
    @ApiOperation(value = "GetNotUsedEmail method",notes = "Bunda Foydalanilmagan Emaillar Olinadi pagenition bilan AdminRole",nickname = "NickName")
    public ResponseEntity<?> getNotUsedEmail(HttpServletRequest request,@PathVariable("size") Integer size,
                                             @PathVariable("page") Integer page) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<EmailHistoryDTO> entity = emailHistoryServise.getNotUsedEmail(size,page);
        return ResponseEntity.ok(entity);
    }

}
