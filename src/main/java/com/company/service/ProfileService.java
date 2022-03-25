package com.company.service;

import com.company.dto.*;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.ProfileCustomRepositoryImpl;
import com.company.repository.ProfileRepository;
import com.company.entity.ProfileEntity;
import com.company.spec.ArticleSpecification;
import com.company.spec.ProfileSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepositoryImpl profileCustomRepository;

    public ProfileDTO create(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLogin(dto.getLogin());
        entity.setPswd(dto.getPswd());
        entity.setStatus(dto.getStatus());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());

        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }


    public ProfileDTO createAdmin(ProfileDTO dto) {

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLogin(dto.getLogin());
        entity.setPswd(dto.getPswd());
        entity.setStatus(dto.getStatus());
        entity.setLastActiveDate(LocalDateTime.now());
        entity.setEmail(dto.getEmail());
        entity.setRole(ProfileRole.ADMIN_ROLE);


        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public ProfileEntity getProfileByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ItemNotFoundException("Email is null or is empty");
        }
        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException("Profile not found"));
    }

    public ProfileDTO updateProfile(ProfileDTO dto, String email) {
        getProfileByEmail(email);

        Optional<ProfileEntity> entity = profileRepository.update(dto.getName(), dto.getSurname(),
                dto.getPswd(), dto.getLogin(), dto.getRole(), email);

        ProfileDTO dto1 = toDTO(entity.get());

        return dto1;
    }

    public void deleteProfile(String email) {
        getProfileByEmail(email);
        profileRepository.deleteByEmail(email);
    }

    public List<ProfileDTO> getAllProfile() {
        List<ProfileDTO> list = new LinkedList<>();
        Iterable<ProfileEntity> entities = profileRepository.findAll();
        Iterator<ProfileEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Profile not found"));
    }

    public ProfileDTO getById(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isPresent()) {
            return toDTO(optional.get());
        }
        return null;
    }

    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setLogin(entity.getLogin());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setStatus(entity.getStatus());
        dto.setPswd(entity.getPswd());
        return dto;
    }


    public PageImpl<ProfileDTO> filter(int page, int size, ProfileFilterDTO filterDTO) {
        PageImpl<ProfileEntity> entityPage = profileCustomRepository.filter(page, size, filterDTO);

        List<ProfileDTO> profileDTOList = entityPage.stream().map(profileEntity -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(profileEntity.getId());
            dto.setName(profileEntity.getName());
            dto.setSurname(profileEntity.getSurname());
            dto.setRole(profileEntity.getRole());
            dto.setStatus(profileEntity.getStatus());
            dto.setEmail(profileEntity.getEmail());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(profileDTOList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    public PageImpl<ProfileDTO> filterSpe(int size, int page, ProfileFilterDTO dto) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
//        Pageable pageable=PageRequest.of(page,size, Sort.Direction.DESC,"id");

        Specification<ProfileEntity> spec = null;
        if (dto.getStatus() != null) {
            spec = Specification.where(ProfileSpecification.status(dto.getStatus()));
        } else {
            spec = Specification.where(ProfileSpecification.status(ProfileStatus.ACTIVE));
        }
        if (dto.getProfileId() != null) {
            spec.and(ProfileSpecification.id(dto.getProfileId()));
        }
        if (dto.getName() != null || !dto.getName().isEmpty()) {
            spec.and(ProfileSpecification.equal("name", dto.getName()));
        }
        if (dto.getSurname() != null || !dto.getSurname().isEmpty()) {
            spec.and(ProfileSpecification.equal("surname", dto.getSurname()));
        }
        if (dto.getEmail() != null || !dto.getEmail().isEmpty()) {
            spec.and(ProfileSpecification.equal("email", dto.getEmail()));

        }
        if (dto.getRole() != null) {
            spec.and(ProfileSpecification.role(dto.getRole()));
        }
        if (dto.getToDate() != null && dto.getFromDate() != null) {
            spec.and(ProfileSpecification.date(dto.getFromDate(), dto.getToDate()));
        }
        Page<ProfileEntity> profileEntitiPage = profileRepository.findAll(spec, pageable);
        ProfileFilterDTO dto1 = new ProfileFilterDTO();
        List<ProfileDTO> dtoList =profileEntitiPage.getContent().stream().map(this::toDTO).collect(Collectors.toList());
                System.out.println(profileEntitiPage.getTotalElements());
        return new PageImpl<>(dtoList,pageable,profileEntitiPage.getTotalElements());
    }
}
