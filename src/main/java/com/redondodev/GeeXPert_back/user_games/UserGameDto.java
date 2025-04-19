package com.redondodev.GeeXPert_back.user_games;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGameDto {

    private Integer userGameId;
    private String name;
    private String coverId;
    private String state;
    private LocalDate date;

}
