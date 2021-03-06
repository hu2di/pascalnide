package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.tokens.Token;

import com.duy.pascal.backend.linenumber.LineInfo;

public class BeginEndToken extends GrouperToken {

    public BeginEndToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toCode() {
        StringBuilder builder = new StringBuilder("begin ");
        if (next != null) {
            builder.append(next);
        }
        for (Token t : this.queue) {
            builder.append(t).append(' ');
        }
        builder.append("end ");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "begin";
    }

    @Override
    protected String getClosingText() {
        return "end";
    }
}
