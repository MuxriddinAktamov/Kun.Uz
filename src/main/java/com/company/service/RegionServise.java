package com.company.service;

import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionServise {
    @Autowired
    RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto, Integer userId) {

        RegionEntity entity = new RegionEntity();
        entity.setRegion(dto.getRegion());
        entity.setCreatedDate(LocalDateTime.now());

        regionRepository.save(entity);
        dto.setId(entity.getId());
        dto.setProfileId(userId);
        return dto;
    }

    public RegionDTO updateRegion(RegionDTO dto, String region, Integer userId) {
        getRegionByregion(region);
        int entity = regionRepository.update(dto.getRegion(), LocalDateTime.now(), region);

        return dto;
    }

    public RegionEntity getRegionByregion(String region) {
        return regionRepository.findByRegion(region)
                .orElseThrow(() -> new RuntimeException("Region not found"));
    }

    public void deleteRegion(String region) {
        getRegionByregion(region);
        regionRepository.deleteByRegion(region);
    }

    public List<RegionDTO> getAllRegion() {
        List<RegionDTO> list = new LinkedList<>();
        Iterable<RegionEntity> entities = regionRepository.findAll();
        Iterator<RegionEntity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            list.add(toDTO(entityIterator.next()));
        }
        return list;
    }

    public RegionDTO getById(Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isPresent()) {
            return toDTO(optional.get());
        }
        return null;
    }

    public RegionEntity get(String region) {
        return regionRepository.findByRegion(region)
                .orElseThrow(() -> new RuntimeException("Region not found"));
    }
    public RegionEntity get(Integer id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found"));
    }

    public RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setRegion(entity.getRegion());
        dto.setId(entity.getId());
        dto.setCreate_date(entity.getCreatedDate());
        return dto;
    }


}
