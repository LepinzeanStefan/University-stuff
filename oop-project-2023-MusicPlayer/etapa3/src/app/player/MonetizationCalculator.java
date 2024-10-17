package app.player;

import app.audio.Files.Song;
import app.monetization.MonetizationStatistics;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class MonetizationCalculator {
    @Getter
    @Setter
    private boolean isUserPremium;
    private final ArrayList<Song> premiumDebts;
    private final ArrayList<Song> brokieDebts;
    private static final double PREMIUM_CREDITS = 1000000;

    public MonetizationCalculator() {
        isUserPremium = false;
        premiumDebts = new ArrayList<>();
        brokieDebts = new ArrayList<>();
    }

    /**
     * Adds the new song the user is listening to the waiting list of debts to be paid off
     * @param song the new song that needs to be paid off
     */
    public void addListenedSong(final Song song) {
        if (isUserPremium) {
            premiumDebts.add(song);
        } else {
            brokieDebts.add(song);
        }
    }

    /**
     * Return to mediocrity and cancel the premium subscription, also paying all the gathered debts
     * while being a premium user.
     */
    public void cancelPremium() {
        if (!isUserPremium || premiumDebts.isEmpty()) {
            return;
        }
        final double amount = PREMIUM_CREDITS / premiumDebts.size();
        isUserPremium = false;

        premiumDebts.forEach(song -> MonetizationStatistics.addSongRevenue(amount, song));
        premiumDebts.clear();
    }

    /**
     * The user just got an ad roll. Pay all his debts with the credits from the add.
     * @param amount the amount of credits gotten from the ad.
     */
    public void payWithAd(final double amount) {
        if (isUserPremium || brokieDebts.isEmpty()) {
            return;
        }
        final double val = amount / brokieDebts.size();

        brokieDebts.forEach(song -> MonetizationStatistics.addSongRevenue(val, song));
        brokieDebts.clear();
    }
}
