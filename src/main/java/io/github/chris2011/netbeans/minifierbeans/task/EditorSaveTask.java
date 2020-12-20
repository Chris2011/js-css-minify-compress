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
package io.github.chris2011.netbeans.minifierbeans.task;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import io.github.chris2011.netbeans.minifierbeans.ui.MinifyProperty;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.document.OnSaveTask;
import io.github.chris2011.netbeans.minifierbeans.css.CSSMinify;
import io.github.chris2011.netbeans.minifierbeans.html.HTMLMinify;
import io.github.chris2011.netbeans.minifierbeans.javascript.JSMinify;
import io.github.chris2011.netbeans.minifierbeans.json.JSONMinify;
import io.github.chris2011.netbeans.minifierbeans.xml.XMLMinify;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

public class EditorSaveTask implements OnSaveTask {
    private final Context context;

    public EditorSaveTask(Context ctx) {
        context = ctx;
    }

    @Override
    public void performTask() {
        try {

            Document document = context.getDocument();
            String content = document.getText(0, document.getLength());

            DataObject dataObject = NbEditorUtilities.getDataObject(context.getDocument());
            if (dataObject.getPrimaryFile().getMIMEType().equals("text/html") && MinifyProperty.getInstance().isAutoMinifyHTML()) {
                HTMLMinify.execute(dataObject, content, false);
            } else if (dataObject.getPrimaryFile().getMIMEType().equals("text/javascript") && MinifyProperty.getInstance().isAutoMinifyJS()) {
                JSMinify.execute(dataObject, null, content, false);
            } else if (dataObject.getPrimaryFile().getMIMEType().equals("text/css") && MinifyProperty.getInstance().isAutoMinifyCSS()) {
                CSSMinify.execute(dataObject, null, content, false);
            } else if (dataObject.getPrimaryFile().getMIMEType().equals("text/x-json") && MinifyProperty.getInstance().isAutoMinifyJSON()) {
                JSONMinify.execute(dataObject, content, false);
            } else if (dataObject.getPrimaryFile().getMIMEType().equals("text/xml") && MinifyProperty.getInstance().isAutoMinifyXML()) {
                XMLMinify.execute(dataObject, content, false);
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void runLocked(Runnable r) {
        r.run();
    }

    @Override
    public boolean cancel() {
        return true;
    }

}
