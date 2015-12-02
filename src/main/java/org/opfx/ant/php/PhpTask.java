package org.opfx.ant.php;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;

//TODO	:we need to resolve the tool; therefore we need to know that the tool is if any
//sometimes we know what are the required setting just as we start executing the task
// task - configurePhpExec(Executor); -> defines, libraries, 
//		- configurePhpTool(Tool tool);

public class PhpTask extends Task {
	protected Executor php;

	protected File srcdir;
	protected File output;
	protected Resources resources;

	protected boolean failOnError;
	protected int verbosity;

	protected PhpTask() {
		super();
		failOnError = true;
		verbosity = Project.MSG_VERBOSE;

	}

	public void init() throws BuildException {
		super.init();
		php = new Executor(this);
		php.createArg().setValue("-n");

	}

	/**
	 * Adds a collection of filesystem resources to be processed.
	 * 
	 * @param c
	 * @return void
	 */
	public void add(ResourceCollection c) {
		if (resources == null) {
			resources = new Resources();
		}
		resources.add(c);

	}

	public void setOutput(File output) {
		this.output = output;
	}

	public void setVerbose(final boolean verbose) {
		verbosity = verbose ? Project.MSG_INFO : Project.MSG_VERBOSE;
	}

	/**
	 * Indicates whether compilation errors will fail the build; defaults to
	 * true.
	 * 
	 * @param failOnError
	 */
	public void setFailOnError(boolean failOnError) {
		this.failOnError = failOnError;
	}

	public void setSrcdir(File dir) {
		FileSet src = new FileSet();
		src.setDir(dir);
		add(src);
	}

	public void execute() throws BuildException {
		validateParameters();
		Executor php = new Executor(this);
		configurePhp(php);
		Tool tool = new Tool(php);
		configureTool(tool);

		php.execute();
	}

	protected void validateParameters() throws BuildException {

	}

	protected void configurePhp(Executor php) throws BuildException {
		// FIXME move to executable
		php.define("extension_dir", "O:\\work\\tool\\php\\7\\ext");
		php.createArg().setValue("-n");
	}

	protected void configureTool(Tool tool) throws BuildException {

	}

}
