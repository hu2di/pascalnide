package com.duy.pascal.backend.ast.runtime_value.value.cloning;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;

public class CloneableObjectCloner implements RuntimeValue {
    protected RuntimeValue[] outputFormat;
    private RuntimeValue r;

    public CloneableObjectCloner(RuntimeValue r) {
        this.r = r;
        this.outputFormat = r.getOutputFormat();
    }

    @Override
    public RuntimeType getType(ExpressionContext f)
            throws ParsingException {
        return r.getType(f);
    }

    @Override
    public RuntimeValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object value = r.getValue(f, main);
        if (value instanceof ContainsVariables) {
            ContainsVariables c = (ContainsVariables) value;
            return c.clone();
        }
        return value;
    }

    @Override
    public LineInfo getLineNumber() {
        return r.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        ContainsVariables c = (ContainsVariables) r.compileTimeValue(context);
        return c.clone();
    }


    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new CloneableObjectCloner(r.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}