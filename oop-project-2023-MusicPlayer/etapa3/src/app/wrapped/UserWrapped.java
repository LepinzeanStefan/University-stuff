package app.wrapped;

import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class UserWrapped implements WrappedCommand {
    private final HashMap<String, Integer> artistStats;
    private final HashMap<String, Integer> songStats;
    private final HashMap<String, Integer> genreStats;
    private final HashMap<String, Integer> albumStats;
    private final HashMap<String, Integer> episodesStats;

    private final int maxSize = 5;

    public UserWrapped() {
        artistStats = new HashMap<>();
        songStats = new HashMap<>();
        genreStats = new HashMap<>();
        albumStats = new HashMap<>();
        episodesStats = new HashMap<>();
    }

    /**
     * Checks if there are no statistics
     * @return true if all the fields are empty
     */
    public boolean isNotEmpty() {
        return !artistStats.isEmpty() || !songStats.isEmpty() || !genreStats.isEmpty()
                || !albumStats.isEmpty() || !episodesStats.isEmpty();
    }

    /**
     * Adds one listen to all the fields relevant to a song played
     * @param entry the entry that is played
     */
    @Override
    public void updateStatistics(final String username, final LibraryEntry entry) {
        if (entry.getClass() == Song.class) {
            Song song = (Song) entry;
            songStats.put(song.getName(),
                    (songStats.getOrDefault(song.getName(), 0) + 1));
            artistStats.put(song.getArtist(),
                    (artistStats.getOrDefault(song.getArtist(), 0) + 1));
            genreStats.put(song.getGenre(),
                    (genreStats.getOrDefault(song.getGenre(), 0) + 1));
            albumStats.put(song.getAlbum(),
                    (albumStats.getOrDefault(song.getAlbum(), 0) + 1));
        } else if (entry.getClass() == Episode.class) {
            Episode episode = (Episode) entry;
            episodesStats.put(episode.getName(),
                    (episodesStats.getOrDefault(episode.getName(), 0) + 1));
        }
    }

    /**
     * Returns the current statistics of the user
     */
    @Override
    public ObjectNode showStatistics() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        // Sorts the hashmaps of the statistics by its values and puts them in a ObjectNode
        result.putPOJO("topArtists",
                artistStats.entrySet().stream().sorted(
                        (entry1, entry2) -> {
                            if (entry2.getValue().equals(entry1.getValue())) {
                                return entry1.getKey().compareTo(entry2.getKey());
                            }
                            return entry2.getValue().compareTo(entry1.getValue());
                        })
                        .limit(maxSize).collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new)));
        result.putPOJO("topGenres",
                genreStats.entrySet().stream().sorted(
                        (entry1, entry2) -> {
                            if (entry2.getValue().equals(entry1.getValue())) {
                                return entry1.getKey().compareTo(entry2.getKey());
                            }
                            return entry2.getValue().compareTo(entry1.getValue());
                        })
                        .limit(maxSize).collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new)));
        result.putPOJO("topSongs",
                songStats.entrySet().stream().sorted(
                        (entry1, entry2) -> {
                            if (entry2.getValue().equals(entry1.getValue())) {
                                return entry1.getKey().compareTo(entry2.getKey());
                            }
                            return entry2.getValue().compareTo(entry1.getValue());
                        })
                        .limit(maxSize).collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new)));
        result.putPOJO("topAlbums",
                albumStats.entrySet().stream().sorted(
                        (entry1, entry2) -> {
                            if (entry2.getValue().equals(entry1.getValue())) {
                                return entry1.getKey().compareTo(entry2.getKey());
                            }
                            return entry2.getValue().compareTo(entry1.getValue());
                        })
                        .limit(maxSize).collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new)));
        result.putPOJO("topEpisodes",
                episodesStats.entrySet().stream().sorted(
                        (entry1, entry2) -> {
                            if (entry2.getValue().equals(entry1.getValue())) {
                                return entry1.getKey().compareTo(entry2.getKey());
                            }
                            return entry2.getValue().compareTo(entry1.getValue());
                        })
                        .limit(maxSize).collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new)));

        return result;
    }
}
