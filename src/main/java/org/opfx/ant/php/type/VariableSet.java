package org.opfx.ant.php.type;

import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

public class VariableSet extends DataType implements Cloneable {

	private Vector<Variable> variables;

	public static class Variable {
		String name;
		String value;

		public Variable(String name, String value) {
			setName(name);
			setValue(value);
		}

		public Variable() {

		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String toString() {
			return getName() + "=" + getValue();
		}

	}

	public VariableSet() {
		super();
		variables = new Vector<Variable>();
	}

	public synchronized void addVar(String name, String value) {
		addVariable(name, value);
	}

	public synchronized void addVariable(String name, String value) {
		addVariable(new Variable(name, value));
	}

	public synchronized void addVariable(Variable variable) {
		if (isReference()) {
			throw noChildrenAllowed();
		}
		variables.addElement(variable);
	}

	protected VariableSet getReference() {
		return getCheckedRef(VariableSet.class, "variables");
	}

	public synchronized Vector<Variable> getVariables() {
		if (isReference()) {
			return getReference().getVariables();
		}
		dieOnCircularReference();
		return variables;
	}

	public synchronized Object clone() throws BuildException {
		if (isReference()) {
			return getReference().clone();
		}
		try {
			VariableSet clone = (VariableSet) super.clone();
			@SuppressWarnings("unchecked")
			Vector<Variable> clonedVariables = (Vector<Variable>) getVariables().clone();
			clone.variables = clonedVariables;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new BuildException(e);
		}
	}
}
