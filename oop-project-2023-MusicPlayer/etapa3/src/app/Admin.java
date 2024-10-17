package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.UserType;
import app.wrapped.StatisticsWrapped;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The type Admin.
 */
public final class Admin {
    private List<User> users = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Host> hosts = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    @Getter
    private Song advertisement;
    private int timestamp = 0;
    private final int limit = 5;
    private static Admin instance;

    private Admin() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Reset instance.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
        // In this implementation the adBreak song is always the last one in the list,
        // but the methods are built in such a way that they could play different ads if needed
        advertisement = songs.get(songs.size() - 1);
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription(),
                                         podcastInput.getOwner()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets artists.
     *
     * @return the artists
     */
    public List<Artist> getArtists() {
        return new ArrayList<>(artists);
    }

    /**
     * Gets hosts.
     *
     * @return the hosts
     */
    public List<Host> getHosts() {
        return new ArrayList<>(hosts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= limit) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= limit) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Adds a new user to one of the UserType lists
     *
     * @param userType the generic user being provided
     */
    public void addGenericUser(final UserType userType) {
        if (userType instanceof User) {
            users.add((User) userType);
        } else if (userType instanceof Artist) {
            artists.add((Artist) userType);
        } else if (userType instanceof Host) {
            hosts.add((Host) userType);
        } else {
            // Handle the case if UserType is of an unknown type
            System.out.println("Unknown UserType");
        }
        StatisticsWrapped.addNewStatistic(userType);
    }

