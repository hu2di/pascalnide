package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.MultipleDefinitionsMainException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.codeunit.RuntimePascalProgram;

import java.io.Reader;
import java.util.List;

public class PascalProgram extends ExecutableCodeUnit {
    public Executable main;

    public FunctionOnStack mainRunning;

    public PascalProgram(ListMultimap<String, AbstractFunction> functionTable) {
        super(functionTable);
    }

    public PascalProgram(Reader program,
                         ListMultimap<String, AbstractFunction> functionTable,
                         String sourceName, List<ScriptSource> includeDirectories)
            throws ParsingException {
        super(program, functionTable, sourceName, includeDirectories);
    }

    @Override
    protected PascalProgramExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> f) {
        return new PascalProgramExpressionContext(f);
    }

    @Override
    public RuntimeExecutable<PascalProgram> run() {
        return new RuntimePascalProgram(this);
    }

    protected class PascalProgramExpressionContext extends
            CodeUnitExpressionContext {
        protected PascalProgramExpressionContext(
                ListMultimap<String, AbstractFunction> f) {
            super(f);
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            if (main != null) {
                throw new MultipleDefinitionsMainException(i.peek().lineInfo, "Multiple definitions of main.");
            }
            main = i.get_next_command(this);
            if (!(i.peek() instanceof PeriodToken)) {
                throw new ExpectedTokenException(".", i.peek());
            }
            i.take();
        }
    }

}
