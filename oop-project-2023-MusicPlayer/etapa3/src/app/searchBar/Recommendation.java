package app.searchBar;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.user.User;
import app.utils.Enums;
import app.wrapped.ArtistWrapped;
import app.wrapped.StatisticsWrapped;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static app.searchBar.FilterUtils.filterByGenre;

@Getter
public class Recommendation {
    private static Admin admin;
    private LibraryEntry lastSelected;
    private Enums.SearchType fileType;
    private static final int MINIMUM_DURATION = 30;
    private static final int MAX_GENRE_LIMIT = 3;
    private static final int FIRST_GENRE_LIMIT = 5;
    private static final int SECOND_GENRE_LIMIT = 3;
    private static final int THIRD_GENRE_LIMIT = 2;
    private static final int FAN_SONG_LIMIT = 5;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    public Recommendation() {

    }

    /**
     * Updates recommendation with the currently playing song
     * @param player the audio player
     * @return the song recommendation
     */
    public String randomSongRecommendation(final Player player) {
        if (player.getCurrentAudioFile() == null
                || !player.getCurrentAudioFile().getClass().equals(Song.class)) {
            return null;
        }
        final Song song = (Song) player.getCurrentAudioFile();
        if (player.getRemainingTime() > song.getDuration() - MINIMUM_DURATION) {
            return null;
        }

        List<LibraryEntry> entries = new ArrayList<>(admin.getSongs());
        entries = filterByGenre(entries, song.getGenre());
        Random random = new Random( song.getDuration() - player.getRemainingTime());
        lastSelected = entries.get(random.nextInt(entries.size()));
        fileType = Enums.SearchType.SONG;

        return lastSelected.getName();
    }

    /**
     * Creates a playlist of recommendations from the users favourite genres
     * @param user the given user
     * @return the name of the playlist
     */
    public String randomPlaylistRecommendation(final User user) {
        final Map<String, Long> genreCounter = user.getLikedSongs().stream()
                .collect(Collectors.groupingBy(Song::getGenre, Collectors.counting()));
        for (Playlist playlist : user.getPlaylists()) {
            playlist.getSongs().forEach(song -> genreCounter.put(song.getGenre(),
                    genreCounter.getOrDefault(song.getGenre(), 1L) + 1L));
        }
        for (Playlist playlist : user.getFollowedPlaylists()) {
            playlist.getSongs().forEach(song -> genreCounter.put(song.getGenre(),
                    genreCounter.getOrDefault(song.getGenre(), 1L) + 1L));
        }

        Iterator<Map.Entry<String, Long>> topGenres = genreCounter.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(MAX_GENRE_LIMIT).iterator();

        if (!topGenres.hasNext()) {
            return null;
        }

        Playlist recommendation = new Playlist(user.getUsername() + "'s recommendations",
                "The Algorithm");

        List<LibraryEntry> entries = new ArrayList<>(admin.getSongs());
        List<Song> results = filterByGenre(entries, topGenres.next().getKey())
                .stream().map(entry -> (Song) entry).toList();
        results.stream().sorted(Comparator.comparingInt(Song::getLikes).reversed())
                .limit(FIRST_GENRE_LIMIT).forEach(recommendation::addSong);

        if (topGenres.hasNext()) {
            results = filterByGenre(entries, topGenres.next().getKey())
                    .stream().map(entry -> (Song) entry).toList();
            results.stream().sorted(Comparator.comparingInt(Song::getLikes).reversed())
                    .limit(SECOND_GENRE_LIMIT).forEach(recommendation::addSong);
        }

        if (topGenres.hasNext()) {
            results = filterByGenre(entries, topGenres.next().getKey())
                    .stream().map(entry -> (Song) entry).toList();
            results.stream().sorted(Comparator.comparingInt(Song::getLikes).reversed())
                    .limit(THIRD_GENRE_LIMIT).forEach(recommendation::addSong);
        }

        lastSelected = recommendation;
        fileType = Enums.SearchType.PLAYLIST;

        return recommendation.getName();
    }

    /**
     * Creates a playlist from top 5 fans of the artist owning the song the user is currently listening to
     * @param player player the audio player
     * @return the playlist recommendation
     */
    public String fansPlaylistRecommendation(final Player player) {
        if (player.getCurrentAudioFile() == null
                || !player.getCurrentAudioFile().getClass().equals(Song.class)) {
            return null;
        }
        // Since the unwrapped command already gets the top 5 fans of the artist, use that instead
        ArtistWrapped artistStats = (ArtistWrapped) StatisticsWrapped.getDataBase()
                .get(((Song) player.getCurrentAudioFile()).getArtist());
        List<String> topFans = artistStats.getUserStats().entrySet().stream().sorted(
                        (entry1, entry2) -> {
                            if (entry2.getValue().equals(entry1.getValue())) {
                                return entry1.getKey().compareTo(entry2.getKey());
                            }
                            return entry2.getValue().compareTo(entry1.getValue());
                        })
                .limit(FAN_SONG_LIMIT).map(Map.Entry::getKey).toList();
        if (topFans.isEmpty()) {
            return null;
        }
        Playlist recommendation = new Playlist(((Song) player.getCurrentAudioFile()).getArtist()
                + " Fan Club recommendations", "The Algorithm");

        for (String username : topFans) {
            admin.getUser(username).getLikedSongs().stream()
                    .sorted(Comparator.comparingInt(Song::getLikes))
                    .filter(song -> !recommendation.containsSong(song))
                    .limit(FAN_SONG_LIMIT).forEachOrdered(recommendation::addSong);
        }
        if (recommendation.getSongs().isEmpty()) {
            return null;
        }

        return recommendation.getName();
    }
}
