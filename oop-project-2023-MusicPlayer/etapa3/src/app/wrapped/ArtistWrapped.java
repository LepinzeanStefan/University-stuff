package app.wrapped;

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
public final class ArtistWrapped implements WrappedCommand {
    private final HashMap<String, Integer> songStats;
    private final HashMap<String, Integer> albumStats;
    private final HashMap<String, Integer> userStats;

    private final int maxSize = 5;

    public ArtistWrapped() {
        songStats = new HashMap<>();
        albumStats = new HashMap<>();
        userStats = new HashMap<>();
    }

    /**
     * Adds one listen to all the fields relevant to a song played
     * @param entry the entry that is played
     */
    @Override
    public void updateStatistics(final String username, final LibraryEntry entry) {
        Song song = (Song) entry;
        songStats.put(song.getName(),
                (songStats.getOrDefault(song.getName(), 0) + 1));
        albumStats.put(song.getAlbum(),
                (albumStats.getOrDefault(song.getAlbum(), 0) + 1));
        userStats.put(username,
                (userStats.getOrDefault(username, 0) + 1));
    }

    @Override
    public boolean isNotEmpty() {
        return !songStats.isEmpty() || !albumStats.isEmpty() || !userStats.isEmpty();
    }

    /**
     * Returns the current statistics of the user
     */
    @Override
    public ObjectNode showStatistics() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        // Sorts the hashmaps of the statistics by its values and puts them in a ObjectNode
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
        result.putPOJO("topFans",
                userStats.entrySet().stream().sorted(
                                (entry1, entry2) -> {
                                    if (entry2.getValue().equals(entry1.getValue())) {
                                        return entry1.getKey().compareTo(entry2.getKey());
                                    }
                                    return entry2.getValue().compareTo(entry1.getValue());
                                })
                        .limit(maxSize).map(Map.Entry::getKey).collect(Collectors.toList()));
        result.put("listeners", userStats.size());
        return result;
    }
}
