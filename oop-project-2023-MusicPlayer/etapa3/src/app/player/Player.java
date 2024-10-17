package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.monetization.MonetizationStatistics;
import app.utils.Enums;
import app.wrapped.StatisticsWrapped;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Player.
 */
public final class Player {
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    private PlayerSource source;
    @Getter
    private String type;
    private String username;
    private final int skipTime = 90;
    private final ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();
    @Getter
    private final MonetizationCalculator monetizationCalculator;
    private int adValue;

    /**
     * Instantiates a new Player.
     */
    public Player(final String username) {
        this.username = username;
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
        monetizationCalculator = new MonetizationCalculator();
    }

    /**
     * Stop.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
            if (source != null) {
                source.getAudioCollection().removeListener();
            }
        }
        if ("playlist".equals(this.type) || "album".equals(this.type)) {
            if (source != null) {
                source.getAudioCollection().removeListener();
            }
        }
        if ("song".equals(this.type)) {
            if (source != null) {
                source.getAudioFile().removeInteraction();
            }
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark =
                    new PodcastBookmark(source.getAudioCollection().getName(),
                                        source.getIndex(),
                                        source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    /**
     * Create source player source.
     *
     * @param type      the type
     * @param entry     the entry
     * @param bookmarks the bookmarks
     * @return the player source
     */
    public static PlayerSource createSource(final String type,
                                            final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        } else if ("album".equals(type)) {
            // Since an album should behave similar to a playlist, just treat it like one
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        }

        return null;
    }

    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }

    /**
     * Sets source.
     *
     * @param entry the entry
     * @param sourceType  the sourceType
     */
    public void setSource(final LibraryEntry entry, final String sourceType) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = sourceType;
        this.source = createSource(sourceType, entry, bookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
        if (this.source != null) {
            StatisticsWrapped.updateStatistics(username, source.getAudioFile());
            MonetizationStatistics.addArtistIfAbsent(source.getAudioFile());
            if (!Objects.equals(this.type, "podcast")) {
                monetizationCalculator.addListenedSong((Song) source.getAudioFile());
            }
        }
    }

    /**
     * Pause.
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Shuffle.
     *
     * @param seed the seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

    /**
     * Repeat enums . repeat mode.
     *
     * @return the enums . repeat mode
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }

    /**
     * Simulate player.
     *
     * @param time the time
     */
    public void simulatePlayer(final int time) {
        int elapsedTime = time;
        if (!paused) {
            while (elapsedTime >= source.getDuration()) {
                elapsedTime -= source.getDuration();
                next();
                if (source != null && source.getAudioFile().getName().equals("Ad Break")) {
                    monetizationCalculator.payWithAd(adValue);
                    adValue = 0;
                }
                if (paused) {
                    break;
                }
            }
            if (!paused) {
                source.skip(-elapsedTime);
            }
        }
    }

    /**
     * Next.
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (!paused && !source.getAudioFile().getName().equals("Ad Break")) {
            StatisticsWrapped.updateStatistics(username, source.getAudioFile());
            MonetizationStatistics.addArtistIfAbsent(source.getAudioFile());
            if (!Objects.equals(this.type, "podcast")) {
                monetizationCalculator.addListenedSong((Song) source.getAudioFile());
            }
        }
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE
                && !source.getAudioFile().getName().equals("Ad Break")) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }

    /**
     * Prev.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        if (!paused && !source.getAudioFile().getName().equals("Ad Break")) {
            StatisticsWrapped.updateStatistics(username, source.getAudioFile());
            MonetizationStatistics.addArtistIfAbsent(source.getAudioFile());
            if (!Objects.equals(this.type, "podcast")) {
                monetizationCalculator.addListenedSong((Song) source.getAudioFile());
            }
        }
        paused = false;
    }

    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }

    /**
     * Skip next.
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-skipTime);
        }
    }

    /**
     * Skip prev.
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(skipTime);
        }
    }

    /**
     * Gets current audio file.
     *
     * @return the current audio file
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }

    /**
     * Gets paused.
     *
     * @return the paused
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Gets shuffle.
     *
     * @return the shuffle
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Gets stats.
     *
     * @return the stats
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }

    /**
     * Schedules an advertisement
     * @param value the number of credits given by the ad
     * @param advertisement the ad that needs to be played
     */
    public void adBreak(final int value, final Song advertisement) {
        source.scheduleAd(advertisement);
        adValue = value;
    }

    /**
     *
     * @return remaining time of the source
     */
    public int getRemainingTime() {
        return source.getRemainedDuration();
    }
}
