package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("from Player p where p.username like %:term%")
    List<Player> searchByPlayerLike(@Param("term") String term);

    Player findByUsername(String username);

    Player findByEmail(String email);

    Player findByUsernameAndEmail(String username, String email);

    @Transactional
    @Modifying
    @Query("update Player player set player.username = :username where player.id = :id")
    void updatePlayerUsername(@Param("id") Long id, @Param("username") String username);

    @Transactional
    @Modifying
    @Query("update Player player set player.password = :password where player.id = :id")
    void updatePlayerPassword(@Param("id") Long id, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("update Player player set player.email = :email where player.id = :id")
    void updatePlayerEmail(@Param("id") Long id, @Param("email") String email);

    @Transactional
    @Modifying
    @Query("update Player player set player.firstName = :firstName where player.id = :id")
    void updatePlayerFirstName(@Param("id") Long id, @Param("firstName") String firstName);

    @Transactional
    @Modifying
    @Query("update Player player set player.lastName = :lastName where player.id = :id")
    void updatePlayerLastName(@Param("id") Long id, @Param("lastName") String lastName);

    @Transactional
    @Modifying
    @Query("update Player player set player.profilePicUrl = :profilePicUrl where player.id = :id")
    void updatePlayerProfilePic(@Param("id") Long id, @Param("profilePicUrl") String profilePicUrl);
}
