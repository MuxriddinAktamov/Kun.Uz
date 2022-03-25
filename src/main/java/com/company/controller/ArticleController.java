package com.company.controller;

import com.company.dto.*;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;

import com.company.service.ArticleService;
import com.company.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    @ApiOperation(value = "create method", notes = "Bunda article create qilinadi faqat Adminlar ", nickname = "NickName")
//    @ApiResponses( value = {
//            @ApiResponse(code = 500, message = "Server Error", response = ArticleDTO.class),
//            @ApiResponse(code = 404,message = "Servise not found"),
//            @ApiResponse(code = 200,message = "Succefully retrival",response = ArticleDTO.class,responseContainer = "List")
//    })
    public ResponseEntity<?> create(@Valid @RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ArticleDTO response = articleService.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/publish/{id}")
    @ApiOperation(value = "publish method", notes = "Bunda bor articlelar publish qilinadi faqat Publisherlar ", nickname = "NickName")
    public ResponseEntity<?> publish(@ApiParam(value = "id", readOnly = true)
                                     @PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.PUBLISHER_ROLE);
        articleService.publish(id, jwtDTO.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{content}")
    @ApiOperation(value = "update method", notes = "Bunda article update qilinadi content orqali  faqat Adminlar ", nickname = "NickName")
    public ResponseEntity<ArticleDTO> updateArticleByAdmin(HttpServletRequest request,
                                                           @PathVariable("content") String content, @Valid @RequestBody ArticleDTO dto) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ArticleDTO entity = articleService.updateArticle(dto, content, jwtDTO.getId());
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/delete/{content}")
    @ApiOperation(value = "Delete method", notes = "Bunda  article Delete qilinadi content orqali  faqat Adminlar ", nickname = "NickName")
    public String DeleteArticleByAdmin(HttpServletRequest request, @PathVariable("content") String content) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        articleService.deleteArticle(content);
        return "Succesfully";
    }

    @GetMapping("/getarticlebyid/{id}")
    @ApiOperation(value = "get Article By Id method", notes = "Bunda bor article ni olish uchun ishlatiladi Id orqali faqat Admin lar ", nickname = "NickName")
    public ResponseEntity<?> getArticleById(HttpServletRequest request, @PathVariable("id") Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ArticleEntity response = articleService.getById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/allarticle")
    @ApiOperation(value = "Aricle List method", notes = "Bunda bor articlelar Olinadi  Userlar  ", nickname = "NickName")
    public ResponseEntity<?> getArticleList(HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.USER_ROLE);
        List<ArticleDTO> list = articleService.getAllArticle();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/")
    @ApiOperation(value = "Aricle List with pagination and filter method", notes = "Bunda bor articlelar Olinadi Filter orqali ", nickname = "NickName")
    public ResponseEntity<?> filter(@Valid @RequestBody ArticleFilterDTO dto,
                                    @RequestParam("page") int page, @RequestParam("size") int size) {
//        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        PageImpl<ArticleDTO> articleDTOS = articleService.filterSpe(page, size, dto);
        return ResponseEntity.ok(articleDTOS);
    }


}
