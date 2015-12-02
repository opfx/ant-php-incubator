package org.opfx.ant.php;

import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Commandline;

/**
 * Responsible for resolving the tool validating basic rules about tool
 * arguments (the first tool argument needs to be -- so that php does
 * 
 * @author Bogdan
 *
 */
public class Tool extends ExecTask {
	private Executor php;

	public Tool(Executor php) {
		super();
		this.php = php;
		php.setTool(this);

	}

	public String getExecutable() {
		String executable = cmdl.getExecutable();
		return resolveExecutable(executable, false);

	}

	public Commandline getCommandline() {
		return cmdl;
	}

	protected String resolveExecutable(String exec, boolean mustSearchPath) {
		return "O:\\work\\proj\\opfx\\ant\\ant-php\\mainline\\ant-php\\src\\main\\resources\\tool\\" + exec;

		// return super.resolveExecutable(exec, mustSearchPath);
	}
}
