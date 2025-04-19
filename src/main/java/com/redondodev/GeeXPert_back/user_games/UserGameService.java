package com.redondodev.GeeXPert_back.user_games;

import com.redondodev.GeeXPert_back.game.Game;
import com.redondodev.GeeXPert_back.game.GameDTO;
import com.redondodev.GeeXPert_back.game.GameRepository;
import com.redondodev.GeeXPert_back.user.User;
import com.redondodev.GeeXPert_back.user.UserRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        Game game = gameRepository.findById(gameDTO.getId()).orElse(null);

        if (game == null) {
            game = Game.builder()
                    .name(gameDTO.getName())
                    .releaseDate(gameDTO.getReleaseDate())
                    .platformsIds(gameDTO.getPlatformsIds())
                    .genresIds(gameDTO.getGenresIds())
                    .coverId(gameDTO.getCoverId())
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

    @Transactional(readOnly = true)
    public List<UserGameDto> getUserGameCollection(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserGame> userGames = userGameRepository.findAllByUser(user);

        return userGames.stream()
                .map(userGame -> new UserGameDto(
                        userGame.getGame().getId(),
                        userGame.getGame().getName(),
                        userGame.getGame().getCoverId(),
                        userGame.getState().name(),
                        userGame.getDate()))
                .toList();
    }

    @Transactional
    public void removeGameFromUserCollection(Integer userId, GameDTO gameDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findById(gameDto.getId())
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
