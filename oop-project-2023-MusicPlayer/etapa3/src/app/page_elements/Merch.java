package app.page_elements;

import lombok.Getter;

/**
 * Class to display an artis's merchandise
 */
@Getter
public class Merch {
    private final String name;
    private final String description;
    private final int price;

    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Displays a string that is appropriate for printCurrentPage
     * @return String containing the fields of the merch
     */
    public String display() {
        return name + " - " + price + ":\n\t" + description;
    }
}
