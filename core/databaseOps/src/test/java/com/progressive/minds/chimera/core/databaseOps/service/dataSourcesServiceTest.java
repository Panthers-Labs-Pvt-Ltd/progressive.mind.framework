package com.progressive.minds.chimera.core.databaseOps.service;

import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;
import com.progressive.minds.chimera.core.databaseOps.model.dataSources;
import com.progressive.minds.chimera.core.databaseOps.repository.dataSourcesRepository;
import com.progressive.minds.chimera.core.databaseOps.service.dataSourcesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class dataSourcesServiceTest {
    private dataSourcesRepository dataSourcesRepository;
    private dataSourcesService dataSourcesService;

    @BeforeEach
    void setUp() {
        System.setProperty("CHIMERA_EXE_ENV", "dev");

        dataSourcesRepository = mock(dataSourcesRepository.class); // Mock the repository
        dataSourcesService = new dataSourcesService(dataSourcesRepository);
    }

    //TODO: Create unit Tests

}