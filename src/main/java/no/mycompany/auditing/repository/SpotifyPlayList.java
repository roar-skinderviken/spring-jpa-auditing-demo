package no.mycompany.auditing.repository;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_spotify_playlist", uniqueConstraints = @UniqueConstraint(columnNames = {"playlistId", "sun", "moon"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SpotifyPlayList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playlistId;

    @Column(length = 50)
    private String sun;

    @Column(length = 50)
    private String moon;
}