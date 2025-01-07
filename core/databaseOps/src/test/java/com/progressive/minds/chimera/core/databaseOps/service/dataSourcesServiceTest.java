package com.progressive.minds.chimera.core.databaseOps.service;

import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;
import com.progressive.minds.chimera.core.databaseOps.service.metadata.dataSourcesService;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class dataSourcesServiceTest {
    private dataSourcesRepository dataSourcesRepository;
    private com.progressive.minds.chimera.core.databaseOps.service.metadata.dataSourcesService dataSourcesService;

    @BeforeEach
    void setUp() {
        System.setProperty("CHIMERA_EXE_ENV", "dev");

        dataSourcesRepository = mock(dataSourcesRepository.class); // Mock the repository
        dataSourcesService = new dataSourcesService(dataSourcesRepository);
    }

    //TODO: Create unit Tests

}