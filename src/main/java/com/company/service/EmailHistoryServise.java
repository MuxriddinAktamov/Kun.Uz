package com.company.service;

import com.company.dto.EmailHistoryDTO;
import com.company.entity.EmailHistoryEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.EmailUsedStatus;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.EmailHistoryRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailHistoryServise {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    @Autowired
    private ProfileService profileService;

    public void createEmailHistory(String email, EmailUsedStatus status) {
        if (!email.contains("@gmail.com")) {
            throw new BadRequestException("Wrong Email");
        }

        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setFromEmail("muxriddin200228@gmail.com");
        entity.setToEmail(email);
        entity.setStatus(status);
        entity.setCreatedDate(LocalDateTime.now());

        emailHistoryRepository.save(entity);
    }

    public void updateStatus(String jwt) {
        Integer id = JwtUtil.decodeJwtAndId(jwt);

        ProfileEntity entity = profileService.get(id);
        Optional<EmailHistoryEntity> emailHistoryEntity = emailHistoryRepository.findByToEmail(entity.getEmail());
        if (!emailHistoryEntity.isPresent()) {
            throw new ItemNotFoundException("This email is not exists");
        }
        emailHistoryEntity.get().setStatus(EmailUsedStatus.USED);
        emailHistoryEntity.get().setUsed_Date(LocalDateTime.now());
        emailHistoryRepository.save(emailHistoryEntity.get());
    }

    public List<EmailHistoryDTO> getAllEmail(Integer size, Integer page) {
        Pageable pageable = PageRequest.of(size, page);
        List<EmailHistoryDTO> list = new LinkedList<>();
        Iterable<EmailHistoryEntity> entities = emailHistoryRepository.findAll(pageable);
        Iterator<EmailHistoryEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public List<EmailHistoryDTO> getAllTodaySendEmail(Integer year, Integer moth, Integer sana) {
        List<EmailHistoryDTO> list = new LinkedList<>();

        LocalDate localDate = LocalDate.of(year, moth, sana);
        LocalDateTime localDateTime1 = localDate.atTime(00, 00, 00);
        LocalDateTime localDateTime2 = localDate.atTime(23, 59, 59);

        Iterable<EmailHistoryEntity> entities = emailHistoryRepository.findByCrated_date(localDateTime1, localDateTime2);
        Iterator<EmailHistoryEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public EmailHistoryEntity getLastEmail() {
        Optional<EmailHistoryEntity> emailHistoryEntity = emailHistoryRepository.findByIdOrderByIdDesc();
//        Optional<EmailHistoryEntity> emailHistoryEntity1 = emailHistoryRepository.findTop1ByOrderByCrated_dateDesc();
        if (!emailHistoryEntity.isPresent()){
            throw new ItemNotFoundException("Item Not Found");
        }
        return emailHistoryEntity.get();
    }

    public List<EmailHistoryDTO> getNotUsedEmail(Integer size,Integer page) {
        Pageable pageable = PageRequest.of(size, page);
        List<EmailHistoryDTO> list = new LinkedList<>();
        Iterable<EmailHistoryEntity> entities=emailHistoryRepository.findByStatus(EmailUsedStatus.NOT_USED,pageable);
        Iterator<EmailHistoryEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }



    public EmailHistoryDTO toDTO(EmailHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setId(entity.getId());
        dto.setToEmail(entity.getToEmail());
        dto.setFromEmail(entity.getFromEmail());
        dto.setCrated_date(entity.getCreatedDate());
        dto.setStatus(entity.getStatus());
        dto.setUsed_Date(entity.getUsed_Date());
        return dto;

    }
}
