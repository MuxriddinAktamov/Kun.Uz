package com.company.dto;

import com.company.enums.ArticleStatus;
import com.company.enums.LikeOrDislikeStatus;
import com.company.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileJwtDTO {
    private Integer id;
    private ProfileRole role;
    private ArticleStatus statusa;
    private Integer articleId;
    private Integer profileId;
    private LikeOrDislikeStatus status;

    public ProfileJwtDTO(Integer articleId,Integer profileId, LikeOrDislikeStatus status) {
        this.articleId = articleId;
        this.profileId=profileId;
        this.status=status;
    }

    public ProfileJwtDTO(Integer id, ProfileRole role) {
        this.id = id;
        this.role = role;
    }

    public ProfileJwtDTO(ArticleStatus status) {
        this.statusa = status;
    }

}
