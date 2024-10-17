package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

import java.util.Objects;

/**
 * The type Audio collection.
 */
@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;
    private int numOfListeners; // Used to check if anybody is listening to the collection

    /**
     * Instantiates a new Audio collection.
     *
     * @param name  the name
     * @param owner the owner
     */
    public AudioCollection(final String name, final String owner) {
        super(name);
        this.owner = owner;
        numOfListeners = 0;
    }

    /**
     * Gets number of tracks.
     *
     * @return the number of tracks
     */
    public abstract int getNumberOfTracks();

    /**
     * Gets track by index.
     *
     * @param index the index
     * @return the track by index
     */
    public abstract AudioFile getTrackByIndex(int index);

    /**
     *
     * @param user the user
     * @return if the user is the owner
     */
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }

    /**
     * Increments the numOfListeners
     */
    public void addListener() {
        numOfListeners++;
    }

    /**
     * Decrements the numOfListeners
     */
    public void removeListener() {
        numOfListeners--;
    }

    /**
     * Checks if two AudioCollections are equal by comparing if they have the same name and owner
     * @param o the other entry to be compared to
     * @return True if the two objects are considered the same
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AudioCollection that = (AudioCollection) o;
        return Objects.equals(getOwner(), that.getOwner());
    }

    /**
     * Creates a hashcode from the collection's name and owner string
     * @return the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getOwner());
    }
}
