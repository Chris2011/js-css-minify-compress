package io.github.chris2011.netbeans.minifierbeans.ui.options;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import io.github.chris2011.netbeans.minifierbeans.css.ui.options.CssOptionsPanel;
import io.github.chris2011.netbeans.minifierbeans.html.ui.options.HtmlOptionsPanel;
import io.github.chris2011.netbeans.minifierbeans.javascript.ui.options.JsOptionsPanel;
import io.github.chris2011.netbeans.minifierbeans.json.ui.options.JsonOptionsPanel;
import io.github.chris2011.netbeans.minifierbeans.project.ui.options.ProjectOptionsPanel;
import io.github.chris2011.netbeans.minifierbeans.xml.ui.options.XmlOptionsPanel;

final class MinificationPanel extends javax.swing.JPanel implements TreeSelectionListener {

    private final MinificationOptionsPanelController controller;

    MinificationPanel(MinificationOptionsPanelController controller) {
        this.controller = controller;
        initComponents();

        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode("Minification options");

        defaultMutableTreeNode.add(new DefaultMutableTreeNode("JavaScript"));
        defaultMutableTreeNode.add(new DefaultMutableTreeNode("CSS"));
        defaultMutableTreeNode.add(new DefaultMutableTreeNode("HTML"));
        defaultMutableTreeNode.add(new DefaultMutableTreeNode("XML"));
        defaultMutableTreeNode.add(new DefaultMutableTreeNode("JSON"));
        defaultMutableTreeNode.add(new DefaultMutableTreeNode("Project"));

        final DefaultTreeModel defaultTreeModel = new DefaultTreeModel(defaultMutableTreeNode);

        optionsTree.setModel(defaultTreeModel);
        optionsTree.setRootVisible(false);

        //Where the tree is initialized:
        optionsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        optionsTree.addTreeSelectionListener(this);
        optionsTree.setSelectionRow(1);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // Returns the last path element of the selection.
        // This method is useful only when the selection model allows a single selection.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) optionsTree.getLastSelectedPathComponent();

        final String nodeValue = node.toString().replaceAll("\\s+", "").toLowerCase();
        
        contentPanel.removeAll();

        if (nodeValue.equals("javascript")) {
            contentPanel.add(JsOptionsPanel.create());
        }
        else if (nodeValue.equals("css")) {
            contentPanel.add(CssOptionsPanel.create());
        }
        else if (nodeValue.equals("html")) {
            contentPanel.add(HtmlOptionsPanel.create());
        }
        else if (nodeValue.equals("xml")) {
            contentPanel.add(XmlOptionsPanel.create());
        }
        else if (nodeValue.equals("json")) {
            contentPanel.add(JsonOptionsPanel.create());
        }
        else if (nodeValue.equals("project")) {
            contentPanel.add(ProjectOptionsPanel.create());
        }

        contentPanel.repaint();
        contentPanel.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        optionsTree = new javax.swing.JTree();
        contentPanel = new javax.swing.JPanel();

        jScrollPane1.setViewportView(optionsTree);

        contentPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        // TODO read settings and initialize GUI
        // Example:        
        // someCheckBox.setSelected(Preferences.userNodeForPackage(MinificationPanel.class).getBoolean("someFlag", false));
        // or for org.openide.util with API spec. version >= 7.4:
        // someCheckBox.setSelected(NbPreferences.forModule(MinificationPanel.class).getBoolean("someFlag", false));
        // or:
        // someTextField.setText(SomeSystemOption.getDefault().getSomeStringProperty());
    }

    void store() {
        // TODO store modified settings
        // Example:
        // Preferences.userNodeForPackage(MinificationPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or for org.openide.util with API spec. version >= 7.4:
        // NbPreferences.forModule(MinificationPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or:
        // SomeSystemOption.getDefault().setSomeStringProperty(someTextField.getText());
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree optionsTree;
    // End of variables declaration//GEN-END:variables
}
