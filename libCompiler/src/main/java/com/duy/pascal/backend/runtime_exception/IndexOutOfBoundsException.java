package com.duy.pascal.backend.runtime_exception;

import com.duy.pascal.backend.linenumber.LineInfo;

public class IndexOutOfBoundsException extends RuntimePascalException {
    String message;

    public IndexOutOfBoundsException(LineInfo line, int index, int min,
                                     int max) {
        super(line);
        this.message = "Index inType of bounds: " + getcause(index, min, max);
    }

    @Override
    public String getMessage() {
        return message;
    }

    private String getcause(int index, int min, int max) {
        if (index < min) {
            return index + " is less than the minimum index of " + min;
        } else if (max == -1 && min == 0) {
            return "variable length array has not been initialized yet";
        } else {
            return index + " is greater than the maximum index of " + max;
        }
    }
}
