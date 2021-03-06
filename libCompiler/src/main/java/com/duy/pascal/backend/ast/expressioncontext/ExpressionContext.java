package com.duy.pascal.backend.ast.expressioncontext;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.ast.ConstantDefinition;
import com.duy.pascal.backend.ast.NamedEntity;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.CodeUnit;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;

import java.util.List;


public interface ExpressionContext extends CompileTimeContext {

    @Nullable
    LineInfo getStartLine();

    RuntimeValue getIdentifierValue(WordToken name)
            throws ParsingException;

    void verifyNonConflictingSymbol(NamedEntity n) throws DuplicateIdentifierException;

    VariableDeclaration getVariableDefinition(String ident);

    void getCallableFunctions(String name, List<List<AbstractFunction>> listsofar);

    boolean functionExists(String name);

    void declareConst(ConstantDefinition c);

    ConstantDefinition getConstantDefinitionLocal(String indent);

    DeclaredType getTypedefTypeLocal(String ident);

    VariableDeclaration getVariableDefinitionLocal(String ident);

    List<AbstractFunction> getCallableFunctionsLocal(String name);

    boolean functionExistsLocal(String name);

    CodeUnit root();

    Executable handleUnrecognizedStatement(Token next, GrouperToken container)
            throws ParsingException;

    boolean handleUnrecognizedDeclaration(Token next, GrouperToken container)
            throws ParsingException;

}
