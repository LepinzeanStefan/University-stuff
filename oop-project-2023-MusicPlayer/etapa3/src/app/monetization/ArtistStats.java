package app.monetization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public final class ArtistStats implements Comparable<ArtistStats> {
    private double merchRevenue;
    private double songRevenue;
    private int ranking;
    private String mostProfitableSong;
    @JsonIgnore
    private HashMap<String, Double> songsRevenue;
    @JsonIgnore
    private static final double ROUND_VALUE = 100.0;

    public ArtistStats() {
        mostProfitableSong = "N/A";
        songsRevenue = new HashMap<>();
    }

    /**
     * Adds revenue for a newly bought merchandise
     * @param amount the amount of credits added
     */
    public void addMerchRevenue(final double amount) {
        merchRevenue += amount;
    }

    /**
     * Adds revenue for a song that a user has listened
     * @param amount the amount of credits added
     */
    public void addSongRevenue(final double amount, final String songName) {
        songRevenue += amount;

        // Also adds the separate song value in a HashMap
        songsRevenue.put(songName, (songsRevenue.getOrDefault(songName, (double) 0) + amount));
    }

    /**
     * Set the topSong to the name that has gained the most revenue.
     * Because this is only called by the endProgram, also rounds all the revenues to two decimals
     */
    public void setTopSong() {
        if (songsRevenue.isEmpty()) {
            return;
        }

        final Map<String, Double> sortedSongs = songsRevenue.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey())).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        mostProfitableSong = sortedSongs.entrySet().iterator().next().getKey();
        songRevenue = Math.round(songRevenue * ROUND_VALUE) / ROUND_VALUE;
    }

    @Override
    public int compareTo(final ArtistStats o) {
        return (int) ((this.merchRevenue + this.songRevenue) - (o.merchRevenue + o.songRevenue));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtistStats that = (ArtistStats) o;
        return Double.compare(getMerchRevenue() + getSongRevenue(),
                that.getMerchRevenue() + that.getSongRevenue()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMerchRevenue() + getSongRevenue());
    }
}
