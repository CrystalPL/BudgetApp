package pl.crystalek.budgetapp.user;

import javafx.scene.image.Image;
import pl.crystalek.budgetapp.user.sex.UserSex;
import pl.crystalek.budgetapp.util.ThreadUtil;

import java.io.File;
import java.util.Set;
import java.util.function.Consumer;

public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void fetchUsers(final Consumer<Set<User>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(userRepository.loadUsers())));
    }

    public void fetchUsersDTO(final Consumer<Set<UserDTO>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(userRepository.loadUsersDTO())));
    }

    public void updateUser(final User user) {
        ThreadUtil.runAsync(() -> userRepository.updateUser(user));
    }

    public void createUser(final String userName, final UserSex sex, final File iconFile, final Consumer<Boolean> consumer) {
        final Image icon = new Image(iconFile.toURI().toString());
        ThreadUtil.runAsync(() -> {
            final boolean userExist = userRepository.userExist(userName, icon);
            ThreadUtil.runInGUIThread(() -> consumer.accept(!userExist));

            if (userExist) {
                return;
            }

            userRepository.createUser(userName, sex, icon);
        });
    }
}
