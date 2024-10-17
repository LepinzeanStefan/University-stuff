package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import app.user.Artist;
import app.user.Host;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;


/**
 * The type Search bar.
 */
public final class SearchBar {
    private static Admin admin;
    private List<LibraryEntry> results;
    private List<Artist> artistResults;
    private List<Host> hostResults;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;
    @Getter
    private LibraryEntry lastSelected;
    @Getter
    private Artist lastArtistSelected;
    @Getter
    private Host lastHostSelected;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.artistResults = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastArtistSelected = null;
        lastHostSelected = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<String> search(final Filters filters, final String type) {
        List<LibraryEntry> entries;
        List<Artist> artists = null;
        List<Host> hosts = null;

        switch (type) {
            case "song":
                entries = new ArrayList<>(admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(admin.getAlbums());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getFollowers());
                }

                break;
            case "artist":
                artists = new ArrayList<>(admin.getArtists());

                if (filters.getName() != null) {
                    artists = filterArtistByName(artists, filters.getName());
                }
            case "host":
                hosts = new ArrayList<>(admin.getHosts());

                if (filters.getName() != null) {
                   hosts = filterHostByName(hosts, filters.getName());
                }
            default:
                entries = new ArrayList<>();
        }

        if (type.equals("artist")) {
            while (artists.size() > MAX_RESULTS) {
                artists.remove(artists.size() - 1);
            }
            this.results = new ArrayList<>();
            this.hostResults = new ArrayList<>();
            this.artistResults = artists;
            this.lastSearchType = type;
            return this.artistResults.stream().map(Artist::getUsername).toList();
        } else if (type.equals("host")) {
            while (hosts.size() > MAX_RESULTS) {
                hosts.remove(hosts.size() - 1);
            }
            this.results = new ArrayList<>();
            this.hostResults = hosts;
            this.artistResults = new ArrayList<>();
            this.lastSearchType = type;
            return this.hostResults.stream().map(Host::getUsername).toList();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.artistResults = new ArrayList<>();
        this.hostResults = new ArrayList<>();
        this.lastSearchType = type;
        return this.results.stream().map(LibraryEntry::getName).toList();
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

    /**
     * Select artist.
     *
     * @param itemNumber the item number
     * @return the artist
     */
    public Artist selectArtist(final Integer itemNumber) {
        if (this.artistResults.size() < itemNumber) {
            artistResults.clear();

            return null;
        } else {
            lastArtistSelected = this.artistResults.get(itemNumber - 1);
            results.clear();

            return lastArtistSelected;
        }
    }

    /**
     * Select host.
     *
     * @param itemNumber the item number
     * @return the host
     */
    public Host selectHost(final Integer itemNumber) {
        if (this.hostResults.size() < itemNumber) {
            hostResults.clear();

            return null;
        } else {
            lastHostSelected = this.hostResults.get(itemNumber - 1);
            results.clear();

            return lastHostSelected;
        }
    }
}