    /**
     * Checks if a user already exists with the given name
     *
     * @param username the name of the user
     * @return true if the user already exists, false otherwise
     */
    public boolean userExists(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return true;
            }
        }
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @return A list of all the usernames that are currently online
     */
    public List<String> getOnlineUsers() {
        return users.stream().filter(User::isOnline)
                .map(User::getUsername)
                .toList();
    }

    /**
     * Gets artist.
     *
     * @param username the username of the artist
     * @return the artist
     */
    public Artist getArtist(final String username) {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Gets host.
     *
     * @param username the username of the host
     * @return the host
     */
    public Host getHost(final String username) {
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }

    /**
     * Add new songs to the List
     * @param songList the new songs that need to be added
     */
    public void addSongs(final ArrayList<Song> songList) {
        songs.addAll(songList);
    }

    /**
     * Add new podcast to the List
     * @param podcast the new podcast that needs to be added
     */
    public void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * @return A list of all the usernames from all the users, artist or hosts on the platform
     */
    public List<String> getAllUsers() {
        final List<String> userList =
                new ArrayList<>(users.stream().map(User::getUsername).toList());
        userList.addAll(artists.stream().map(Artist::getUsername).toList());
        userList.addAll(hosts.stream().map(Host::getUsername).toList());

        return userList;
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (Artist artist : artists) {
            albums.addAll(artist.getAlbums());
        }
        return albums;
    }

    /**
     * Checks if the page of a certain user is currently searched by another user
     * @param username the name of the user
     * @return true if another user is currently on his page
     */
    public boolean isPageSelected(final String username) {
        for (User user : users) {
            if (user.getCurrPage().getUsername().equals(username)
                    && !user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a song if the song is safe to delete
     *
     * @param song the song
     */
    public void removeSong(final Song song) {
        if (song.getNumOfInteractions() != 0) {
            return;
        }
        for (User user : users) {
            user.getLikedSongs().remove(song);
            for (Playlist playlist : user.getPlaylists()) {
                playlist.removeSong(song);
            }
        }
        songs.remove(song);
    }

    /**
     * Removes an album if the album is safe to delete
     * @param artistName name of the artist
     * @param albumName name of the album
     * @return success status
     */
    public String removeAlbum(final String artistName, final String albumName) {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(artistName)) {
                if (artist.getAlbums().stream()
                        .noneMatch(album -> album.getName().equals(albumName))) {
                    // Checks if there is an album with this name
                    return artistName + " doesn't have an album with the given name.";
                }
                if (artist.removeAlbum(albumName)) {
                    return artistName + " deleted the album successfully.";
                } else {
                    return artistName + " can't delete this album.";
                }
            }
        }
        if (!userExists(artistName)) {
            return  "The username " + artistName + " doesn't exist.";
        }
        return artistName + " is not an artist.";
    }

    /**
     * Deletes the given user
     * @param username the name of the user
     * @return success message
     */
    public String deleteUser(final String username) {
        if (!userExists(username)) {
            return  "The username " + username + " doesn't exist.";
        }

        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                if (artist.deleteArtist()) {
                    artists.remove(artist);
                    return username + " was successfully deleted.";
                } else {
                    return username + " can't be deleted.";
                }
            }
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.deleteUser()) {
                    users.remove(user);
                    return username + " was successfully deleted.";
                } else {
                    return username + " can't be deleted.";
                }
            }
        }
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                if (host.deleteHost()) {
                    hosts.remove(host);
                    return username + " was successfully deleted.";
                } else {
                    return username + " can't be deleted.";
                }
            }
        }

        return "unimplemented";
    }

    /**
     * Removes an event if the event exists
     * @param artistName name of the artist
     * @param eventName name of the event
     * @return success status
     */
    public String removeEvent(final String artistName, final String eventName) {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(artistName)) {
                if (artist.removeEvent(eventName)) {
                    return artistName + " deleted the event successfully.";
                } else {
                    return artistName + " doesn't have an event with the given name.";
                }
            }
        }
        if (!userExists(artistName)) {
            return  "The username " + artistName + " doesn't exist.";
        }
        return artistName + " is not an artist.";
    }

    /**
     * Removes an event if the event exists
     * @param hostName name of the artist
     * @param eventName name of the event
     * @return success status
     */
    public String removeAnnouncement(final String hostName, final String eventName) {
        for (Host host : hosts) {
            if (host.getUsername().equals(hostName)) {
                if (host.removeAnnouncement(eventName)) {
                    return hostName + " has successfully deleted the announcement.";
                } else {
                    return hostName + " has no announcement with the given name.";
                }
            }
        }
        if (!userExists(hostName)) {
            return  "The username " + hostName + " doesn't exist.";
        }
        return hostName + " is not an host.";
    }

    /**
     * Removes a playlist if the playlist is safe to delete
     * @param playlist the playlist
     * @return success status
     */
    public boolean removePlaylist(final Playlist playlist) {
        if (playlist.getNumOfListeners() != 0) {
            return false;
        }
        for (User user : users) {
            user.getFollowedPlaylists().remove(playlist);
        }
        return true;
    }

    /**
     * Removes a podcast if the podcast is safe to delete
     * @param podcastName the podcast
     * @return success status
     */
    public String removePodcast(final String hostName, final String podcastName) {
            for (Host host : hosts) {
                if (host.getUsername().equals(hostName)) {
                    if (host.getPodcasts().stream()
                            .noneMatch(p -> p.getName().equals(podcastName))) {
                        // Checks if there is a podcast with this name
                        return hostName + " doesn't have a podcast with the given name.";
                    }
                    if (host.removePodcast(podcastName)) {
                        for (Podcast podcast : podcasts) {
                            if (podcast.getName().equals(podcastName)) {
                                podcasts.remove(podcast);
                                return hostName + " deleted the podcast successfully.";
                            }
                        }
                    } else {
                        return hostName + " can't delete this podcast.";
                    }
                }
            }
            if (!userExists(hostName)) {
                return  "The username " + hostName + " doesn't exist.";
            }
            return hostName + " is not an host.";
    }

    /**
     * Removes a podcast from the podcast list
     *
     * @param podcast the podcast
     */
    public void removePodcastInstance(final Podcast podcast) {
        if (podcast.getNumOfListeners() != 0) {
            return;
        }
        podcasts.remove(podcast);
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparing(Album::getTotalLikes)
                .reversed().thenComparing(LibraryEntry::getName));

        List<String> topAlbums = sortedAlbums.stream().map(LibraryEntry::getName).toList();
        if (topAlbums.size() > limit) {
            topAlbums = topAlbums.subList(0, limit);
        }
        return topAlbums;
    }

    /**
     * Gets top 5 artists, sorted by the number of likes they have.
     *
     * @return the top 5 artists
     */
    public List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(artists);
        sortedArtists.sort(Comparator.comparing(Artist::getTotalLikes).reversed());

        List<String> topArtists = sortedArtists.stream().map(Artist::getUsername).toList();
        if (topArtists.size() > limit) {
            topArtists = topArtists.subList(0, limit);
        }
        return topArtists;
    }

    /**
     * Payday! Cancel all the premium subscriptions at the end of the test.
     */
    public void massUnsubscribe() {
        users.forEach(User::cancelPremium);
    }

    /**
     * Gets the type of the user
     * @param username the users name
     * @return the class type as a string
     */
    public String getUserType(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "user";
            }
        }

        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return "artist";
            }
        }

        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return "host";
            }
        }

        return "unknown";
    }
}
