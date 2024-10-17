package app.audio.Files;

import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioFile extends LibraryEntry {
    private final Integer duration;
    private int numOfInteractions;  // Used to check if a file has any interactions with other
    // users, such as being listened to or existing in a playlist

    public AudioFile(final String name, final Integer duration) {
        super(name);
        this.duration = duration;
        this.numOfInteractions = 0;
    }

    /**
     * Increments the numOfInteractions
     */
    public void addInteraction() {
        numOfInteractions++;
    }

    /**
     * Decrements the numOfInteractions
     */
    public void removeInteraction() {
        numOfInteractions--;
    }
}
