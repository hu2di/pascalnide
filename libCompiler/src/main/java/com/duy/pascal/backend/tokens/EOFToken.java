package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException.Type;
import com.duy.pascal.backend.tokens.closing.ClosingToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;

public class EOFToken extends ClosingToken {
    public EOFToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
//        return Character.valueOf((char) 3).toString();
        return "";
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof ParenthesizedToken) {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_PARENTHESES);
        } else if (t instanceof BeginEndToken) {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_BEGIN_END);
        } else if (t instanceof BracketedToken) {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_BRACKETS);
        } else {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_CONSTRUCT);
        }
    }

}
