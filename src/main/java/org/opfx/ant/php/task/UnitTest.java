package org.opfx.ant.php.task;

import org.apache.tools.ant.BuildException;
import org.opfx.ant.php.PhpTask;
import org.opfx.ant.php.Tool;

public class UnitTest extends PhpTask {

	protected void configureTool(Tool tool) throws BuildException {
		tool.setExecutable("phpunit-4.4.1.phar");
		tool.createArg().setValue("--version");
		// tool.createArg().setValue("--bootstrap");
		// tool.createArg().setValue(
		// "O:\\work\\proj\\opfx\\ant\\ant-php\\mainline\\ant-php\\src\\test\\resources\\project\\dist\\example-test.phar");
		// tool.createArg().setValue("phar://example-test.phar/org/example/lang/ObjectTest.php");
	}

}
