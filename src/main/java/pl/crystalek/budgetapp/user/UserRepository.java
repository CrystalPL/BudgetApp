package pl.crystalek.budgetapp.user;

import javafx.scene.image.Image;
import pl.crystalek.budgetapp.user.sex.UserSex;

import java.util.Set;

public interface UserRepository {

    void createUser(final String userName, final UserSex sex, final Image icon);

    void updateUser(final User user);

    Set<User> loadUsers();

    Set<UserDTO> loadUsersDTO();

    boolean userExist(final String userName, final Image icon);

    void createTable();
}
