package app.wrapped;

import app.audio.LibraryEntry;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface WrappedCommand {

    /**
     * Shows wrapped statistics
     * @return ObjectNode containing all the statistics
     */
    ObjectNode showStatistics();

    /**
     * Updates statistics of the user from the given entry
     * @param username the name of the user
     * @param entry LibraryEntry
     */
    void updateStatistics(String username, LibraryEntry entry);

    /**
     * Checks if all the statistics fields are empty
     * @return true if there is at least one field which is not empty
     */
    boolean isNotEmpty();
}
