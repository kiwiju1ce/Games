package code.util;

import java.io.BufferedWriter;
import java.io.IOException;

public class BufferedWriterWrapper {
    private BufferedWriter writer;

    BufferedWriterWrapper(BufferedWriter writer) {
        this.writer = writer;
    }

    public void printSingleLine(String value) {
        try {
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printMultipleLine(String[] values) {
        try {
            for (String value : values) {
                writer.write(value);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
