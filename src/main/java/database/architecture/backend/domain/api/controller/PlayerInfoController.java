package database.architecture.backend.domain.api.controller;

import database.architecture.backend.domain.api.dto.PlayerDTO;
import database.architecture.backend.domain.api.service.PlayerInfoService;
import database.architecture.backend.domain.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerInfoController {

    private final PlayerInfoService playerInfoService;

    @GetMapping("/type/{name}")
    public ResponseEntity<String> getPlayerType(@PathVariable("name") String name) {
        Player player = playerInfoService.getPlayerByName(name);
        String type = player.getPlayerPos() == 1 ? "pitcher" : "batter";
        return ResponseEntity.ok(type);
    }

    @GetMapping("/batter/{name}")
    public ResponseEntity<?> getBatterData(@PathVariable("name") String name) {
        PlayerDTO.BatterResponseDTO playerData = playerInfoService.getBatterInfo(name);
        return ResponseEntity.ok(playerData);
    }

    @GetMapping("/pitcher/{name}")
    public ResponseEntity<?> getPitcherData(@PathVariable("name") String name) {
        PlayerDTO.PitcherResponseDTO playerData = playerInfoService.getPitcherInfo(name);
        return ResponseEntity.ok(playerData);
    }

}
