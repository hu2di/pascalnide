package com.duy.pascal.backend.ast.runtime_value.value.boxing;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.value.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.PointerType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class GetAddress extends DebuggableReturnValue {
    private final AssignableValue target;
    private LineInfo line;

    public GetAddress(AssignableValue target) throws UnAssignableTypeException {
        this.line = target.getLineNumber();
        this.target = target;
        this.outputFormat = target.getOutputFormat();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.getType(f).declType),
                false);
    }

    @Override
    public LineInfo getLineNumber() {
        return target.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return target.getReference(f, main);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }
}
