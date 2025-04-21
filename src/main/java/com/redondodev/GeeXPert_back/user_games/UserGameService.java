package com.redondodev.GeeXPert_back.user_games;

import com.redondodev.GeeXPert_back.game.Game;
import com.redondodev.GeeXPert_back.game.GameDTO;
import com.redondodev.GeeXPert_back.game.GameRepository;
import com.redondodev.GeeXPert_back.user.User;
import com.redondodev.GeeXPert_back.user.UserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class UserGameService {

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserGameRepository userGameRepository;

    @Transactional
    public void addGameToUserCollection(Integer userId, GameDTO gameDTO) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Game game = gameRepository.findByName(gameDTO.getName()).orElse(null);

            if (game == null) {
                LocalDate releaseDate = null;
                if (gameDTO.getFirst_release_date() != null) {
                    long timestamp = gameDTO.getFirst_release_date();
                    releaseDate = Instant.ofEpochSecond(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                }

                game = Game.builder()
                        .name(gameDTO.getName())
                        .releaseDate(releaseDate) // Allow null release date
                        .platformsIds(gameDTO.getPlatforms() != null
                                ? joinListToString(gameDTO.getPlatforms())
                                : null)
                        .genresIds(gameDTO.getGenres() != null
                                ? joinListToString(gameDTO.getGenres())
                                : null)
                        .cover(gameDTO.getCover() != null ? gameDTO.getCover() : null)
                        .rating(gameDTO.getRating())
                        .build();

                gameRepository.save(game);
            }

            if (!userGameRepository.existsByUserAndGame(user, game)) {
                UserGame userGame = UserGame.builder()
                        .user(user)
                        .game(game)
                        .state(UserGame.State.PENDING)
                        .date(LocalDate.now())
                        .build();
                userGameRepository.save(userGame);
            } else {
                throw new RuntimeException("Game already in collection");
            }

        } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException e) {
            throw new RuntimeException("Conflict detected: The game was updated or deleted by another transaction", e);
        }
    }


    private String joinListToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            if (i < list.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    @Transactional(readOnly = true)
    public List<UserGameDto> getUserGameCollection(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserGame> userGames = userGameRepository.findAllByUser(user);

        return userGames.stream()
                .map(userGame -> new UserGameDto(
                        userGame.getGame().getId(),
                        userGame.getGame().getName(),
                        userGame.getGame().getCover(),
                        userGame.getState().name(),
                        userGame.getDate() != null
                                ? userGame.getDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
                                : null))
                .toList();
    }

    @Transactional
    public void removeGameFromUserCollection(Integer userId, Integer gameId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        UserGame userGame = userGameRepository.findByUserAndGame(user, game)
                .orElseThrow(() -> new RuntimeException("Game not found in user's collection"));

        userGameRepository.delete(userGame);
    }

    @Transactional
    public void updateUserGameStatus(Integer userId, Integer gameId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        UserGame userGame = userGameRepository.findByUserAndGame(user, game)
                .orElseThrow(() -> new RuntimeException("Game not found in user's collection"));

        UserGame.State newState = UserGame.State.valueOf(status.toUpperCase());
        userGame.setState(newState);

        userGameRepository.save(userGame);
    }

}
