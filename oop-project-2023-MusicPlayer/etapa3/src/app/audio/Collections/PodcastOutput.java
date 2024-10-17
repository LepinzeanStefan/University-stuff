package app.audio.Collections;

import app.audio.Files.Episode;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class PodcastOutput {
    private final String name;
    private final ArrayList<String> episodes;

    public PodcastOutput(final Podcast podcast) {
        this.name = podcast.getName();
        episodes = new ArrayList<>();
        for (Episode episode : podcast.getEpisodes()) {
            episodes.add(episode.getName());
        }
    }
}
