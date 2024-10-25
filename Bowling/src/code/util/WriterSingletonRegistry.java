package code.util;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class WriterSingletonRegistry {
    private WriterSingletonRegistry() {
    }

    public static BufferedWriterWrapper getInstance() {
        return WriterSingletonHolder.instance;
    }

    private static class WriterSingletonHolder {
        private static final BufferedWriterWrapper instance =
                new BufferedWriterWrapper(
                        new BufferedWriter(new OutputStreamWriter(System.out))
                );
    }
}
