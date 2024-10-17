package app.user;

public class GenericUser {
    public enum Type {
        USER, ARTIST, HOST
    }

    /**
     * Creates a generic user with the given type
     * @param userType the type of user
     * @param username the name of the user
     * @param age the age of the user
     * @param city the city of the user
     * @return the user instance with its chosen userType
     */
    public static UserType createUser(final Type userType, final String username,
                                      final int age, final String city) {
        return switch (userType) {
            case USER -> new User(username, age, city);
            case ARTIST -> new Artist(username, age, city);
            case HOST -> new Host(username, age, city);
        };
    }
}
