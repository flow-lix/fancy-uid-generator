package learn.platform.bootstrap.service;

import org.junit.Test;

public class SnowflakeServiceTest {

    @Test
    public void testBit() {
        System.out.println(Integer.toBinaryString(-1));

        System.out.println(Integer.toBinaryString(-1 << 10));
        System.out.println(~5);
        System.out.println((1 << 10) - 1);
        System.out.println(~(1 << 10L));
    }
}
