package com.duy.pascal.backend.ast.runtime_value.value.boxing;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class CharacterBoxer extends DebuggableReturnValue {
    private RuntimeValue charValue;

    public CharacterBoxer(RuntimeValue charValue) {
        this.charValue = charValue;
        this.outputFormat = charValue.getOutputFormat();
    }

    @Override
    public LineInfo getLineNumber() {
        return charValue.getLineNumber();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) {
        return new RuntimeType(BasicType.StringBuilder, false);
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return new StringBuilder(charValue.getValue(f, main).toString());
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object val = charValue.compileTimeValue(context);
        if (val != null) {
            return new StringBuilder(val.toString());
        } else {
            return null;
        }
    }


    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, charValue.getLineNumber());
        } else {
            return new CharacterBoxer(charValue.compileTimeExpressionFold(context));
        }
    }

}
