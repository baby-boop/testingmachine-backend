package testingmachine_backend.projects.meta.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    /**
     * DEV CONNECTION

    private static final String DB_URL = "jdbc:oracle:thin:@172.169.88.66:1521:dev";
    private static final String DB_USERNAME = "IA_USER";
    private static final String DB_PASSWORD = "Interactive123$";
     */

    private static final String DB_URL = "jdbc:oracle:thin:@172.169.88.80:1521:dev";
    private static final String DB_USERNAME = "IA_USER";
    private static final String DB_PASSWORD = "Interactive123$";

    public static List<String> getIdsFromMetaList() {
        List<String> ids = new ArrayList<>();

        String query = "SELECT META_DATA_ID AS ID FROM META_DATA where rownum <= 10";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                ids.add(resultSet.getString("id"));
                System.out.println(resultSet.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }
}
