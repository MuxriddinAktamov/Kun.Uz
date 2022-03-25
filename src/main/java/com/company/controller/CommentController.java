package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.CommentDTO;
import com.company.dto.CommentFilterDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.ProfileRole;
import com.company.service.CommentServise;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = "comment")
public class CommentController {
    @Autowired
    private CommentServise commentServise;

    @PostMapping("/create")
    @ApiOperation(value = "create  method",notes = "Bunda comment create qilinadi faqat Statusi publish bo'lgan Articlelarga ",nickname = "NickName")
    public ResponseEntity<?> create(@Valid @RequestBody CommentDTO dto, HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getArticele(request, ArticleStatus.PUBLISHED);
        CommentDTO dto1 = commentServise.create(dto);
        return ResponseEntity.ok(dto1);

    }

    @GetMapping("/getarticlebyid/{id}")
    @ApiOperation(value = "get Comment By Id  method",notes = "Bunda comment olinadi faqat Statusi publish bo'lgan Articlelardan ",nickname = "NickName")
    public ResponseEntity<?> getCommentById(HttpServletRequest request, @PathVariable Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getArticele(request, ArticleStatus.PUBLISHED);
        CommentEntity response = commentServise.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{content}")
    @ApiOperation(value = "update  method",notes = "Bunda comment update qilinadi content orqali faqat adminlar ",nickname = "NickName")
    public ResponseEntity<CommentDTO> updateCommentByAdmin(HttpServletRequest request,
                                                           @PathVariable String content,@Valid @RequestBody CommentDTO dto) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        CommentDTO entity = commentServise.updateComment(dto, content, jwtDTO.getId());
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/delete/{content}")
    @ApiOperation(value = "Delete method",notes = "Bunda comment delete qilinadi content orqali user lar o'zi yozgan commentlarni ",nickname = "NickName")
    public String DeleteCommentByAdmin(HttpServletRequest request, @PathVariable String content) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.USER_ROLE);
        commentServise.deleteComment(content, jwtDTO.getId());
        return "Succesfully";
    }


    @GetMapping("/allcomment")
    @ApiOperation(value = "getCommentList method",notes = "Bunda commentlar olinadi faqat Adminlar tomonidan",nickname = "NickName")
    public ResponseEntity<?> getCommentList(HttpServletRequest request, @RequestParam("page") int page, @RequestParam("size") int size) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<CommentDTO> list = commentServise.getAllComment(size, page);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getprofilecommentlist")
    @ApiOperation(value = "getProfileCommentList method",notes = "Bunda profile yozgan barcha commentlar olinadi pagenition bilan",nickname = "NickName")
    public ResponseEntity<?> getProfileCommentList(HttpServletRequest request, @RequestParam("page") int page, @RequestParam("size") int size) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        List<CommentDTO> list = commentServise.getProfileCommentList(size, page, jwtDTO.getId());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getarticlrcommentlist")
    @ApiOperation(value = "GetArticleCommentList method",notes = "Bunda Article ga yozilgan Commentlar olinadi pagenition bilan",nickname = "NickName")
    public ResponseEntity<?> getArticleCommentList(@PathVariable Integer aricleid, @RequestParam("page") int page, @RequestParam("size") int size) {
        List<CommentDTO> list = commentServise.getArticleCommentList(size, page, aricleid);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/")
    @ApiOperation(value = "filter method",notes = "Bunda comment filter orqali olinadi ",nickname = "NickName")
    public ResponseEntity<?> filter(@Valid @RequestBody CommentFilterDTO dto,
                                    @RequestParam("page") int page, @RequestParam("size") int size){
//        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
      PageImpl<CommentDTO> commentDTOS=commentServise.filterSpe(page, size, dto);
        return ResponseEntity.ok(commentDTOS);
    }

}
