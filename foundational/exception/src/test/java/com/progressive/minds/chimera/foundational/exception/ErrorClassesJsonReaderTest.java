package com.progressive.minds.chimera.foundational.exception;

class ErrorClassesJsonReaderTest {

 /*   private ErrorClassesJsonReader errorClassesJsonReader;

    @BeforeEach
    void setUp() {
        errorClassesJsonReader = new ErrorClassesJsonReader(
                Collections.singletonList(
                        this.getClass().getClassLoader().getResource("test_errors.json")));
    }

    @Test
    void getErrorMessage_withValidErrorClassAndParameters_returnsFormattedMessage() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        String result = errorClassesJsonReader.getErrorMessage("DataSourceException", params);
        assertEquals("Error Generated Due to Framework -DataSourceException Exception", result);
    }

    @Test
    void getErrorMessage_withInvalidErrorClass_throwsException() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        assertThrows(Exception.class, () -> errorClassesJsonReader.getErrorMessage("invalidErrorClass", params));
    }

    @Test
    void getMessageTemplate_withValidErrorClass_returnsTemplate() {
        String result = errorClassesJsonReader.getMessageTemplate("DataSourceException");
        assertEquals("Expected message template", result);
    }

    @Test
    void getMessageTemplate_withInvalidErrorClass_throwsException() {
        assertThrows(Exception.class, () -> errorClassesJsonReader.getMessageTemplate("invalidErrorClass"));
    }

    @Test
    void getSqlState_withValidErrorClass_returnsSqlState() {
        String result = errorClassesJsonReader.getSqlState("DataSourceException");
        assertEquals("42704", result);
    }

    @Test
    void getSqlState_withInvalidErrorClass_returnsNull() {
        String result = errorClassesJsonReader.getSqlState("invalidErrorClass");
        assertNull(result);
    }*/
}