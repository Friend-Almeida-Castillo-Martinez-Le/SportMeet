package com.codeup.sportmeet.services;

import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.PlayerWithRoles;
import com.codeup.sportmeet.repositories.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlayerDetailsLoader implements UserDetailsService {
    private final PlayerRepository players;

    public PlayerDetailsLoader(PlayerRepository players) {
        this.players = players;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = players.findByUsername(username);
        if (player == null) {
            throw new UsernameNotFoundException("No player found for" + username);
        }
        return new PlayerWithRoles(player);
    }
}
