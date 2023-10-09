package com.example.demo.src.followMapping.dto;


import com.example.demo.src.followMapping.entitiy.FollowMapping;
import com.example.demo.src.util.LoginUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FollowMappingDto {
    protected String loginUserEmail;


    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FollowDto extends FollowMappingDto{ //내가 팔로우 하는 사람 (그러면 팔로워에 내가 있어야지)
        private Long followUserId;
        public FollowDto(FollowMapping followMapping){
            // TODO: 10/9/23 과연 이게 맞는 일인가....
            this.loginUserEmail= LoginUtil.getLoginEmail();
            this.followUserId= followMapping.getFollowUser().getId();
        }

    }
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FollowerDto extends FollowMappingDto{ //나를 팔로우 하는 사람 (그러면 팔로우에 내가 있어야지)
        private Long followerUserId;
        public FollowerDto(FollowMapping followMapping){
            this.loginUserEmail= LoginUtil.getLoginEmail();
            this.followerUserId=followMapping.getFollowerUser().getId();
        }
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class PostFollowDto extends FollowMappingDto{
        private Long followUserId; //팔로우 당하는 사람
        private Long followerUserId; // 팔로우 하는 사람(팔로워가 됨)

        public PostFollowDto(FollowMapping followMapping){
            this.loginUserEmail= LoginUtil.getLoginEmail();
            this.followUserId=followMapping.getFollowUser().getId();
            this.followerUserId=followMapping.getFollowerUser().getId();
        }
    }
}
