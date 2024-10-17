package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Album class, similar to a playlist, but has methods better suited for artists
 */
@Getter
public final class Album extends AudioCollection {
    private final ArrayList<Song> songs;
    private int followers;
    private final int releaseYear;
    private final String description;

    /**
     * Instantiates a new Audio collection.
     *
     * @param name  the name
     * @param owner the owner
     * @param songs the songs
     * @param releaseYear the release year
     */
    public Album(final String name, final String owner, final int releaseYear,
                 final String description, final ArrayList<Song> songs) {
        super(name, owner);
        this.songs = songs;
        this.releaseYear = releaseYear;
        this.description = description;
        this.followers = 0;
    }

    /**
     * Increase followers.
     */
    public void increaseFollowers() {
        followers++;
    }

    /**
     * Decrease followers.
     */
    public void decreaseFollowers() {
        followers--;
    }

    /**
     * Checks if the album is safe to delete
     * @return true if it is safe to delete
     */
    public boolean isSafeToDelete() {
        if (getNumOfListeners() != 0) {
            return false;
        }
        // Check if no other users are interacting with the songs
        return songs.stream().noneMatch(song -> song.getNumOfInteractions() != 0);
    }

    /**
     * @return The accumulated amount of likes from all the songs in the playlist
     */
    public int getTotalLikes() {
        int result = 0;
        for (Song song : songs) {
            result += song.getLikes();
        }
        return result;
    }

    @Override
    public boolean matchesOwner(final String user) {
        return getOwner().toLowerCase().startsWith(user.toLowerCase());
    }

    @Override
    public boolean matchesDescription(final String otherDescription) {
        return description.toLowerCase().startsWith(otherDescription.toLowerCase());
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }
}
