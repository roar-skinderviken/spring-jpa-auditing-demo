package no.mycompany.auditing;

import no.mycompany.auditing.repository.AuditorAwareImpl;
import no.mycompany.auditing.repository.SpotifyPlayList;
import no.mycompany.auditing.repository.SpotifyPlayListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // use settings from application-test.yml
@DataJpaTest
@Import(AuditorAwareImpl.class)
class SpotifyPlayListRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    SpotifyPlayListRepository sut;

    @TestConfiguration
    static class MySQLTestContainerConfiguration {

        @Bean
        @ServiceConnection
        MySQLContainer<?> mysqlContainer() {
            return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
        }
    }

    @Test
    void saveNew_givenValidPlayList_expectAllAuditAwareColumnsToBePopulated() {
        var savedPlayList = sut.save(createValidPlayListInTest());

        // assert that all auditor aware fields are populated
        assertNotNull(savedPlayList.getCreatedAt());
        assertEquals("system", savedPlayList.getCreatedBy());
        assertNotNull(savedPlayList.getUpdatedAt());
        assertEquals("system", savedPlayList.getUpdatedBy());
    }

    @Test
    void updateExisting_givenExistingPlayListInDb_expectOnlyLastModifiedColsToBeUpdated() {
        // emulate existing record in db
        var existingPlayListInDb = entityManager.persist(createValidPlayListInTest());
        var originalUpdateAt = existingPlayListInDb.getUpdatedAt();

        // get and update the existing record
        var playListToUpdate = sut.findById(existingPlayListInDb.getId()).orElseThrow();
        playListToUpdate.setPlaylistId("~newPlaylistId~");
        sut.save(playListToUpdate);

        entityManager.flush(); // see the update statement in console

        // assert that updatedAt is updated
        var updatedPlayList = entityManager.find(SpotifyPlayList.class, existingPlayListInDb.getId());
        assertTrue(updatedPlayList.getUpdatedAt().isAfter(originalUpdateAt));
    }

    private static SpotifyPlayList createValidPlayListInTest() {
        return SpotifyPlayList.builder()
                .playlistId("~playlistId~")
                .sun("̃~sun~")
                .moon("~moon~")
                .build();
    }
}
