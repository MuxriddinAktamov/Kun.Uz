package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.CommentDTO;
import com.company.dto.LikeOrDislikeDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.LikeOrDislikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ActionStatus;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.LikeOrDislikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LikeOrDislikeServise {
    @Autowired
    private LikeOrDislikeRepository likeOrDislikeRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentServise commentServise;

    public LikeOrDislikeDTO create(LikeOrDislikeDTO dto, Integer id) {

        ProfileEntity profile = profileService.get(id);

        LikeOrDislikeEntity entity = new LikeOrDislikeEntity();
        if (dto.getActionStatus().equals(ActionStatus.ARTICLE)) {
            ArticleEntity action = articleService.get(dto.getActionId());
            entity.setActionId(action.getId());
            entity.setActionStatus(ActionStatus.ARTICLE);
        }
        if (dto.getActionStatus().equals(ActionStatus.COMMENT)) {
            CommentEntity action = commentServise.getById(dto.getActionId());
            entity.setActionId(action.getId());
            entity.setActionStatus(ActionStatus.COMMENT);
        }
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(profile);
        entity.setStatus(dto.getStatus());

        likeOrDislikeRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void deleteLikeOrDislike(Integer id) {
        get(id);
        likeOrDislikeRepository.deleteById(id);
    }

    public LikeOrDislikeEntity get(Integer id) {
        return likeOrDislikeRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Like Or Dislike not found"));
    }

    public LikeOrDislikeDTO updateArticle(LikeOrDislikeDTO dto, Integer id) {
//        getProfileByEmail(email);
//        Optional<ProfileEntity> entity = profileRepository.update(dto.getName(), dto.getSurname(),
//                dto.getPswd(), dto.getLogin(), dto.getStatus(), email);
//        ProfileDTO profile = new ProfileDTO();
//        profile.setName(entity.get().getName());
//        profile.setSurname(entity.get().getSurname());
//        profile.setPswd(entity.get().getPswd());
//        profile.setLogin(entity.get().getLogin());
//        profile.setStatus(entity.get().getStatus());
//        profile.setEmail(email);
        Optional<LikeOrDislikeEntity> entit = likeOrDislikeRepository.findById(id);
        if (!entit.isPresent()) {
            throw new ItemNotFoundException("LikeOrDislike Not Found");
        }
        Optional<LikeOrDislikeEntity> entity = likeOrDislikeRepository.update(dto.getStatus(), dto.getProfileId(), dto.getActionId(), id);

        LikeOrDislikeDTO dto1 = toDTO(entity.get());
        return dto1;
    }

    public LikeOrDislikeDTO toDTO(LikeOrDislikeEntity entity) {
        LikeOrDislikeDTO dislikeDTO = new LikeOrDislikeDTO();
        dislikeDTO.setId(entity.getId());
        dislikeDTO.setStatus(entity.getStatus());
        dislikeDTO.setProfileId(entity.getProfileId().getId());
        dislikeDTO.setActionId(entity.getActionId());
        dislikeDTO.setCreated_date(entity.getCreatedDate());
        return dislikeDTO;
    }

    public Integer getCountArticleId(Integer id) {
        int count = likeOrDislikeRepository.findBycountByStatus(id);
        return count;
    }

    public Integer getCountByCommentId(Integer id) {
        int count = likeOrDislikeRepository.findBycountByCommentId(id);
        return count;
    }

    public List<ArticleEntity> getarticlebyprofilelike(Integer id) {
        List<ArticleEntity> count = likeOrDislikeRepository.getprofileLikeArticleList(id);
        return count;
    }
    public List<ArticleEntity> getcommetbyprofilelike(Integer id) {
        List<ArticleEntity> count = likeOrDislikeRepository.getprofileLikeArticleList(id);
        return count;
    }
}
