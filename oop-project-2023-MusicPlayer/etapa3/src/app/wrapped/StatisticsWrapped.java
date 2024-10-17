package app.wrapped;

import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.UserType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class StatisticsWrapped {
    @Getter
    private static HashMap<String, WrappedCommand> dataBase;

    /**
     * Creates a new instance of the dataBase and adds all the starting users to said dataBase
     * @param users the user that the admin starts with
     */
    public static void resetDataBase(final List<String> users) {
        dataBase = new HashMap<>();
        users.forEach(user -> dataBase.put(user, new UserWrapped()));
    }

    /**
     * Adds a new WrappedCommand to the dataBase
     * @param user the name of the new user
     */
    public static void addNewStatistic(final UserType user) {
        if (user.getClass() == User.class) {
            dataBase.putIfAbsent(user.getUsername(), new UserWrapped());
        } else if (user.getClass() == Artist.class) {
            dataBase.putIfAbsent(user.getUsername(), new ArtistWrapped());
        } else if (user.getClass() == Host.class) {
            dataBase.putIfAbsent(user.getUsername(), new HostWrapped());
        }
    }

    /**
     * Checks if the given user is empty
     * @param username the username
     * @return true if it is empty
     */
    public static boolean isUserEmpty(final String username) {
        return dataBase.get(username).isNotEmpty();
    }

    /**
     * Shows the unwrapped statistics at the moment
     * @param username name of the user, that will be used as the key
     * @return the statistics of said user
     */
    public static ObjectNode showStatistics(final String username) {
        return dataBase.get(username).showStatistics();
    }

    /**
     * Updates the unwrapped statistics of the user
     * @param username name of the user, that will be used as the key
     */
    public static void updateStatistics(final String username, final LibraryEntry entry) {
        WrappedCommand command = dataBase.get(username);
        if (command != null) {
            command.updateStatistics(username, entry);
        }
        if (entry.getClass() == Song.class) {
            WrappedCommand artistCommand = dataBase.get(((Song) entry).getArtist());
            if (artistCommand != null) {
                artistCommand.updateStatistics(username, entry);
            }
        } else if (entry.getClass() == Episode.class) {
            WrappedCommand hostCommand = dataBase.get(((Episode) entry).getHost());
            if (hostCommand != null) {
                hostCommand.updateStatistics(username, entry);
            }
        }
    }
}
