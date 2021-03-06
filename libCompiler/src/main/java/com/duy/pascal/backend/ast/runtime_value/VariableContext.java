package com.duy.pascal.backend.ast.runtime_value;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;

public abstract class VariableContext implements ContainsVariables {

    /**
     * Global variable of function
     *
     * @param name - name of var
     * @return - value of variable
     * @throws RuntimePascalException
     */
    public abstract Object getLocalVar(String name)
            throws RuntimePascalException;

    public abstract boolean setLocalVar(String name, Object val);

    @Override
    @Nullable
    public Object getVar(String name) throws RuntimePascalException {
        Object result = this.getLocalVar(name);
        VariableContext parentcontext = getParentContext();
        if (result == null && parentcontext != null) {
            result = parentcontext.getVar(name);
        }
        if (result == null) {
            System.err.println("Warning!  Fetched null variable!");
        }
        return result;
    }

    @Override
    public void setVar(String name, Object val) {
        if (val == null) {
            System.err.println("Warning!  Setting null variable!");
        }
        if (setLocalVar(name, val)) {
            return;
        }
        VariableContext parentcontext = getParentContext();
        if (parentcontext != null) {
            parentcontext.setVar(name, val);
        }
    }

    public abstract VariableContext getParentContext();

    @Override
    public VariableContext clone() {
        return null;
    }


}
