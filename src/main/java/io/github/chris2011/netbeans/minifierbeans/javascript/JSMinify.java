/**
 * Copyright [2013] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.chris2011.netbeans.minifierbeans.javascript;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import org.netbeans.api.progress.ProgressHandle;
import io.github.chris2011.netbeans.minifierbeans.ui.MinifyProperty;
import io.github.chris2011.netbeans.minifierbeans.util.source.minify.MinifyFileResult;
import io.github.chris2011.netbeans.minifierbeans.util.source.minify.MinifyUtil;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.NotificationDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;
import org.openide.windows.TopComponent;

@ActionID(category = "Build",
        id = "org.netbeans.util.source.minify.JSMinify")
@ActionRegistration(iconBase = "io/github/chris2011/netbeans/minifierbeans/util/source/minify/compress.png",
        displayName = "#CTL_JSMinify")
@ActionReferences({
    @ActionReference(path = "Loaders/text/javascript/Actions", position = 200, separatorBefore = 150, separatorAfter = 250)
})
@Messages("CTL_JSMinify=Minify JS")
public final class JSMinify implements ActionListener {
    private final DataObject context;
    private final static RequestProcessor RP = new RequestProcessor("JSMinify", 1, true);

    public JSMinify(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        execute(context, null, null, true);
    }

    public static void execute(final DataObject context, final FileObject file, final String content, final boolean notify) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                jsMinify(context, file, content, notify);
            }
        };

        final RequestProcessor.Task theTask = RP.create(runnable);

        String foString = context == null ? file.getName() : context.getPrimaryFile().getName();

        final ProgressHandle ph = ProgressHandle.createHandle("Minifying JS " + foString, theTask);

        theTask.addTaskListener(new TaskListener() {
            @Override
            public void taskFinished(org.openide.util.Task task) {
                ph.finish();
            }
        });

        ph.start();
        theTask.schedule(0);
    }

    private static void jsMinify(DataObject context, FileObject file, String content, boolean notify) {
        Project project = TopComponent.getRegistry().getActivated().getLookup().lookup(Project.class);
        FileObject primaryFile = null;

        if (context != null) {
            primaryFile = context.getPrimaryFile();
        } else {
            primaryFile = file;
        }

        MinifyProperty minifyProperty = MinifyProperty.getInstance();
        MinifyUtil util = new MinifyUtil();
        MinifyFileResult minifyFileResult = new MinifyFileResult();

        if (!util.isMinifiedFile(primaryFile.getName(), minifyProperty.getPreExtensionJS())) {
            String inputFilePath = primaryFile.getPath();
            String outputFilePath;

            if (minifyProperty.isNewJSFile() && minifyProperty.getPreExtensionJS() != null && !minifyProperty.getPreExtensionJS().trim().isEmpty()) {
                outputFilePath = primaryFile.getParent().getPath() + "/" + primaryFile.getName() + minifyProperty.getPreExtensionJS() + "." + primaryFile.getExt();
            } else {
                outputFilePath = inputFilePath;
            }

            File inputFile = new File(inputFilePath);
            File outputFile = new File(outputFilePath);
            minifyFileResult.setInputFileSize(inputFile.length());

            StringWriter outputWriter = new StringWriter();

            outputWriter.flush();

            if (project == null) {
                project = FileOwnerQuery.getOwner(primaryFile);
            }

            GoogleClosureCompilerCliExecutable googleClosureCompilerCliExecutable = GoogleClosureCompilerCliExecutable.getDefault(project);
            Future<Integer> task = googleClosureCompilerCliExecutable.generate(inputFile, outputFile, minifyProperty.getCompilerFlagsJS());

            try {
                task.get(1, TimeUnit.MINUTES);

                minifyFileResult.setOutputFileSize(outputFile.length());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException | TimeoutException ex) {
                Exceptions.printStackTrace(ex);
            }

//                if (content != null) {
//                    minifyFileResult = util.compressContent(inputFilePath, content, "text/javascript", outputFilePath, minifyProperty);
//                } else {
//                    minifyFileResult = util.compress(inputFilePath, "text/javascript", outputFilePath, minifyProperty);
//                }

            if (minifyProperty.isEnableOutputLogAlert() && notify) {
                NotificationDisplayer.getDefault().notify("Successful JS minification",
                        NotificationDisplayer.Priority.NORMAL.getIcon(), String.format(
                        "Input JS Files Size: %s Bytes \n"
                        + "JS Minified Completed Successfully\n"
                        + "After Minifying JS Files Size: %s Bytes \n"
                        + "JS Space Saved %s%%", minifyFileResult.getInputFileSize(), minifyFileResult.getOutputFileSize(), minifyFileResult.getSavedPercentage()), null);
            }
        }
    }
}
