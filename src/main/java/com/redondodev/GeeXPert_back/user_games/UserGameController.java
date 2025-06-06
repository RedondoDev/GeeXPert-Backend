package com.redondodev.GeeXPert_back.user_games;

import com.redondodev.GeeXPert_back.game.GameDTO;
import com.redondodev.GeeXPert_back.jwt.JwtService;
import com.redondodev.GeeXPert_back.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collection")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserGameController {

    private final UserGameService userGameService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private Integer getJwtUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addGameToCollection(@RequestBody GameDTO gameDTO, HttpServletRequest request) {
        Integer userId = getJwtUserId(request);
        userGameService.addGameToUserCollection(userId, gameDTO);

        System.out.println("Game added to collection: " + gameDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Game added to collection");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/games")
    public ResponseEntity<List<UserGameDto>> getUserGameCollection(HttpServletRequest request) {
        Integer userId = getJwtUserId(request);
        List<UserGameDto> userGameCollection = userGameService.getUserGameCollection(userId);
        return ResponseEntity.ok(userGameCollection);
    }

    @PutMapping("/{gameId}/state")
    public ResponseEntity<String> updateUserGameStatus(
            @PathVariable Integer gameId, @RequestBody String state, HttpServletRequest request) {
        Integer userId = getJwtUserId(request);
        userGameService.updateUserGameStatus(userId, gameId, state);
        return ResponseEntity.ok("Game status updated");
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<String> removeGameFromCollection(@PathVariable Integer gameId, HttpServletRequest request) {
        Integer userId = getJwtUserId(request);
        userGameService.removeGameFromUserCollection(userId, gameId);
        System.out.println();
        return ResponseEntity.ok("Game removed from collection");
    }

}
