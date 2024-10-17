package app.page_elements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
    private final String name;
    private final String description;


    public Notification(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
