package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.monetization.MonetizationStatistics;
import app.page.NotificationsInbox;
import app.page.Page;
import app.page_elements.Notification;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.Recommendation;
import app.searchBar.SearchBar;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

/**
 * The normal User.
 */
public class User extends UserType {
    @Getter
    private final ArrayList<Playlist> playlists;
    @Getter
    private final ArrayList<Song> likedSongs;
    @Getter
    private final ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private final Recommendation recommendation;
    @Getter
    private final ArrayList<String> recommendedSongs;
    @Getter
    private final ArrayList<String> recommendedPlaylists;
    private boolean lastSearched;
    @Getter
    private boolean isOnline = true;
    @Getter
    private final Page currPage;
    private static Admin admin;
    @Getter
    private final ArrayList<String> boughtMerch;
    @Getter
    private final NotificationsInbox inbox;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username, age, city);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player(this.getUsername());
        searchBar = new SearchBar(username);
        lastSearched = false;
        currPage = new Page(Enums.PageType.HOME_PAGE, username);
        boughtMerch = new ArrayList<>();
        inbox = new NotificationsInbox();
        recommendation = new Recommendation();
        recommendedSongs = new ArrayList<>();
        recommendedPlaylists = new ArrayList<>();
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;

        return new ArrayList<>(searchBar.search(filters, type));
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;
        if (searchBar.getLastSearchType().equals("artist")) {
            final Artist selected = searchBar.selectArtist(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            currPage.changePage(Enums.PageType.ARTIST_PAGE, selected.getUsername());

            return "Successfully selected %s".formatted(selected.getUsername())
                    + "'s page.";
        } else if (searchBar.getLastSearchType().equals("host")) {
            final Host selected = searchBar.selectHost(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            currPage.changePage(Enums.PageType.HOST_PAGE, selected.getUsername());

            return "Successfully selected %s".formatted(selected.getUsername())
                    + "'s page.";
        } else {
            final LibraryEntry selected = searchBar.select(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!(player.getType().equals("playlist") || player.getType().equals("album"))) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            player.getCurrentAudioFile().removeInteraction();
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        player.getCurrentAudioFile().addInteraction();
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!(type.equals("playlist"))) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();
        admin.getUser(playlist.getOwner()).getInbox()
                .addNotification(new Notification("New Follow",
                        "New Follow from " + getUsername() + "."));

        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (isOnline) {
            player.simulatePlayer(time);
        }
    }

    /**
     * Switches online status
     */
    public void switchStatus() {
        isOnline = !isOnline;
    }

    /**
     * Prints the page the user is currently on
     * @return contents of the page
     */
    public String printCurrentPage() {
        if (!isOnline) {
            return getUsername() + " is offline.";
        }

        return currPage.printCurrentPage();
    }

    /**
     * Changes the page to a new one
     * @return message
     */
    public String changePage(final String nextPage) {
        if (!isOnline) {
            return getUsername() + " is offline.";
        }

        if (nextPage.equals("Home")) {
            currPage.changePage(Enums.PageType.HOME_PAGE, getUsername());
        } else if (nextPage.equals("LikedContent")) {
            currPage.changePage(Enums.PageType.LIKED_CONTENT_PAGE, getUsername());
        } else if (nextPage.equals("Artist") && player.getCurrentAudioFile() != null
                && player.getCurrentAudioFile().getClass().equals(Song.class)) {
            Song song = (Song) player.getCurrentAudioFile();
            currPage.changePage(Enums.PageType.ARTIST_PAGE, song.getArtist());
        } else if (nextPage.equals("Host") && player.getCurrentAudioFile() != null
                && player.getCurrentAudioFile().getClass().equals(Episode.class)) {
            Episode episode = (Episode) player.getCurrentAudioFile();
            currPage.changePage(Enums.PageType.HOST_PAGE, episode.getHost());
        } else {
            return getUsername() + " is trying to access a non-existent page.";
        }
        return getUsername() + " accessed " + nextPage + " successfully.";
    }

    /**
     * Deletes the user, if all the playlists are safe to delete
     * @return true if the user was deleted successfully
     */
    public boolean deleteUser() {
        for (Playlist playlist : playlists) {
            if (playlist.getNumOfListeners() != 0) {
                return false;
            }
        }
        for (Playlist playlist : playlists) {
            admin.removePlaylist(playlist);
        }
        for (Playlist playlist : admin.getPlaylists()) {
            for (Song song : playlist.getSongs()) {
                song.removeInteraction();
            }
            if (followedPlaylists.contains(playlist)) {
                playlist.decreaseFollowers();
            }
        }
        return true;
    }

    /**
     * Attempts to buy merchandise from the artistPage the user is currently on.
     * @param merchName name of the merch
     * @return either a successful message or an error message
     */
    public String buyMerch(final String merchName) {
        if (currPage.getPageType() != Enums.PageType.ARTIST_PAGE) {
            return "Cannot buy merch from this page.";
        }
        final int merchPrice = currPage.getMerchPrice(merchName);

        if (merchPrice == -1) {
            return "The merch " + merchName + " doesn't exist.";
        } else {
            MonetizationStatistics.addMerchRevenue(currPage.getUsername(), merchPrice);
            boughtMerch.add(merchName);
            return getUsername() + " has added new merch successfully.";
        }
    }

    /**
     * Buys premium subscription for the user
     * @return either a successful message or an error message
     */
    public String buyPremium() {
        if (player.getMonetizationCalculator().isUserPremium()) {
            return getUsername() + " is already a premium user.";
        }
        player.getMonetizationCalculator().setUserPremium(true);
        return getUsername() + " bought the subscription successfully.";
    }

    /**
     * Cancels premium subscription for the user
     * @return either a successful message or an error message
     */
    public String cancelPremium() {
        if (!player.getMonetizationCalculator().isUserPremium()) {
            return getUsername() + " is not a premium user.";
        }
        player.getMonetizationCalculator().cancelPremium();
        return getUsername() + " cancelled the subscription successfully.";
    }

    /**
     * Schedules an ad in the player
     * @param value the number of credits given by the ad
     * @param advertisement the ad that needs to be played
     * @return either a successful message or an error message
     */
    public String adBreak(final int value, final Song advertisement) {
        if (player.getCurrentAudioFile() == null
                || !player.getCurrentAudioFile().getClass().equals(Song.class)) {
            return getUsername() + " is not playing any music.";
        }
        player.adBreak(value, advertisement);
        return "Ad inserted successfully.";
    }

    /**
     * Subscribes to a host/artist, if the user is currently on its page
     * @return either a successful message or an error message
     */
    public String subscribe() {
        if (currPage.getPageType() == Enums.PageType.ARTIST_PAGE) {
            return admin.getArtist(currPage.getUsername()).subscribe(this);
        } else if (currPage.getPageType() == Enums.PageType.HOST_PAGE) {
            return admin.getHost(currPage.getUsername()).subscribe(this);
        } else {
            return "To subscribe you need to be on the page of an artist or host.";
        }
    }

    /**
     * Changes the page to the previously searched one
     * @return either a successful message or an error message
     */
    public String previousPage() {
        if (currPage.previousPage()) {
            return "The user " + getUsername() + " has navigated successfully to the previous page.";
        } else {
            return "There are no pages left to go back.";
        }
    }

    /**
     * Changes the page to the next one
     * @return either a successful message or an error message
     */
    public String nextPage() {
        if (currPage.nextPage()) {
            return "The user " + getUsername() + " has navigated successfully to the next page.";
        } else {
            return "There are no pages left to go forward.";
        }
    }

    /**
     * Updates the recommendations page of the user
     * @return either a successful message or an error message
     */
    public String updateRecommendation(String recommendationType) {
        switch (recommendationType) {
            case "random_song" -> {
                String result = recommendation.randomSongRecommendation(player);
                if (result == null) {
                    return "No new recommendations were found";
                }

                recommendedSongs.add(result);
            }
            case "random_playlist" -> {
                String result = recommendation.randomPlaylistRecommendation(this);
                if (result == null) {
                    return "No new recommendations were found";
                }

                recommendedPlaylists.add(result);
            }
            case "fans_playlist" -> {
                String result = recommendation.fansPlaylistRecommendation(player);
                if (result == null) {
                    return "No new recommendations were found";
                }

                recommendedPlaylists.add(result);
            }
            default -> {
                return "Unrecognized recommendation type";
            }
        }

        return "The recommendations for user " + getUsername() + " have been updated successfully.";
    }

    /**
     * Loads the last given recommendation
     * @return either a successful message or an error message
     */
    public String loadRecommendation() {
        if (!isOnline) {
            return getUsername() + " is offline.";
        }
        if (recommendation.getLastSelected() == null) {
            return "No recommendations available.";
        }

        if (recommendation.getFileType() == Enums.SearchType.SONG) {
            player.setSource(recommendation.getLastSelected(), "song");
        } else {
            player.setSource(recommendation.getLastSelected(), "playlist");
        }
        player.pause();

        return "Playback loaded successfully.";
    }
}
