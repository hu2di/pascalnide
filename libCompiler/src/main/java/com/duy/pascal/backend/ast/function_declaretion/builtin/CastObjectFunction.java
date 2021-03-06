/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.ast.function_declaretion.builtin;


import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.JavaClassBasedType;
import com.duy.pascal.backend.data_types.PointerType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

/**
 * Casts an object to the class or the interface represented
 */
public class CastObjectFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new JavaClassBasedType(Object.class), true),
                    new RuntimeType(new JavaClassBasedType(Object.class), false)};

    @Override
    public String getName() {
        return "cast";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue pointer = arguments[0];
        RuntimeValue value = arguments[1];
        PointerType declType = (PointerType) pointer.getType(f).declType;
        Class<?> storageClass = declType.pointedToType.getStorageClass();
        return new InstanceObjectCall(pointer, value, storageClass, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class InstanceObjectCall extends FunctionCall {
        private RuntimeValue value;
        private Class<?> storageClass;
        private LineInfo line;
        private RuntimeValue pointer;

        InstanceObjectCall(RuntimeValue pointer, RuntimeValue value, Class<?> storageClass, LineInfo line) {
            this.value = value;
            this.pointer = pointer;
            this.storageClass = storageClass;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return null;
        }

        @Override
        public LineInfo getLineNumber() {
            return line;
        }


        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new InstanceObjectCall(pointer, value, storageClass, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new InstanceObjectCall(pointer, value, storageClass, line);
        }

        @Override
        protected String getFunctionName() {
            return "cast";
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            //indexOf reference of variable
            PascalReference pointer = (PascalReference) this.pointer.getValue(f, main);

            //indexOf value of arg 2
            Object value = this.value.getValue(f, main);

            //cast object to type of variable
            Object casted = storageClass.cast(value);

            //set value
            pointer.set(casted);
            return null;
        }

    }
}
