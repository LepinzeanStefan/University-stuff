package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Episode;
import app.page_elements.Announcement;
import app.page_elements.Notification;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Host type user, featuring its own page
 */
@Getter
public class Host extends UserType {
    private final List<Podcast> podcasts;
    private final List<Announcement> announcements;
    private static Admin admin;
    private final ArrayList<User> followers;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        followers = new ArrayList<>();
    }

    /**
     * Creates a new podcast and adds it to the podcast list
     * @param commandInput the command given in input
     * @return success status
     */
    public String addPodcast(final CommandInput commandInput) {
        final String name = commandInput.getName();
        final ArrayList<EpisodeInput> episodeInputList = commandInput.getEpisodes();

        ArrayList<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episode : episodeInputList) {
            episodes.add(new Episode(episode.getName(), episode.getDuration(),
                    episode.getDescription(), this.getUsername()));
        }

        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return getUsername() + " has another podcast with the same name.";
            }
        }
        // Use a set to check for duplicates
        Set<Podcast> set = new HashSet<>();
        for (Podcast element : podcasts) {
            if (!set.add(element)) {
                return getUsername() + " has the same episode in this podcast.";
            }
        }

        Podcast podcast = new Podcast(name, getUsername(), episodes);

        podcasts.add(podcast);
        admin.addPodcast(podcast);
        notifyFollowers(new Notification("New Podcast",
                "New Podcast from " + getUsername() + "."));
        return getUsername() + " has added new podcast successfully.";
    }

    /**
     * Creates a new announcement and adds it to the event list
     * @param name name of the announcement
     * @param description description of the announcement
     * @return success status
     */
    public String addAnnouncement(final String name, final String description) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return getUsername() + " has already added an announcement"
                        + "with this name.";
            }
        }

        announcements.add(new Announcement(name, description));
        notifyFollowers(new Notification("New Announcement",
                "New Announcement    from " + getUsername() + "."));
        return getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes an announcement
     * @param eventName name of the event
     * @return true if the announcement was deleted
     */
    public boolean removeAnnouncement(final String eventName) {
        for (Announcement event : announcements) {
            if (event.getName().equals(eventName)) {
                announcements.remove(event);
                return true;
            }
        }
        return false;
    }

    /**
     * Show podcasts in array list format.
     *
     * @return the array list
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }
        return podcastOutputs;
    }

    /**
     * Deletes a podcast if it is safe to delete
     * @param podcastName name of the podcast that needs to be checked
     * @return True if the podcast is safely deleted, false if the host doesn't have the podcast
     * or if it's unsafe to delete
     */
    public boolean removePodcast(final String podcastName) {
        for (Podcast podcast: podcasts) {
            if (podcast.getName().equals(podcastName)) {
                if (podcast.getNumOfListeners() != 0) {
                    return false;
                }
                podcasts.remove(podcast);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes host, if all the podcasts are safe to delete and no one is currently on his page
     * Practically only deletes its podcasts, the Admin will remove the Artist instance
     * @return boolean representing either success or failure
     */
    public boolean deleteHost() {
        for (Podcast podcast : podcasts) {
            if (podcast.getNumOfListeners() != 0) {
                return false;
            }
        }
        if (admin.isPageSelected(getUsername())) {
            return false;
        }

        for (Podcast podcast : podcasts) {
            admin.removePodcastInstance(podcast);
        }
        podcasts.clear();
        return true;
    }

    /**
     * Adds a new user to the followers list, or removes them if they are already subscribed
     * @param user the user that needs to be added
     * @return subscribe message
     */
    public String subscribe(final User user) {
        if (followers.contains(user)) {
            followers.remove(user);
            return user.getUsername() + " unsubscribed from " + getUsername() + " successfully.";
        } else {
            followers.add(user);
            return user.getUsername() + " subscribed to " + getUsername() + " successfully.";
        }
    }

    /**
     * Notify all users of a new addition
     * @param notification the notification message
     */
    public void notifyFollowers(Notification notification) {
        followers.forEach(user -> user.getInbox().addNotification(notification));
    }
}
