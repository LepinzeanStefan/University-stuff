package app.page_elements;

import lombok.Getter;

/**
 * Class used to display a host announcement on the host's page
 */
@Getter
public class Announcement {
    private final String name;
    private final String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Displays a string that is appropriate for printCurrentPage
     * @return String containing the fields of the announcement
     */
    public String display() {
        return name + ":\n\t" + description + "\n";
    }
}
