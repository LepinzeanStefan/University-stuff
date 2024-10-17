package app.user;

import lombok.Getter;

import java.util.Objects;

/**
 * Generic user type that all the other users inherit
 */
@Getter
public abstract class UserType {
    private final String username;
    private final int age;
    private final String city;

    public UserType(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    /**
     * Checks if two users are equal by comparing their names
     * @param o the object witch is being compared two
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserType userType = (UserType) o;
        return Objects.equals(getUsername(), userType.getUsername());
    }

    /**
     * Creates a hashcode from the users name
     * @return the hashcode of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
