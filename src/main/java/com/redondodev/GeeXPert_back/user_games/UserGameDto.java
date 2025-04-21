package com.redondodev.GeeXPert_back.user_games;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGameDto {

    private Integer userGameId;
    private String name;
    private String cover;
    private String state;
    private Long date;

}
