package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.monetization.MonetizationStatistics;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.User;
import app.user.Host;
import app.user.GenericUser;
import app.user.UserType;
import app.wrapped.StatisticsWrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Admin admin;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        assert user != null;
        String message;
        ArrayList<String> results;
        if (user.isOnline()) {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        } else {
            results = new ArrayList<>();
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.putPOJO("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.select(commandInput.getItemNumber());
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.load();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.playPause();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.repeat();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.shuffle(seed);
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.forward();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.backward();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.like();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.next();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.prev();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.createPlaylist(commandInput.getPlaylistName(),
                    commandInput.getTimestamp());
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.switchPlaylistVisibility(commandInput.getPlaylistId());
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.isOnline()) {
            message = user.follow();
        } else {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        assert user != null;
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Adds a new user, if the username is not taken already
     *
     * @param commandInput the command input
     * @return message object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (admin.userExists(username)) {
            objectNode.put("message", "The username "
                    + username + " is already taken.");
        } else {
            UserType newUser = null;
            switch (commandInput.getType()) {
                case "user" -> newUser = GenericUser
                        .createUser(GenericUser.Type.USER,
                                commandInput.getUsername(),
                                commandInput.getAge(),
                                commandInput.getCity());
                case "artist" -> newUser = GenericUser
                        .createUser(GenericUser.Type.ARTIST,
                                commandInput.getUsername(),
                                commandInput.getAge(),
                                commandInput.getCity());
                case "host" -> newUser = GenericUser
                        .createUser(GenericUser.Type.HOST,
                                commandInput.getUsername(),
                                commandInput.getAge(),
                                commandInput.getCity());
                default -> objectNode.put("message", "Invalid type");
            }
            if (newUser != null) {
                admin.addGenericUser(newUser);
                objectNode.put("message", "The username "
                        + username + " has been added successfully.");
            }
        }

        return objectNode;
    }

    /**
     * Switches the online status of a player.
     *
     * @param commandInput the command input
     * @return message object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not a normal user.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            user.switchStatus();
            objectNode.put("message", commandInput.getUsername()
                    + " has changed status successfully.");
        }

        return objectNode;
    }

    /**
     * Gets a list of all the users that are online
     *
     * @param commandInput the command input
     * @return all the users that are online
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> names = admin.getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(names));

        return objectNode;
    }

    /**
     * Adds a new album
     *
     * @param commandInput the command input
     * @return success message
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        Artist artist = admin.getArtist(commandInput.getUsername());


        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (artist == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not an artist.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message", artist.addAlbum(commandInput));
        }

        return objectNode;
    }

    /**
     * Adds a new podcast
     *
     * @param commandInput the command input
     * @return success message
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        Host host = admin.getHost(commandInput.getUsername());


        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (host == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not a host.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message", host.addPodcast(commandInput));
        }

        return objectNode;
    }

    /**
     * Adds a new announcement
     *
     * @param commandInput the command input
     * @return success message
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        Host host = admin.getHost(commandInput.getUsername());


        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (host == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not a host.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message", host.addAnnouncement(commandInput.getName(),
                    commandInput.getDescription()));
        }

        return objectNode;
    }

    /**
     * Show albums object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        Artist user = admin.getArtist(commandInput.getUsername());
        assert user != null;
        ArrayList<AlbumOutput> albums = user.showAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Shows the page that the user is currently on
     * @param commandInput the command input
     * @return object node with the result
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not a normal user.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message", user.printCurrentPage());
        }
        return objectNode;
    }

    /**
     * Changes to a new page for the selected user
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not a normal user.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message",
                    user.changePage(commandInput.getNextPage()));
        }
        return objectNode;
    }

    /**
     * Adds a new event for the selected artist
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        Artist artist = admin.getArtist(commandInput.getUsername());


        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (artist == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not an artist.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message", artist.addEvent(commandInput.getName(),
                    commandInput.getDescription(), commandInput.getDate()));
        }

        return objectNode;
    }

    /**
     * Adds merchandise for the selected artist
     * @param commandInput the input node
     * @return object node with the success message
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        Artist artist = admin.getArtist(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (artist == null) {
            if (admin.userExists(commandInput.getUsername())) {
                objectNode.put("message", commandInput.getUsername()
                        + " is not an artist.");
            } else {
                objectNode.put("message", "The username "
                        + commandInput.getUsername() + " doesn't exist.");
            }
        } else {
            objectNode.put("message", artist.addMerch(commandInput.getName(),
                    commandInput.getDescription(), commandInput.getPrice()));
        }
        return objectNode;
    }

    /**
     * Gets a list of all the users, artists and hosts
     *
     * @param commandInput the command input
     * @return all the users
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> names = admin.getAllUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(names));

        return objectNode;
    }

    /**
     * Deletes the given user if it exists and is safe to delete
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", admin.deleteUser(commandInput.getUsername()));
        return objectNode;
    }

    /**
     * Removes the given album if it exists and is safe to delete
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", admin.removeAlbum(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }

    /**
     * Removes the given event if it exists
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", admin.removeEvent(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }

    /**
     * Removes the given announcement if it exists
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", admin.removeAnnouncement(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }

    /**
     * Show podcasts object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        Host user = admin.getHost(commandInput.getUsername());
        assert user != null;
        ArrayList<PodcastOutput> podcasts = user.showPodcasts();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(podcasts));

        return objectNode;
    }

    /**
     * Removes the given podcast if it exists and is safe to delete
     * @param commandInput the command input
     * @return object node with the success message
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", admin.removePodcast(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }

    /**
     * Gets top 5 albums.
     *
     * @param commandInput the command input
     * @return the top 5 albums
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets top 5 artists.
     *
     * @param commandInput the command input
     * @return the top 5 artists
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> artists = admin.getTop5Artists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.putPOJO("result", objectMapper.valueToTree(artists));

        return objectNode;
    }

    /**
     * Gets the wrapped statistics for the user
     *
     * @param commandInput the command input
     * @return statistics
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (!StatisticsWrapped.isUserEmpty(commandInput.getUsername())) {
            objectNode.put("message",
                    "No data to show for " + admin.getUserType(commandInput.getUsername())
                            + " " + commandInput.getUsername() + ".");
        } else {
            objectNode.putPOJO("result",
                    StatisticsWrapped.showStatistics(commandInput.getUsername()));
        }

        return objectNode;
    }

    /**
     * Shows the revenue statistics of the app
     *
     * @return statistics
     */
    public static ObjectNode endProgram() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");
        objectNode.putPOJO("result", MonetizationStatistics.showRankings());

        return objectNode;
    }

    /**
     * Buys merchandise from an artist, if the user is currently on the artists page
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode buyMerch(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("message", user.buyMerch(commandInput.getName()));
        }

        return objectNode;
    }

    /**
     * Shows all the merchandise that the given user currently owns
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode seeMerch(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.putPOJO("result", new ArrayList<>(user.getBoughtMerch()));
        }

        return objectNode;
    }

    /**
     * Buy a premium subscription for the given user
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode buyPremium(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.putPOJO("message", user.buyPremium());
        }

        return objectNode;
    }

    /**
     * Cancel the premium subscription for the given user
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode cancelPremium(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.putPOJO("message", user.cancelPremium());
        }

        return objectNode;
    }

    /**
     * Schedules an ad for the given user
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode adBreak(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.putPOJO("message", user.adBreak(commandInput.getPrice(),
                    admin.getAdvertisement()));
        }

        return objectNode;
    }

    /**
     * Subscribes a user to a host/artist
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("message", user.subscribe());
        }

        return objectNode;
    }

    /**
     * Shows a users notifications inbox, and clears the inbox
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.putPOJO("notifications",
                    new ArrayList<>(user.getInbox().getNotifications()));
            user.getInbox().clearInbox();
        }

        return objectNode;
    }

    /**
     * Goes to the previous page of the user
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode previousPage(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("message", user.previousPage());
        }

        return objectNode;
    }

    /**
     * Goes to the previous page of the user
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode nextPage(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("message", user.nextPage());
        }

        return objectNode;
    }

    /**
     * updates the recommended songs
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode updateRecommendations(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("message",
                    user.updateRecommendation(commandInput.getRecommendationType()));
        }

        return objectNode;
    }

    /**
     * loads last recommended song
     *
     * @param commandInput the command input
     * @return message node
     */
    public static ObjectNode loadRecommendations(final CommandInput commandInput) {
        final User user = admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("message", user.loadRecommendation());
        }

        return objectNode;
    }
}
