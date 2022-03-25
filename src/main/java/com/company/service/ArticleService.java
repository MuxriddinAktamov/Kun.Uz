package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.RegionEntity;
import com.company.enums.ArticleStatus;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.ArticleCustomRepositoryImpl;
import com.company.repository.ArticleRepository;
import com.company.spec.ArticleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RegionServise regionServise;
    @Autowired
    private ArticleCustomRepositoryImpl articleCustomRepository;

    public ArticleDTO create(ArticleDTO dto, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);
        RegionEntity entity1 = regionServise.get(dto.getRegion());

        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new BadRequestException("Title can not be null");
        }
        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new BadRequestException("Content can not be null");
        }
        if (entity1 == null) {
            throw new BadRequestException("Region can not be null");
        }


        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle()); // null
        entity.setContent(dto.getContent());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setStatus(ArticleStatus.CREATED);
        entity.setRegion(entity1);
        entity.setProfile(profileEntity);

        articleRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void publish(Integer articleId, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);

        ArticleEntity entity = get(articleId);
        entity.setStatus(ArticleStatus.PUBLISHED);
        entity.setPublishedDate(LocalDateTime.now());
        articleRepository.save(entity);
    }

    public ArticleEntity get(Integer id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found"));
    }

    public ArticleDTO updateArticle(ArticleDTO dto, String content, Integer pulishid) {
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
        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Content not found");
        }
        articleRepository.findByContent(content);

        Optional<ArticleEntity> entity = articleRepository.update(dto.getTitle(), pulishid, dto.getCreatedDate(),
                dto.getPublishedDate(), dto.getProfileId(), content, dto.getRegion());

        ArticleDTO dto1 = toDTO(entity.get());
        return dto1;
    }

    public List<ArticleDTO> getAllArticle() {
        List<ArticleDTO> list = new LinkedList<>();
        Iterable<ArticleEntity> entities = articleRepository.findAll();
        Iterator<ArticleEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public ArticleEntity getById(Integer id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found"));
    }

    public void deleteArticle(String content) {
        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Content is empty or equals null");
        }
        articleRepository.deleteByContent(content);
    }

    public ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setProfileId(entity.getProfile().getId());
//        dto.setPublisherId(entity.getPublisher().getId());
        dto.setRegion(entity.getRegion().getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setPublishedDate(LocalDateTime.now());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<ArticleDTO> filter(int page, int size, ArticleFilterDTO filterDTO) {
        PageImpl<ArticleEntity> entityPage = articleCustomRepository.filter(page, size, filterDTO);

        List<ArticleDTO> articleDTOList = entityPage.stream().map(articleEntity -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setId(articleEntity.getId());
            dto.setContent(articleEntity.getContent());
            dto.setRegion(articleEntity.getRegion().getId());
            dto.setTitle(articleEntity.getTitle());
            dto.setCreatedDate(articleEntity.getCreatedDate());
            dto.setPublishedDate(articleEntity.getPublishedDate());
            dto.setProfileId(articleEntity.getProfile().getId());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(articleDTOList, entityPage.getPageable(), entityPage.getTotalElements());
    }


    public PageImpl<ArticleDTO> filterSpe(int page, int size, ArticleFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

//        Specification<ArticleEntity> title = ((root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get("title"), filterDTO.getTitle());
//        });
//        Specification<ArticleEntity> idSpec = ((root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get("id"), filterDTO.getArticleId());
//        });
//        Specification<ArticleEntity> spec = Specification.where(title);
//        spec.and(idSpec);
//
//        Page<ArticleEntity> articleEntityPage = articleRepository.findAll(spec, pageable);
//        System.out.println(articleEntityPage.getTotalElements());
//        return null;

        Specification<ArticleEntity> spec = null;
        if (filterDTO.getStatus() != null) {
            spec = Specification.where(ArticleSpecification.status(filterDTO.getStatus()));
        } else {
            spec = Specification.where(ArticleSpecification.status(ArticleStatus.PUBLISHED));
        }

        if (filterDTO.getTitle() != null) {
            spec.and(ArticleSpecification.title(filterDTO.getTitle()));
        }
        if (filterDTO.getArticleId() != null) {
            spec.and(ArticleSpecification.equal("id", filterDTO.getArticleId()));
        }
        if (filterDTO.getProfileId() != null) {
            spec.and(ArticleSpecification.equal("profile.id", filterDTO.getProfileId()));
        }
        if (filterDTO.getToDate() != null && filterDTO.getFromDate() != null) {
            spec.and(ArticleSpecification.date(filterDTO.getFromDate(), filterDTO.getToDate()));
        }

        Page<ArticleEntity> articleEntityPage = articleRepository.findAll(spec, pageable);

        List<ArticleDTO> dtoList = articleEntityPage.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        System.out.println(articleEntityPage.getTotalElements());
        return new PageImpl<>(dtoList, pageable, articleEntityPage.getTotalElements());
    }
}
