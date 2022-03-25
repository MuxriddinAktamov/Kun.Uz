package com.company;

import com.company.dto.*;
import com.company.entity.RegionEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.repository.ArticleCustomRepositoryImpl;
import com.company.service.ArticleService;
import com.company.service.CommentServise;
import com.company.service.ProfileService;
import com.company.service.RegionServise;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class Lesson63ApplicationTests {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RegionServise regionServise;
    @Autowired
    private CommentServise commentServise;
    @Autowired
    private ArticleCustomRepositoryImpl articleCustomRepository;


    @Test
    void createProfile() {
//        ProfileDTO profileDTO = new ProfileDTO();
//        profileDTO.setName("Admin");
//        profileDTO.setSurname("Adminjon");
//        profileDTO.setLogin("admin");
//        profileDTO.setEmail("admin@gmail.com");
//        profileDTO.setPswd("123");
//        profileDTO.setRole(ProfileRole.ADMIN_ROLE);
//
//        profileService.create(profileDTO);

//        ProfileDTO profileDTO = new ProfileDTO();
//        profileDTO.setName("Ali");
//        profileDTO.setSurname("Aliyev");
//        profileDTO.setLogin("Alish");
//        profileDTO.setEmail("ali@gmail.com");
//        profileDTO.setPswd("123alish");
//        profileDTO.setRole(ProfileRole.ADMIN_ROLE);
//        profileService.create(profileDTO);
//
//        RegionDTO dto = new RegionDTO();
//        dto.setCreate_date(LocalDateTime.now());
//        dto.setProfileId(1);
//        dto.setRegion("Toshknt");
//        regionServise.create(dto, 1);

    }

    @Test
    public void createArticle() {
//        ArticleDTO dto = new ArticleDTO();
//        RegionEntity entity = regionServise.get("Toshknt");
//        dto.setTitle("Dollar kursi");
//        dto.setContent("Dollar kursi pasaymoqda. Xa");
//        dto.setProfileId(1);
//        dto.setRegion(entity.getId());
//        dto.setCreatedDate(LocalDateTime.now());
//
//        articleService.create(dto, 1);

//        RegionDTO dto = new RegionDTO();
//        dto.setRegion("Surxonaryo");
//        dto.setCreate_date(LocalDateTime.now());
//        regionServise.create(dto, 1);

        CommentFilterDTO dto = new CommentFilterDTO();
        dto.setArticleId(1);
        dto.setProfileId(1);
//        dto.setContent("salom");
        dto.setCommentId(1);
        dto.setFromDate(LocalDate.now());
        dto.setToDate(LocalDate.now());
        commentServise.filter(1, 10, dto);
    }

    @Test
    public void ProfileTest() {
        ProfileFilterDTO dto = new ProfileFilterDTO();
        dto.setName("Muxa");
        dto.setSurname("Aktamov");
        dto.setProfileId(1);
        dto.setStatus(ProfileStatus.ACTIVE);
        dto.setEmail("muxriddin200228aktamov@gmail.com");
//        dto.setRole(ProfileRole.USER_ROLE);
        profileService.filter(1, 10, dto);
    }

    @Test
    public void ArticleTest() {
        ArticleFilterDTO dto = new ArticleFilterDTO();
        dto.setTitle("Test");
        articleService.filterSpe(0,10,dto);

    }
}
