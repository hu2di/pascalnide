package com.duy.pascal.backend.ast.runtime_value.value;


import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class StringIndex extends DebuggableAssignableValue {
    RuntimeValue string;
    RuntimeValue index;

    public StringIndex(RuntimeValue string, RuntimeValue index) {
        this.index = index;
        this.string = string;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return str.charAt(ind - 1);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return new StringIndexReference(str, ind);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        boolean writable = string.getType(f).writable;
        return new RuntimeType(BasicType.Character, writable);
    }

    @Override
    public LineInfo getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        StringBuilder str = (StringBuilder) string.compileTimeValue(context);
        int ind = (int) index.compileTimeValue(context);
        return str.charAt(ind - 1);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        RuntimeValue cstr = string.compileTimeExpressionFold(context);
        RuntimeValue cind = index.compileTimeExpressionFold(context);
        return new StringIndex(cstr, cind);
    }
}
