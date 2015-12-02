package org.opfx.ant.php.task;

import org.apache.tools.ant.BuildException;
import org.opfx.ant.php.Executor;
import org.opfx.ant.php.Tool;

// info : https://github.com/theseer/Autoload

// TODO : use require_once rather then require
// TODO : basedir
// TODO : output =>destfile
// TODO : alias; if not provided use lowercased, stripped version of the filename 
// TODO : includes
// TODO : excludes
// TODO : template/stub

// TODO : quiet [-q/--quiet]
// TODO : compression
// TODO : template variables
// TODO : metadata

public class Phar extends AutoloadBuilder {

	protected void configurePhp(Executor php) throws BuildException {
		super.configurePhp(php);
		php.define(" phar.readonly", "0");
	}

	protected void configureTool(Tool tool) throws BuildException {
		super.configureTool(tool);
		tool.setExecutable("phpab-1.20.3.phar");
		tool.createArg().setValue("--phar");

	}
}
