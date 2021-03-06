package com.duy.pascal.backend.ast.runtime_value;

import com.duy.pascal.backend.ast.runtime_value.references.Reference;

public class ObjectBasedPointer<T> implements Reference<T> {
	public T obj;

	public ObjectBasedPointer(T val) {
		obj = val;
	}

	@Override
	public T get() {
		return obj;
	}

	@Override
	public void set(T value) {
		obj = value;
	}

	@Override
	public ObjectBasedPointer<T> clone() {
        return new ObjectBasedPointer<>(obj);
	}
}
