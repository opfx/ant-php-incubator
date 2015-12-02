package org.opfx.ant.php;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.Redirector;
import org.apache.tools.ant.types.Commandline;

//TODO php version should be customizable per project
//TODO resolve php
//TODO decorate extensions based on the operating system
/**
 * Responsible with resolving php and its extensions
 * 
 *
 */
public class Executor extends ExecTask {

	private boolean executing = false;
	private Tool tool;

	public Executor(Task owner) {
		super();
		this.bindToOwner(owner);
		this.setExecutable("O:\\work\\tool\\php\\7\\php.exe");
	}

	/**
	 * Sets a custom value for any configuration directive allowed in php.ini.
	 * 
	 * 
	 */
	public void define(String directive, String value) {
		if (value != null) {
			value = "=" + value;
		}

		directive = directive.trim();
		directive = directive.toLowerCase();
		this.createArg().setValue("-d " + directive + value);

	}

	public void define(String directive) {
		define(directive, null);
	}

	public void loadExtension(String extension) {
		extension = extension.toLowerCase();
		define("extension", resolveExtension(extension));
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	protected void runExec(Execute exe) throws BuildException {
		// add the tool executable (if any)
		if (tool != null) {
			cmdl.createArgument().setValue(tool.getExecutable());
			cmdl.addArguments(tool.getCommandline().getArguments());
		}
		// cmdl.createArgument().setValue("--version");
		super.runExec(exe);
	}

	public Object clone() throws CloneNotSupportedException {
		if (this.executing) {
			throw new CloneNotSupportedException("An executing task cannot be cloned");
		}
		Executor clone = (Executor) super.clone();
		clone.cmdl = (Commandline) cmdl.clone();
		redirector = new Redirector(this);
		return clone;
	}

	private String resolveExtension(String extension) {
		return "php_" + extension + ".dll";
	}

}
