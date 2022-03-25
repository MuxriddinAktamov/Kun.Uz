package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.LikeOrDislikeDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.entity.ArticleEntity;
import com.company.enums.ProfileRole;
import com.company.service.LikeOrDislikeServise;
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
@RequestMapping("/likeordislike")
@Api(tags = "attitude")
public class LikeOrDislikeController {
    @Autowired
    private LikeOrDislikeServise likeOrDislikeServise;

    @PostMapping("")
    @ApiOperation(value = "create method ",notes = "Bunda Like yoki Dislike create qilinadi ",nickname = "NickName")
    public ResponseEntity<?> create(@Valid @RequestBody LikeOrDislikeDTO dto,
                                    HttpServletRequest request) {
        System.out.println(request.getAttribute("jwtDTO"));
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        LikeOrDislikeDTO response = likeOrDislikeServise.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "Delete method ",notes = "Bunda Like yoki Dislike delete qilinadi id bo'yicha ",nickname = "NickName")
    public String DeleteLikeOrDislike(HttpServletRequest request, @PathVariable("id") Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        likeOrDislikeServise.deleteLikeOrDislike(id);
        return "Succesfully";
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "update method ",notes = "Bunda Like yoki Dislike update qilinadi id orqali ",nickname = "NickName")
    public ResponseEntity<LikeOrDislikeDTO> updateArticleByAdmin(HttpServletRequest request,
                                                                 @PathVariable("id") Integer id,@Valid @RequestBody LikeOrDislikeDTO dto) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        LikeOrDislikeDTO entity = likeOrDislikeServise.updateArticle(dto, id);
        return ResponseEntity.ok(entity);
    }

    //    Get article like and dislike count
    @GetMapping("/getcount/{id}")
    @ApiOperation(value = "GetCountByArticleId method ",notes = "Bunda Like yoki Dislike soni olinadi article id orqali ",nickname = "NickName")
    public ResponseEntity<?> getCountByArticle_Id(@PathVariable("id") Integer id) {
        Integer count = likeOrDislikeServise.getCountArticleId(id);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/getcountbycommentid/{id}")
    @ApiOperation(value = "GetCountByCommentId method ",notes = "Bunda Like yoki Dislike soni olinadi comment id orqali ",nickname = "NickName")
    public ResponseEntity<?> getCountByCommentId(@PathVariable("id") Integer id) {
        Integer count = likeOrDislikeServise.getCountByCommentId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/getarticlebyprofilelike/{id}")
    @ApiOperation(value = "getarticlebyprofilelike method ",notes = "Bunda ayni bir profile like bosgan article lar",nickname = "NickName")
    public ResponseEntity<?> getarticlebyprofilelike(HttpServletRequest request,@PathVariable("id") Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        List<ArticleEntity> count = likeOrDislikeServise.getarticlebyprofilelike(id);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/getcommetbyprofilelike/{id}")
    @ApiOperation(value = "getcommetbyprofilelike method ",notes = "Bunda ayni bir profile like bosgan commentlar lar",nickname = "NickName")
    public ResponseEntity<?> getcommetbyprofilelike(HttpServletRequest request,@PathVariable("id") Integer id) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        List<ArticleEntity> count = likeOrDislikeServise.getcommetbyprofilelike(id);
        return ResponseEntity.ok(count);
    }
}
