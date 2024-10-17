package code.util;

import code.exception.ExitFlagException;

import java.io.BufferedReader;
import java.io.IOException;

import static code.constant.BowlingConstant.EXIT_FLAG;

public class BufferedReaderWrapper {
    private BufferedReader reader;

    BufferedReaderWrapper(BufferedReader reader) {
        this.reader = reader;
    }

    public String readLine() {
        try {
            String value = reader.readLine();
            if (isExitFlag(value)) {
                throw new ExitFlagException();
            }
            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isExitFlag(String value) {
        return value.equals(EXIT_FLAG);
    }
}
