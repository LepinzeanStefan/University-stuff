package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Files.Song;
import app.page_elements.Event;
import app.page_elements.Merch;
import app.page_elements.Notification;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Artist type user, featuring its own page
 */
@Getter
public class Artist extends UserType {
    private final List<Album> albums;
    private final List<Merch> merchs;
    private final List<Event> events;
    private final ArrayList<User> followers;

    static final int MAX_MONTHS = 12;
    static final int FEBRUARY = 2;
    static final int MAX_FEBRUARY_DAYS = 28;
    static final int MAX_MONTH_DAYS = 31;
    static final int MIN_YEAR = 1900;
    static final int MAX_YEAR = 2023;

    private static Admin admin;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        merchs = new ArrayList<>();
        events = new ArrayList<>();
        followers = new ArrayList<>();
    }

    /**
     * Creates a new album and adds it to the album list
     * @param commandInput the command given in input
     * @return success status
     */
    public String addAlbum(final CommandInput commandInput) {
        final String name = commandInput.getName();
        final int releaseYear = commandInput.getReleaseYear();
        final String description = commandInput.getDescription();
        final ArrayList<SongInput> songInputList = commandInput.getSongs();

        ArrayList<Song> songs = new ArrayList<>();
        // Transform the list of SongInputs into a list of Songs
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(),
                    songInput.getAlbum(), songInput.getTags(), songInput.getLyrics(),
                    songInput.getGenre(), songInput.getReleaseYear(),
                    songInput.getArtist()));
        }

        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return getUsername() + " has another album with the same name.";
            }
        }
        // Use a set to check for duplicates
        Set<Song> set = new HashSet<>();
        for (Song element : songs) {
            if (!set.add(element)) {
                return getUsername() + " has the same song at least twice in this album.";
            }
        }

        albums.add(new Album(name, getUsername(), releaseYear, description, songs));
        // Add all the new songs to the Admin list of songs
        admin.addSongs(songs);
        notifyFollowers(new Notification("New Album",
                "New Album from " + getUsername() + "."));
        return getUsername() + " has added new album successfully.";
    }

    /**
     * Creates a new event and adds it to the event list
     * @param name name of the event
     * @param description description of the event
     * @param date date of the event
     * @return success status
     */
    public String addEvent(final String name, final String description,
                           final String date) {

        for (Event event : events) {
            if (event.getName().equals(name)) {
                return getUsername() + " has another event with the same name.";
            }
        }

        final int day = Integer.parseInt(date.substring(0, 2));
        final int month = Integer.parseInt(date.substring(3, 5));
        final int year = Integer.parseInt(date.substring(6));

        if (month < 1 || month > MAX_MONTHS) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }

        if ((month == FEBRUARY && day > MAX_FEBRUARY_DAYS) || day < 1 || day > MAX_MONTH_DAYS) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }

        if (year < MIN_YEAR || year > MAX_YEAR) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }

        events.add(new Event(name, description, date));
        notifyFollowers(new Notification("New Event",
                "New Event from " + getUsername() + "."));
        return getUsername() + " has added new event successfully.";
    }

    /**
     * Creates a new merchandise and adds it to the merch list
     * @param name the name of the merch
     * @param description the description of the merch
     * @param price the price of the merch
     * @return success status
     */
    public String addMerch(final String name, final String description,
                           final int price) {
        for (Merch merch : merchs) {
            if (merch.getName().equals(name)) {
                return getUsername() + " has merchandise with the same name.";
            }
        }

        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }

        merchs.add(new Merch(name, description, price));
        notifyFollowers(new Notification("New Merchandise",
                "New Merchandise from " + getUsername() + "."));
        return getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Show albums array list.
     *
     * @return the array list
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }

        return albumOutputs;
    }

    /**
     * Deletes an album if it is safe to delete
     * @param albumName name of the album that needs to be checked
     * @return True if the album is safely deleted, false if the artist doesn't have the album
     * or if it's unsafe to delete
     */
    public boolean removeAlbum(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                if (!album.isSafeToDelete()) {
                    return false;
                }
                for (Song song : album.getSongs()) {
                    // Should never be false, because it is already checked if the album is safe
                     admin.removeSong(song);
                }
                albums.remove(album);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes artist, if all the albums are safe to delete and no one is currently on his page
     * Practically only deletes its albums, the admin will remove the Artist instance
     * @return boolean representing either success or failure
     */
    public boolean deleteArtist() {
        for (Album album : albums) {
            if (!album.isSafeToDelete()) {
                return false;
            }
        }
        if (admin.isPageSelected(getUsername())) {
            return false;
        }

        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                // Should never be false, because it is already checked if the album is safe
                 admin.removeSong(song);
            }
        }
        albums.clear();
        return true;
    }

    /**
     * Removes an event
     * @param eventName name of the event
     * @return true if the event was deleted
     */
    public boolean removeEvent(final String eventName) {
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                events.remove(event);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the total likes of the artist. They are calculated as the sum of all the likes from
     * all the songs that the artist owns.
     * @return The accumulated sum of all the likes
     */
    public int getTotalLikes() {
        return albums.stream().mapToInt(Album::getTotalLikes).sum();
    }

    /**
     * Adds a new user to the followers list, or removes them if they are already subscribed
     * @param user the user that needs to be added
     * @return subscribe message
     */
    public String subscribe(final User user) {
        if (followers.contains(user)) {
            followers.remove(user);
            return user.getUsername() + " unsubscribed from " + getUsername() + " successfully.";
        } else {
            followers.add(user);
            return user.getUsername() + " subscribed to " + getUsername() + " successfully.";
        }
    }

    /**
     * Notify all users of a new addition
     * @param notification the notification message
     */
    public void notifyFollowers(Notification notification) {
        followers.forEach(user -> user.getInbox().addNotification(notification));
    }
}
