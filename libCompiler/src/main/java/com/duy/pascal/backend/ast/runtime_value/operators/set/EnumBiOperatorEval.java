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

package com.duy.pascal.backend.ast.runtime_value.operators.set;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.OperatorTypes;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.data_types.set.EnumElementValue;
import com.duy.pascal.backend.data_types.set.EnumGroupType;
import com.duy.pascal.backend.runtime_exception.PascalArithmeticException;
import com.duy.pascal.backend.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.value.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;


public class EnumBiOperatorEval extends BinaryOperatorEval {

    public EnumBiOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                              OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        switch (operator_type) {
            case EQUALS:
            case GREATEREQ:
            case GREATERTHAN:
            case LESSEQ:
            case LESSTHAN:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
            case PLUS:
            case MINUS:
                EnumGroupType type = (EnumGroupType) operon1;
                return new RuntimeType(type, false);
            default:
                return null;
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException {
        EnumElementValue v1 = (EnumElementValue) value1;
        switch (operator_type) {
            case PLUS:
                int inc = (int) value2;
                return v1.getEnumGroupType().get(v1.getIndex() + inc);
            case MINUS:
                int inc2 = (int) value2;
                return v1.getEnumGroupType().get(v1.getIndex() - inc2);
        }
        EnumElementValue v2 = (EnumElementValue) value2;
        switch (operator_type) {
            case EQUALS:
                return v1.equals(v2);

            case NOTEQUAL:
                return !v1.equals(v2);

            case GREATEREQ:
                if (v1 == null) return true;
                if (v2 == null) return false;
                return v1.getIndex() >= v2.getIndex();

            case GREATERTHAN:
                return v1.getIndex() > v2.getIndex();

            case LESSEQ:
                if (v1 == null) return false;
                if (v2 == null) return true;
                return v1.getIndex() <= v2.getIndex();

            case LESSTHAN:
                return v1.getIndex() < v2.getIndex();

            default:
                return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new EnumBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }
}
