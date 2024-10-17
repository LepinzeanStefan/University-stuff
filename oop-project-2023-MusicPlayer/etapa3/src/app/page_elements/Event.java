package app.page_elements;

import lombok.Getter;

/**
 * Class used to display an artist event, works like the Announcement but also has a date
 */
@Getter
public final class Event extends Announcement {
    private final String date;

    public Event(final String name, final String description, final String date) {
        super(name, description);
        this.date = date;
    }

    @Override
    public String display() {
        return super.getName() + " - " + date + ":\n\t" + super.getDescription();
    }
}
