package testingmachine_backend.process.Controller;

import lombok.extern.slf4j.Slf4j;
import testingmachine_backend.meta.Controller.ProcessMetaData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProcessDatabaseUtils {

    private static final String DB_URL = "jdbc:oracle:thin:@172.169.88.80:1521:dev";
    private static final String DB_USERNAME = "IA_USER";
    private static final String DB_PASSWORD = "Interactive123$";

    public static List<ProcessMetaData> getProcessMetaDataList() {
        List<ProcessMetaData> processList = new ArrayList<>();
        String query = """
            SELECT t4.meta_data_name AS system_name, 
                   t2.meta_data_id, 
                   t2.meta_data_code, 
                   t2.meta_data_name 
            FROM test_case t1
            INNER JOIN meta_data t2 ON t2.meta_data_id = t1.PROCESS_META_DATA_ID
            LEFT JOIN test_case_system_map t3 ON t3.test_case_id = t1.id
            LEFT JOIN meta_data t4 ON t4.meta_data_id = t3.system_id AND t4.meta_type_id = 200101010000025
            WHERE t1.TEST_MODE = 3 AND t1.is_active = 1 AND t2.meta_data_id in (17319935152441)
            """;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String id = resultSet.getString("meta_data_id");
                String systemName = resultSet.getString("system_name");
                String code = resultSet.getString("meta_data_code");
                String name = resultSet.getString("meta_data_name");

                processList.add(new ProcessMetaData(id, systemName, code, name));
            }
        } catch (Exception e) {
            log.error("Database error: ", e);
        }

        return processList;
    }
}




