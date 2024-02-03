package pl.crystalek.budgetapp.user.sex;

import javafx.util.StringConverter;

public final class SexConverter extends StringConverter<UserSex> {

    public static final SexConverter INSTANCE = new SexConverter();

    private SexConverter() {
    }

    @Override
    public String toString(final UserSex sex) {
        return sex.getPolishTranslation();
    }

    @Override
    public UserSex fromString(final String sex) {
        return UserSex.valueOf(sex);
    }
}
