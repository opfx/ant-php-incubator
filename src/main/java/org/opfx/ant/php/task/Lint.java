package org.opfx.ant.php.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Parallel;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.opfx.ant.php.Executor;
import org.opfx.ant.php.PhpTask;

// TODO make sure the sync work
// TODO lint does work if:
// - any of the existing files has changed; then we should only lint the changed file
// - the list of files in the source dir(s) has changed (added,removed,moved); we lint only the changed file
// - any of the variables that we need to replace in source code has changed 
// 	(for this to work successfully we need to know which files are affected by which variables (maybe))
// - the lint can only be incremental if we have a destdir or we use a tempdir

// TODO make sure that exclude works so that we can exclude stub
//<php: lint destdir=''>
// <sources>
// 	<fileset dir="srcdir" exclude="stub.*"/>
//	<fileset dir="gensrc >
// </sources>
//<php:lint>
public class Lint extends PhpTask {

	private static int DEFAULT_THREAD_COUNT = 25;
	private Processor processor;
	private boolean parallel;
	private int threadCount;

	public Lint() {
		super();
		parallel = false;
		threadCount = DEFAULT_THREAD_COUNT;
	}

	public void add(ResourceCollection rc) {
		if (rc instanceof FileSet && rc.isFilesystemOnly()) {
			// receives special treatment in copy that this task relies on
			((FileSet) rc).appendIncludes(new String[] { "**/*.php" });
			processor.add(rc);
		} else {
			if (resources == null) {
				Restrict r = new Restrict();
				r.add(new Exists());
				r.add(resources = new Resources());
				processor.add(r);
			}
			resources.add(rc);
		}
	}

	/**
	 * Location to store the class files.
	 * 
	 * @param dir
	 */
	public void setDestDir(File dir) {
		output = dir;

	}

	/**
	 * Indicates whether the source files to be compiled will be listed;
	 * defaults to no.
	 * 
	 * @param flag
	 */
	public void setListFiles(boolean flag) {
		// TODO
	}

	public void init() throws BuildException {
		super.init();
		php.createArg().setValue("-l");
		processor = new Processor(this);
		processor.init();
	}

	public void lint(Hashtable<String, String[]> sources) {
		if (sources.size() == 0) {
			log("Nothing to lint", Project.MSG_INFO);
			return;
		}

		log(String.format("Linting %d file%s...", sources.size(), (sources.size() == 1 ? "" : "s")));

		if (!parallel) {
			threadCount = 1;
		}

		log(String.format("Using %d thread%s", threadCount, (threadCount == 1 ? "" : "s")));
		Parallel runner = new Parallel();
		runner.setProject(getProject());

		for (Map.Entry<String, String[]> entry : sources.entrySet()) {
			String source = entry.getKey();

			if (!source.contains(".php")) {
				continue;
			}

			Executor task = null;
			try {
				task = (Executor) php.clone();
			} catch (CloneNotSupportedException e) {
				log("should not happen");
			}

			if (Os.isFamily(Os.FAMILY_WINDOWS)) {
				task.createArg().setValue("\"" + source + "\"");
			} else {
				task.createArg().setValue(source);
			}
			task.setResultProperty(String.valueOf(source.hashCode()) + ".lint");
			runner.addTask(task);
		}

		runner.execute();

		// collect failures
		ArrayList<String> failures = new ArrayList<String>();

		for (Map.Entry<String, String[]> entry : sources.entrySet()) {
			String source = entry.getKey();
			String status = getProject().getProperty(String.valueOf(source.hashCode() + ".lint"));
			if (status != null && status.equals("-1")) {
				failures.add(source);
			}
		}

		if (failures.size() > 0 && failOnError) {

		}

		// discard failures
		for (String failure : failures) {
			sources.remove(failure);
		}

	}

	public void execute() throws BuildException {
		processor.setFiltering(false);
		processor.setIncludeEmptyDirs(false);
		processor.setPreserveLastModified(true);

		// processor.add(resources);
		processor.setTodir(output);

		processor.execute();
	}

	class Processor extends Copy {
		Lint owner;

		Processor(Lint owner) {
			super();
			this.owner = owner;
			setProject(owner.getProject());
			setTaskName(owner.getTaskName());
			setOwningTarget(owner.getOwningTarget());

		}

		protected void doFileOperations() {
			try {
				owner.lint(fileCopyMap);
			} finally {
				super.doFileOperations();
			}
		}
	}

}
