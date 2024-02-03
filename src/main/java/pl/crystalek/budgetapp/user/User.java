package pl.crystalek.budgetapp.user;

import javafx.scene.image.Image;
import pl.crystalek.budgetapp.user.sex.UserSex;

public class User {
    private final int id;
    private String name;
    private UserSex userSex;
    private Image icon;

    public User(final String name, final UserSex userSex, final Image icon, final int id) {
        this.name = name;
        this.userSex = userSex;
        this.icon = icon;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(final Image icon) {
        this.icon = icon;
    }

    public UserSex getSex() {
        return userSex;
    }

    public void setSex(final UserSex userSex) {
        this.userSex = userSex;
    }

    public int getId() {
        return id;
    }
}
