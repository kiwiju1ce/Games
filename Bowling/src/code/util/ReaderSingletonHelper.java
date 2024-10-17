package code.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ReaderSingletonHelper {
    private ReaderSingletonHelper() {
    }

    public static BufferedReaderWrapper getInstance() {
        return ReaderInstanceHolder.instance;
    }

    private static class ReaderInstanceHolder {
        private static final BufferedReaderWrapper instance =
                new BufferedReaderWrapper(
                        new BufferedReader(new InputStreamReader(System.in))
                );
    }
}
