package com.progressive.minds.chimera.core.databaseOps.repository;

import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.dataSources;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class dataSourcesRepositoryTest {

private dataSourcesRepository dataSourcesRepository;
private DataSource dataSource;

@BeforeEach
void setUp() throws Exception {
    System.setProperty("CHIMERA_EXE_ENV", "dev");
    dataSource = DataSourceConfig.getDataSource();
    dataSourcesRepository = new dataSourcesRepository();
}

//@AfterEach
//void tearDown() throws Exception {
//    // Clear the database after each test
//    RunScript.execute(dataSource.getConnection(), new FileReader("src/test/resources/cleanup.sql"));
//}

//@Test
//void testSaveUser_AddsUserToDatabase() {
//    // Arrange
//    User user = new User(0, "John Doe", "john.doe@example.com");
//
//    // Act
//    userRepository.saveUser(user);
//
//    // Assert
//    List<User> users = userRepository.getAllUsers();
//    assertEquals(1, users.size());
//    assertEquals("John Doe", users.get(0).getName());
//}

//@Test
//void testGetAllDataSources_ReturnsEmptyList_WhenNoDataSources() {
//    // Act
//    List<dataSources> dataSources = dataSourcesRepository.getAllDataSources();
//    dataSources.forEach(ds -> System.out.println("dataSourceType : " + ds.getDataSourceType() + " data_source_sub_type : " + ds.getDataSourceSubType()));
//
//    // Assert
//    assertTrue(dataSources.isEmpty());
//}

    @Test
    void testGetAllDataSources() {
        // Act
        List<dataSources> dataSources = dataSourcesRepository.getAllDataSources();
        dataSources.forEach(
                ds -> System.out.println("dataSourceType : " + ds.getDataSourceType() +
                        ", data_source_sub_type : " + ds.getDataSourceSubType() +
                        ", description : " + ds.getDescription() +
                        ", read_defaults : " + ds.getReadDefaults() +
                        ", write_defaults : " + ds.getWriteDefaults() +
                        ", created_by : " + ds.getCreatedBy() +
                        ", created_timestamp : " + ds.getCreatedTimestamp() +
                        ", updated_by : " + ds.getUpdatedBy() +
                        ", updated_timestamp : " + ds.getUpdatedTimestamp() +
                        ", active_flag : " + ds.getActiveFlag()));
        System.out.println("Size : " + dataSources.size());
        // Assert
        assertEquals(14, dataSources.size());
    }

//@Test
//void testGetAllUsers_ReturnsAllUsers() {
//    // Arrange
//    userRepository.saveUser(new User(0, "John Doe", "john.doe@example.com"));
//    userRepository.saveUser(new User(0, "Jane Doe", "jane.doe@example.com"));
//
//    // Act
//    List<User> users = userRepository.getAllUsers();
//    users.forEach(user -> System.out.println("Users Name " + user.getName()));
//
//    // Assert
//    assertEquals(2, users.size());
//}
}


