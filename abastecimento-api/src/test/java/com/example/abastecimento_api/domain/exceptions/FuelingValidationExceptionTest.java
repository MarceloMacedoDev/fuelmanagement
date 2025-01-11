package com.example.abastecimento_api.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FuelingValidationExceptionTest {
    @Test
    void testGetMessage() {
        String expectedMessage = "Fueling validation failed!";
        FuelingValidationException exception = new FuelingValidationException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testGetMessage_nullMessage() {
        FuelingValidationException exception = new FuelingValidationException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void testGetMessage_emptyMessage() {
        FuelingValidationException exception = new FuelingValidationException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void testGetMessage_longMessage() {
        String longMessage = "This is a very long message to test the getMessage() method of the FuelingValidationException class.  It should handle messages of various lengths without any issues.";
        FuelingValidationException exception = new FuelingValidationException(longMessage);
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    void testConstructor_nullMessage() {
        FuelingValidationException exception = new FuelingValidationException(null);
        assertNull(exception.getMessage());
        assertDoesNotThrow(() -> new FuelingValidationException(null));
    }

    @Test
    void testConstructor_emptyMessage() {
        FuelingValidationException exception = new FuelingValidationException("");
        assertEquals("", exception.getMessage());
        assertDoesNotThrow(() -> new FuelingValidationException(""));

    }

}