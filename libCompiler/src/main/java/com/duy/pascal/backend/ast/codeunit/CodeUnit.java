package com.duy.pascal.backend.ast.codeunit;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.UnrecognizedTokenException;
import com.duy.pascal.backend.source_include.ScriptSource;
import com.duy.pascal.backend.tokenizer.NewLexer;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.IRunnablePascal;

import java.io.Reader;
import java.util.List;

public abstract class CodeUnit {
    public final ExpressionContextMixin context;
    @Nullable
    protected String programName;
    private List<ScriptSource> includeDirectories;

    public CodeUnit(IRunnablePascal handler) {
        this.context = getExpressionContextInstance( handler);
    }

    public CodeUnit(Reader program,
                    String sourceName, List<ScriptSource> includeDirectories,
                    @Nullable IRunnablePascal handler)
            throws ParsingException {
        this(handler);
        this.includeDirectories = includeDirectories;

        NewLexer lexer = new NewLexer(program, sourceName, includeDirectories);
        lexer.parse();
        parseTree(lexer.getTokenQueue());
    }

    public ExpressionContextMixin getContext() {
        return context;
    }

    public ExpressionContextMixin getProgram() {
        return context;
    }

    protected abstract CodeUnitExpressionContext getExpressionContextInstance(IRunnablePascal handler);

    private void parseTree(GrouperToken tokens) throws ParsingException {
        while (tokens.hasNext()) {
            context.addNextDeclaration(tokens);
        }
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> run();

    public String getProgramName() {
        return programName;
    }

    public List<ScriptSource> getIncludeDirectories() {
        return includeDirectories;
    }

    public void setIncludeDirectories(List<ScriptSource> includeDirectories) {
        this.includeDirectories = includeDirectories;
    }

    protected abstract class CodeUnitExpressionContext extends ExpressionContextMixin {
        public CodeUnitExpressionContext(@Nullable IRunnablePascal handler,
                                         boolean isLibrary) {
            super(CodeUnit.this, null, handler, isLibrary);
        }

        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            throw new UnrecognizedTokenException(next);
        }


        @Override
        protected void handleBeginEnd(GrouperToken i) throws ParsingException {
            i.take();
        }

    }

}
