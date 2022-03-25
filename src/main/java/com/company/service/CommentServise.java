package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleFilterDTO;
import com.company.dto.CommentDTO;
import com.company.dto.CommentFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.UnauthorizedException;
import com.company.repository.CommentCustomRepositoryIml;
import com.company.repository.CommentRepository;
import com.company.spec.ArticleSpecification;
import com.company.spec.CommentSpecfication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServise {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentCustomRepositoryIml commentCustomRepositoryIml;

    public CommentDTO create(CommentDTO dto) {


        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticleId(articleService.getById(dto.getArticleId()));
        entity.setProfileId(profileService.get(dto.getProfileId()));
        entity.setCreatedDate(LocalDateTime.now());

        commentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public CommentEntity getById(Integer id) {
        return commentRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Comment not found"));
    }

    public void deleteComment(String content, Integer profileId) {
        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Content is empty or equals null");
        }
        Optional<CommentEntity> comment = commentRepository.findByContent(content);
        if (!comment.get().getProfileId().equals(profileId)) {
            throw new UnauthorizedException("the owner of this content was not found");
        }
        commentRepository.deleteByContent(content);
    }


    public CommentDTO updateComment(CommentDTO dto, String content, Integer profileId) {
        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Content not found");
        }
        Optional<CommentEntity> entity1 = commentRepository.findByContent(content);
        if (!entity1.get().getId().equals(profileId)) {
            throw new UnauthorizedException("the owner of this content was not found");
        }

        Optional<CommentEntity> entity = commentRepository.update(dto.getArticleId(), dto.getProfileId(), LocalDateTime.now(), dto.getContent(), content);

        CommentDTO dto1 = toDTO(entity.get());
        return dto1;
    }

    public List<CommentDTO> getAllComment(Integer size, Integer page) {
        Pageable pageable = PageRequest.of(size, page);
        List<CommentDTO> list = new LinkedList<>();
        Iterable<CommentEntity> entities = commentRepository.findAll(pageable);
        Iterator<CommentEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public List<CommentDTO> getProfileCommentList(Integer profileId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(size, page);
        List<CommentDTO> list = new LinkedList<>();
        Iterable<CommentEntity> entities = commentRepository.findByProfileId(profileId, pageable);
        Iterator<CommentEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public List<CommentDTO> getArticleCommentList(Integer articleId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(size, page);
        List<CommentDTO> list = new LinkedList<>();
        Iterable<CommentEntity> entities = commentRepository.findByArticleId(articleId, pageable);
        Iterator<CommentEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }


    public CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfileId().getId());
        dto.setArticleId(entity.getArticleId().getId());
        dto.setCreare_date(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<CommentDTO> filter(int page, int size, CommentFilterDTO filterDTO) {
        PageImpl<CommentEntity> entityPage = commentCustomRepositoryIml.filter1(page, size, filterDTO);

        List<CommentDTO> commentDTOList = entityPage.stream().map(commenEntity -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(commenEntity.getId());
            dto.setCreare_date(commenEntity.getCreatedDate());
            dto.setArticleId(commenEntity.getArticleId().getId());
            dto.setContent(commenEntity.getContent());
            dto.setProfileId(commenEntity.getProfileId().getId());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(commentDTOList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    public PageImpl<CommentDTO> filterSpe(int page, int size, CommentFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Specification<CommentEntity> spec = Specification.where(CommentSpecfication.isNotNull("id"));
        if (filterDTO.getContent() != null || !filterDTO.getContent().isEmpty()) {
            spec.and(CommentSpecfication.equal("content", filterDTO.getContent()));
        }
        if (filterDTO.getArticleId() != null) {
            spec.and(CommentSpecfication.id("articleId", filterDTO.getArticleId()));
        }
        if (filterDTO.getProfileId() != null) {
            spec.and(CommentSpecfication.id("profileId", filterDTO.getProfileId()));
        }
        if (filterDTO.getToDate()!=null && filterDTO.getFromDate()!=null){
            spec.and(CommentSpecfication.date(filterDTO.getFromDate(),filterDTO.getToDate()));
        }


        Page<CommentEntity> commentEntityPage = commentRepository.findAll(spec, pageable);
        List<CommentDTO> dtoList = commentEntityPage.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        System.out.println(commentEntityPage.getTotalElements());
        return new PageImpl<>(dtoList, pageable, commentEntityPage.getTotalElements());
    }
}
