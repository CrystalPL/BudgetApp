package pl.crystalek.budgetapp.user.sex;

import java.util.Arrays;

public enum UserSex {
    MALE("Mężczyzna"),
    FEMALE("Kobieta");

    private final String polishTranslation;

    UserSex(final String polishTranslation) {
        this.polishTranslation = polishTranslation;
    }

    public static String[] getPolishTranslations() {
        return Arrays.stream(UserSex.values())
                .map(UserSex::getPolishTranslation)
                .toArray(String[]::new);
    }

    public static UserSex getSexByPolishName(final String polishName) {
        return Arrays.stream(UserSex.values())
                .filter(userSex -> userSex.getPolishTranslation().equalsIgnoreCase(polishName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono płci o polskim tłumaczeniu: " + polishName));
    }

    public String getPolishTranslation() {
        return polishTranslation;
    }
}
