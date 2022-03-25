package com.company.repository;

import com.company.dto.RegistrationDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>, JpaSpecificationExecutor<ProfileEntity> {


    Optional<ProfileEntity> findByLoginAndPswd(String login, String pswd);

    Optional<ProfileEntity> findByEmail(String email);
    Optional<ProfileEntity> findByLogin(String login);


    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.name=:name, p.surname=:surname,p.pswd=:pswd,p.login=:login,p.role=:status where p.email=:email")
    Optional<ProfileEntity> update(@Param("name") String name, @Param("surname") String surname,
                                   @Param("pswd") String pswd, @Param("login") String login,
                                   @Param("status") ProfileRole status, @Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "delete from ProfileEntity s where s.email=:email")
    int deleteByEmail(@Param("email") String email);
}
