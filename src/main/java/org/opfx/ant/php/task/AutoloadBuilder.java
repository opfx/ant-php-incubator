package org.opfx.ant.php.task;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.opfx.ant.php.Executor;
import org.opfx.ant.php.PhpTask;
import org.opfx.ant.php.Tool;

// info : https://github.com/theseer/Autoload

// TODO : use require_once rather then require
// TODO : basedir
// TODO : output =>destfile
// TODO : alias	
// TODO : includes
// TODO : excludes
// TODO : template/stub

// TODO : quiet [-q/--quiet]
// TODO : compression
// TODO : template variables
// TODO : metadata

//-the strategy should be create an autoloader file in the temp directory
// compare the generated autoloader file with the one that already exists
// if the generated one is different than the one that exists, overwrite 
// the existing one

//-if no stub file is provided, attempt to resolve the stubfile in the basedir;
// if none is found use the one that comes inside the resources of the phar;
// if none is found use the one that is inside the tool itself

//-nologo

public class AutoloadBuilder extends PhpTask {

	protected File basedir;
	protected File template;

	public void setSrcdir(File dir) {
		basedir = dir;
	}

	protected void validateParameters() throws BuildException {
		if (this.output == null) {
			throw new BuildException("You must specify the autoload file to create!");
		}
	}

	protected void configurePhp(Executor php) throws BuildException {
		super.configurePhp(php);
		php.loadExtension("fileinfo");
	}

	protected String getTemplate() {
		if (template != null) {
			return template.getAbsolutePath();
		}
		// FIXME
		return new File(
				"O:\\work\\proj\\opfx\\ant\\ant-php\\mainline\\ant-php\\src\\main\\resources\\template\\autoload.php")
						.getAbsolutePath();

	}

	protected void configureTool(Tool tool) throws BuildException {
		tool.setExecutable("phpab-1.20.3.phar");
		tool.createArg().setValue("--template");
		tool.createArg().setValue(getTemplate());

		tool.createArg().setValue("--output");
		tool.createArg().setValue(output.toString());
		tool.createArg().setValue("--basedir");
		tool.createArg().setValue(basedir.getAbsolutePath());
		tool.createArg().setValue(basedir.getAbsolutePath());
		// for (Resource resource : resources) {
		// tool.createArg().setValue(resource.toString());
		// }
	}

}
