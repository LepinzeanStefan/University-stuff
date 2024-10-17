package app.wrapped;

import app.audio.Files.Episode;
import app.audio.LibraryEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class HostWrapped implements WrappedCommand {
    private final HashMap<String, Integer> episodeStats;
    private final HashMap<String, Integer> userStats;

    private final int maxSize = 5;

    public HostWrapped() {
        episodeStats = new HashMap<>();
        userStats = new HashMap<>();
    }

    /**
     * Adds one listen to all the fields relevant to a song played
     * @param entry the entry that is played
     */
    @Override
    public void updateStatistics(final String username, final LibraryEntry entry) {
        Episode episode = (Episode) entry;
        episodeStats.put(episode.getName(),
                (episodeStats.getOrDefault(episode.getName(), 0) + 1));
        userStats.put(username,
                (userStats.getOrDefault(username, 0) + 1));
    }

    @Override
    public boolean isNotEmpty() {
        return !episodeStats.isEmpty() || !userStats.isEmpty();
    }

    /**
     * Returns the current statistics of the user
     */
    @Override
    public ObjectNode showStatistics() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        // Sorts the hashmaps of the statistics by its values and puts them in a ObjectNode
        result.putPOJO("topEpisodes",
                episodeStats.entrySet().stream().sorted(
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
        result.put("listeners", userStats.size());
        return result;
    }
}
