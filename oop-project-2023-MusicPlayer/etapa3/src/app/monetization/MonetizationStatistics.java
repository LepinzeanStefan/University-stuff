package app.monetization;

import app.audio.Files.Song;
import app.audio.LibraryEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class MonetizationStatistics {
    private static LinkedHashMap<String, ArtistStats> revenueStatistics;

    /**
     * Reset the statistics of the app
     */
    public static void resetStatistics() {
        revenueStatistics = new LinkedHashMap<>();
    }

    /**
     * Adds a new artist to the map, if they are not already added
     * @param entry Expected to be a Song that the user needs to pay for
     */
    public static void addArtistIfAbsent(final LibraryEntry entry) {
        if (entry.getClass() == Song.class) {
            Song song = (Song) entry;
            if (!revenueStatistics.containsKey(song.getArtist())) {
                revenueStatistics.put(song.getArtist(), new ArtistStats());
            }
        }
    }

    /**
     * Adds revenue from a merchandise transaction
     * @param artistName The name of the artist
     * @param price the price of the transaction
     */
    public static void addMerchRevenue(final String artistName, final double price) {
        // Unlike the addSongRevenue method, it is uncertain if the artist was added in the HashMap
        // beforehand.
        if (!revenueStatistics.containsKey(artistName)) {
            revenueStatistics.put(artistName, new ArtistStats());
        }
        revenueStatistics.get(artistName).addMerchRevenue(price);
    }

    /**
     * Adds revenue from a song that was listened
     * @param price the price that is being paid
     * @param song the song that was listened
     */
    public static void addSongRevenue(final double price, final Song song) {
        // Since the way the player is implemented, an artist is always added if someone listened to
        // at least one of its songs, the get() method should never return null.
        revenueStatistics.get(song.getArtist()).addSongRevenue(price, song.getName());
    }

    /**
     * Sorts all the artist by their revenue and shows their revenue
     * @return ObjectNode containing all the statistics of the artists
     */
    public static ObjectNode showRankings() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        // Used to count the ranking of the artist
        AtomicInteger count = new AtomicInteger();

        revenueStatistics.entrySet().stream().sorted(
                (entry1, entry2) -> {
                    if (entry2.getValue().equals(entry1.getValue())) {
                        return entry1.getKey().compareTo(entry2.getKey());
                    }
                    return entry2.getValue().compareTo(entry1.getValue());
                }
        ).forEach(
                entry -> {
                    entry.getValue().setRanking(count.incrementAndGet());
                    result.putPOJO(entry.getKey(), entry.getValue());
                    entry.getValue().setTopSong();
        });
        return result;
    }
}
