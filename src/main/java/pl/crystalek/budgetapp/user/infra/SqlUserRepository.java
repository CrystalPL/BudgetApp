package pl.crystalek.budgetapp.user.infra;

import com.zaxxer.hikari.HikariDataSource;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.storage.function.SQLFunction;
import pl.crystalek.budgetapp.storage.util.SqlUtil;
import pl.crystalek.budgetapp.user.User;
import pl.crystalek.budgetapp.user.UserDTO;
import pl.crystalek.budgetapp.user.UserRepository;
import pl.crystalek.budgetapp.user.sex.UserSex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class SqlUserRepository extends SqlUtil implements UserRepository {
    String userExist;
    String createUser;
    String updateUser;
    String getUsers;
    String getUsersDto;
    String usersTable;

    public SqlUserRepository(final HikariDataSource database, final String userExist, final String createUser, final String updateUser,
                             final String getUsers, final String getUsersDto, final String usersTable) {
        super(database);

        this.userExist = userExist;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.getUsers = getUsers;
        this.getUsersDto = getUsersDto;
        this.usersTable = usersTable;

        createTable();
    }

    public static User loadUser(final ResultSet resultSet) throws SQLException {
        final int id = resultSet.getInt("id");
        final String userName = resultSet.getString("name");
        final UserSex sex = UserSex.valueOf(resultSet.getString("sex"));
        final Image image;
        try (
                final InputStream icon = resultSet.getBinaryStream("icon")
        ) {
            final BufferedImage read = ImageIO.read(icon);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }

        return new User(userName, sex, image, id);
    }

    @Override
    @SneakyThrows
    public void createUser(final String userName, final UserSex sex, final Image icon) {
        final InputStream imageInputStream = getImageInputStream(icon);
//        final String s = Base64.getEncoder().encodeToString(imageInputStream.readAllBytes());
//        System.out.println(s.length());
        executeUpdateAndOpenConnection(createUser, userName, imageInputStream, sex.name());
    }

    @Override
    public void updateUser(final User user) {
        final InputStream imageInputStream = getImageInputStream(user.getIcon());
        executeUpdateAndOpenConnection(updateUser, user.getName(), imageInputStream, user.getSex().name(), user.getId());
    }

    @Override
    public Set<User> loadUsers() {
        final SQLFunction<ResultSet, Set<User>> loadUsersFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashSet<>();
            }

            final Set<User> users = new HashSet<>();
            do {
                final User user = loadUser(resultSet); //TODO REMOVE STATIC

                users.add(user);
            } while (resultSet.next());

            return users;
        };

        return executeQueryAndOpenConnection(getUsers, loadUsersFunction);
    }

    @Override
    public Set<UserDTO> loadUsersDTO() {
        final SQLFunction<ResultSet, Set<UserDTO>> loadUsersFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashSet<>();
            }

            final Set<UserDTO> users = new HashSet<>();
            do {
                final String name = resultSet.getString("name");
                final int id = resultSet.getInt("id");

                users.add(new UserDTO(name, id));
            } while (resultSet.next());

            return users;
        };

        return executeQueryAndOpenConnection(getUsersDto, loadUsersFunction);
    }

    @Override
    public boolean userExist(final String userName, final Image icon) {
        final SQLFunction<ResultSet, Boolean> userExistFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return false;
            }

            do {
                final String name = resultSet.getString("name");
                if (userName.equalsIgnoreCase(name)) {
                    return true;
                }

                try {
                    final BufferedImage read = ImageIO.read(resultSet.getBinaryStream("icon"));
                    final Image image = SwingFXUtils.toFXImage(read, null);

                    if (isImageEqual(image, icon)) {
                        return true;
                    }
                } catch (final IOException exception) {
                    throw new RuntimeException(exception);
                }

            } while (resultSet.next());

            return false;
        };

        return executeQueryAndOpenConnection(userExist, userExistFunction);
    }

    @Override
    public void createTable() {
        executeUpdateAndOpenConnection(usersTable);
    }

    private boolean isImageEqual(final Image firstImage, final Image secondImage) {
        if (firstImage.getWidth() != secondImage.getWidth() && firstImage.getHeight() != secondImage.getHeight()) {
            return false;
        }

        for (int x = 0; x < firstImage.getWidth(); x++) {
            for (int y = 0; y < firstImage.getHeight(); y++) {
                int firstArgb = firstImage.getPixelReader().getArgb(x, y);
                int secondArgb = secondImage.getPixelReader().getArgb(x, y);

                if (firstArgb != secondArgb) {
                    return false;
                }
            }
        }

        return true;
    }

    private InputStream getImageInputStream(final Image icon) {
        final BufferedImage bufferedImage = SwingFXUtils.fromFXImage(icon, null);
        try (
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            ImageIO.write(bufferedImage, "png", outputStream);
            byte[] bytes = outputStream.toByteArray();

            try (
                    final InputStream inputStream = new ByteArrayInputStream(bytes)
            ) {
                return inputStream;
            }

        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
