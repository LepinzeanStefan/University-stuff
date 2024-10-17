package app.page;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.page_elements.Announcement;
import app.page_elements.Event;
import app.page_elements.Merch;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class representing the user page generator
 */
@Getter
public class Page {
    private Enums.PageType pageType;
    private final Stack<Enums.PageType> history;
    private final Stack<String> usernameHistory;
    private final Stack<Enums.PageType> undoHistory;
    private final Stack<String> usernameUndoHistory;
    private String username;
    static final int MAX_LENGTH = 5;
    private static Admin admin;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    public Page(final Enums.PageType pageType, final String username) {
        this.pageType = pageType;
        this.username = username;
        history = new Stack<>();
        undoHistory = new Stack<>();
        usernameHistory = new Stack<>();
        usernameUndoHistory = new Stack<>();
    }

    /**
     * Changes the page that user is currently on
     * @param nextPage the next page
     * @param name the name of the user that owns the page
     */
    public void changePage(final Enums.PageType nextPage, final String name) {
        history.push(pageType);
        usernameHistory.push(username);
        undoHistory.clear();
        usernameUndoHistory.clear();
        pageType = nextPage;
        username = name;
    }

    /**
     * Prints the page that the user is currently on
     * @return string representing the page contents
     */
    public String printCurrentPage() {
        switch (pageType) {
            case HOME_PAGE -> {
                User user =  admin.getUser(username);
                assert user != null;
                if (!user.isOnline()) {
                    return username + " is offline.";
                }
                List<String> topLikedSongs = user.getLikedSongs().stream()
                        .sorted(Comparator.comparing(Song::getLikes).reversed())
                        .map(LibraryEntry::getName).toList();
                topLikedSongs = topLikedSongs
                        .subList(0, Math.min(topLikedSongs.size(), MAX_LENGTH));
                List<String> topLikedPlaylists = user.getFollowedPlaylists().stream()
                        .sorted(Comparator.comparing(Playlist::getTotalLikes).reversed())
                        .map(LibraryEntry::getName).toList();
                topLikedPlaylists = topLikedPlaylists
                        .subList(0, Math.min(topLikedPlaylists.size(), MAX_LENGTH));

                return "Liked songs:\n\t" + topLikedSongs + "\n\nFollowed playlists:\n\t"
                        + topLikedPlaylists + "\n\nSong recommendations:\n\t"
                        + new ArrayList<>(user.getRecommendedSongs())
                        + "\n\nPlaylists recommendations:\n\t"
                        + new ArrayList<>(user.getRecommendedPlaylists());
            }
            case LIKED_CONTENT_PAGE -> {
                User user =  admin.getUser(username);
                assert user != null;
                if (!user.isOnline()) {
                    return username + " is offline.";
                }
                List<String> topLikedSongs = user.getLikedSongs().stream()
                        .map((s) -> s.getName() + " - " + s.getArtist()).toList();
                List<String> topLikedPlaylists = user.getFollowedPlaylists().stream()
                        .map((p) -> p.getName() + " - " + p.getOwner()).toList();

                return "Liked songs:\n\t" + topLikedSongs + "\n\nFollowed playlists:\n\t"
                        + topLikedPlaylists;
            }
            case ARTIST_PAGE -> {
                Artist artist =  admin.getArtist(username);
                assert artist != null;
                List<String> albums = artist.getAlbums().stream()
                        .map(LibraryEntry::getName).toList();
                List<String>  merch = artist.getMerchs().stream()
                        .map(Merch::display).toList();
                List<String>  events = artist.getEvents().stream()
                        .map(Event::display).toList();

                return "Albums:\n\t" + albums + "\n\nMerch:\n\t" + merch
                        + "\n\nEvents:\n\t" + events;
            }
            case HOST_PAGE -> {
                Host host =  admin.getHost(username);
                assert host != null;
                List<String> podcasts = host.getPodcasts().stream()
                        .map(p -> p.getName() + ":\n\t" + p.getEpisodes().stream()
                                .map(e -> e.getName() + " - " + e.getDescription()).toList()
                                + "\n").toList();
                List<String>  events = host.getAnnouncements().stream()
                        .map(Announcement::display).toList();

                return "Podcasts:\n\t" + podcasts + "\n\nAnnouncements:\n\t" + events;
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Searches for the price of the provided merchandise in the current artist page
     * @param merchName the name of the merchandise
     * @return The price of the merch, or -1 if it cannot be found
     */
    public int getMerchPrice(final String merchName) {
        if (pageType != Enums.PageType.ARTIST_PAGE) {
            return -1;
        }
        AtomicInteger merchPrice = new AtomicInteger(-1);

        Artist artist = admin.getArtist(username);
        assert artist != null;
        artist.getMerchs().forEach(merch -> {
            if (merch.getName().equals(merchName)) {
                merchPrice.set(merch.getPrice());
            }
        });

        return merchPrice.get();
    }

    /**
     * Returns to the previous page in history, if there are any
     * @return true if successful
     */
    public boolean previousPage() {
        if (history.isEmpty()) {
            return false;
        }
        undoHistory.push(pageType);
        usernameUndoHistory.push(username);
        pageType = history.pop();
        username = usernameHistory.pop();
        return true;
    }

    /**
     * Returns to the next page in history, if there are any
     * @return true if successful
     */
    public boolean nextPage() {
        if (undoHistory.isEmpty()) {
            return false;
        }
        history.push(pageType);
        usernameHistory.push(username);
        pageType = undoHistory.pop();
        username = usernameUndoHistory.pop();
        return true;
    }
}
