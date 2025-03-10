package com.progressive.minds.chimera.common.kvstore;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayWrappersTest {

    @Test
    public void testGenericArrayKey() {
        byte[] b1 = new byte[] { 0x01, 0x02, 0x03 };
        byte[] b2 = new byte[] { 0x01, 0x02 };
        int[] i1 = new int[] { 1, 2, 3 };
        int[] i2 = new int[] { 1, 2 };
        String[] s1 = new String[] { "1", "2", "3" };
        String[] s2 = new String[] { "1", "2" };

        assertEquals(ArrayWrappers.forArray(b1), ArrayWrappers.forArray(b1));
        assertNotEquals(ArrayWrappers.forArray(b1), ArrayWrappers.forArray(b2));
        assertNotEquals(ArrayWrappers.forArray(b1), ArrayWrappers.forArray(i1));
        assertNotEquals(ArrayWrappers.forArray(b1), ArrayWrappers.forArray(s1));

        assertEquals(ArrayWrappers.forArray(i1), ArrayWrappers.forArray(i1));
        assertNotEquals(ArrayWrappers.forArray(i1), ArrayWrappers.forArray(i2));
        assertNotEquals(ArrayWrappers.forArray(i1), ArrayWrappers.forArray(b1));
        assertNotEquals(ArrayWrappers.forArray(i1), ArrayWrappers.forArray(s1));

        assertEquals(ArrayWrappers.forArray(s1), ArrayWrappers.forArray(s1));
        assertNotEquals(ArrayWrappers.forArray(s1), ArrayWrappers.forArray(s2));
        assertNotEquals(ArrayWrappers.forArray(s1), ArrayWrappers.forArray(b1));
        assertNotEquals(ArrayWrappers.forArray(s1), ArrayWrappers.forArray(i1));

        assertEquals(0, ArrayWrappers.forArray(b1).compareTo(ArrayWrappers.forArray(b1)));
        assertTrue(ArrayWrappers.forArray(b1).compareTo(ArrayWrappers.forArray(b2)) > 0);

        assertEquals(0, ArrayWrappers.forArray(i1).compareTo(ArrayWrappers.forArray(i1)));
        assertTrue(ArrayWrappers.forArray(i1).compareTo(ArrayWrappers.forArray(i2)) > 0);

        assertEquals(0, ArrayWrappers.forArray(s1).compareTo(ArrayWrappers.forArray(s1)));
        assertTrue(ArrayWrappers.forArray(s1).compareTo(ArrayWrappers.forArray(s2)) > 0);
    }

}
