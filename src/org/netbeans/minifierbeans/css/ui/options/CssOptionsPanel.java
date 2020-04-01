package org.netbeans.minifierbeans.css.ui.options;

import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.minifierbeans.css.CssNanoCliExecutable;
import org.netbeans.minifierbeans.css.FileUtils;
import org.netbeans.minifierbeans.project.ui.options.ProjectOptionsPanel;
import org.netbeans.minifierbeans.ui.MinifyProperty;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;

public final class CssOptionsPanel extends JPanel implements ChangeListener {
    private MinifyProperty minifyProperty;

    private static final long serialVersionUID = 1L;
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public CssOptionsPanel() {
        assert EventQueue.isDispatchThread();

        initComponents();

        init();
    }

    private void init() {
        DocumentListener defaultDocumentListener = new DefaultDocumentListener();
        cssNanoCliPathTextField.getDocument().addDocumentListener(defaultDocumentListener);
        
        final ProjectOptionsPanel projectOptionsPanel = new ProjectOptionsPanel();

        minifyProperty = MinifyProperty.getInstance();

        /*-------- CSS ----------*/
        newCSSFile.setSelected(minifyProperty.isNewCSSFile());
        preExtensionCSS.setEnabled(minifyProperty.isNewCSSFile());
        preExtensionCSS_Label.setEnabled(minifyProperty.isNewCSSFile());
        projectOptionsPanel.skipPreExtensionCSS.setEnabled(minifyProperty.isNewCSSFile());
        this.preExtensionCSS.setText(minifyProperty.getPreExtensionCSS());

        autoMinifyCSS.setSelected(minifyProperty.isAutoMinifyCSS());
        headerEditorPaneCSS.setText(minifyProperty.getHeaderCSS());

        headerEditorPaneCSS.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
            }

            @Override
            public void focusLost(FocusEvent fe) {
                String text = headerEditorPaneCSS.getText();
                if (text == null || text.trim().isEmpty()) {
                    text = "";
                    headerEditorPaneCSS.setText("");
                } else {
                    text = text.trim();
                }
                minifyProperty.setHeaderCSS(text);
            }
        });
        this.autoMinifyCSS.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    minifyProperty.setAutoMinifyCSS(Boolean.TRUE);
                } else {
                    minifyProperty.setAutoMinifyCSS(Boolean.FALSE);
                }
            }
        });
        this.newCSSFile.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    minifyProperty.setNewCSSFile(Boolean.TRUE);
                    minifyProperty.setPreExtensionCSS(".min");
                    preExtensionCSS.setText(".min");
                    preExtensionCSS.setEnabled(Boolean.TRUE);
                    preExtensionCSS_Label.setEnabled(Boolean.TRUE);

                    if (minifyProperty.isBuildCSSMinify() && minifyProperty.isNewCSSFile()) {
                        projectOptionsPanel.skipPreExtensionCSS.setEnabled(Boolean.TRUE);
                        minifyProperty.setSkipPreExtensionCSS(Boolean.TRUE);
                        projectOptionsPanel.skipPreExtensionCSS.setSelected(Boolean.TRUE);
                    }

                } else {
                    minifyProperty.setNewCSSFile(Boolean.FALSE);
                    preExtensionCSS.setEnabled(Boolean.FALSE);
                    preExtensionCSS_Label.setEnabled(Boolean.FALSE);
                    projectOptionsPanel.skipPreExtensionCSS.setEnabled(Boolean.FALSE);
                    minifyProperty.setSkipPreExtensionCSS(Boolean.FALSE);
                    projectOptionsPanel.skipPreExtensionCSS.setSelected(Boolean.FALSE);
                }
            }
        });

        this.preExtensionCSS.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
            }

            @Override
            public void focusLost(FocusEvent fe) {
                String text = preExtensionCSS.getText();
                if (text == null || text.trim().isEmpty()) {
                    text = ".min";
                    preExtensionCSS.setText(text);
                } else {
                    text = text.trim();
                }
                minifyProperty.setPreExtensionCSS(text);
            }
        });
    }

    public static CssOptionsPanel create() {
        CssOptionsPanel panel = new CssOptionsPanel();

        return panel;
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public String getCssNanoCli() {
        return cssNanoCliPathTextField.getText();
    }

    public void setCssNanoCli(String cssNanoCli) {
        cssNanoCliPathTextField.setText(cssNanoCli);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cssNanoLabel = new javax.swing.JLabel();
        cssNanoCliPathTextField = new javax.swing.JTextField();
        cssNanoCliFolderBrowseButton = new javax.swing.JButton();
        cssNanoCliPathBrowseButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        preExtensionCSS_Label = new javax.swing.JLabel();
        extLabel = new javax.swing.JLabel();
        preExtensionCSS = new javax.swing.JTextField();
        autoMinifyCSS = new javax.swing.JCheckBox();
        newCSSFile = new javax.swing.JCheckBox();
        headerLabelCSS = new javax.swing.JLabel();
        headerScrollPaneCSS = new javax.swing.JScrollPane();
        headerEditorPaneCSS = new javax.swing.JEditorPane();

        org.openide.awt.Mnemonics.setLocalizedText(cssNanoLabel, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.cssNanoLabel.text")); // NOI18N

        cssNanoCliPathTextField.setText(org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.cssNanoCliPathTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cssNanoCliFolderBrowseButton, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.cssNanoCliFolderBrowseButton.text")); // NOI18N
        cssNanoCliFolderBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cssNanoCliFolderBrowseButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cssNanoCliPathBrowseButton, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.cssNanoCliPathBrowseButton.text")); // NOI18N
        cssNanoCliPathBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cssNanoCliPathBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cssNanoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cssNanoCliPathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cssNanoCliFolderBrowseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cssNanoCliPathBrowseButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cssNanoLabel)
                .addComponent(cssNanoCliPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cssNanoCliFolderBrowseButton)
                .addComponent(cssNanoCliPathBrowseButton))
        );

        org.openide.awt.Mnemonics.setLocalizedText(preExtensionCSS_Label, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.preExtensionCSS_Label.text")); // NOI18N

        extLabel.setForeground(extLabel.getForeground().darker());
        org.openide.awt.Mnemonics.setLocalizedText(extLabel, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.extLabel.text")); // NOI18N

        preExtensionCSS.setText(org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.preExtensionCSS.text")); // NOI18N

        autoMinifyCSS.setBackground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(autoMinifyCSS, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.autoMinifyCSS.text")); // NOI18N
        autoMinifyCSS.setOpaque(false);

        newCSSFile.setBackground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(newCSSFile, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.newCSSFile.text")); // NOI18N
        newCSSFile.setToolTipText(org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.newCSSFile.toolTipText")); // NOI18N
        newCSSFile.setOpaque(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autoMinifyCSS, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newCSSFile)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(preExtensionCSS_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(preExtensionCSS, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extLabel)))
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(autoMinifyCSS)
                .addGap(6, 6, 6)
                .addComponent(newCSSFile)
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(preExtensionCSS_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(preExtensionCSS, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extLabel))
                .addGap(6, 6, 6))
        );

        org.openide.awt.Mnemonics.setLocalizedText(headerLabelCSS, org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.headerLabelCSS.text")); // NOI18N

        headerEditorPaneCSS.setToolTipText(org.openide.util.NbBundle.getMessage(CssOptionsPanel.class, "CssOptionsPanel.headerEditorPaneCSS.toolTipText")); // NOI18N
        headerScrollPaneCSS.setViewportView(headerEditorPaneCSS);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(headerLabelCSS)
                            .addComponent(headerScrollPaneCSS, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)))
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(headerLabelCSS)
                .addGap(6, 6, 6)
                .addComponent(headerScrollPaneCSS, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @NbBundle.Messages("CssNanoCliOptionsPanel.executable.notFound=No CSSNano CLI executable found.")
    private void cssNanoCliPathBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cssNanoCliPathBrowseButtonActionPerformed
        List<String> cssNanoCliPaths = FileUtils.findFileOnUsersPath(CssNanoCliExecutable.CSS_NANO_CLI_NAME);

        if (cssNanoCliPaths.isEmpty()) {
            StatusDisplayer.getDefault().setStatusText(Bundle.CssNanoCliOptionsPanel_executable_notFound());
        } else {
            cssNanoCliPathTextField.setText(cssNanoCliPaths.get(0));
        }
    }//GEN-LAST:event_cssNanoCliPathBrowseButtonActionPerformed

    @NbBundle.Messages("CssNanoCliOptionsPanel.browse.title=Select CSSNano CLI")
    private void cssNanoCliFolderBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cssNanoCliFolderBrowseButtonActionPerformed
        File file = new FileChooserBuilder(CssOptionsPanel.class)
                .setFilesOnly(true)
                .setTitle("Bundle.CssNanoCliOptionsPanel_browse_title()")
                .showOpenDialog();
        if (file != null) {
            cssNanoCliPathTextField.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_cssNanoCliFolderBrowseButtonActionPerformed

//    void load() {
//        String ngCli = NbPreferences.forModule(CssOptionsPanel.class).get("ngCliExecutableLocation", "");
//        cssNanoCliPathTextField.setText(ngCli);
//    }
//    void store() {
//        NbPreferences.forModule(CssOptionsPanel.class).put("ngCliExecutableLocation", cssNanoCliPathTextField.getText());
//    }
    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JCheckBox autoMinifyCSS;
    private javax.swing.JButton cssNanoCliFolderBrowseButton;
    private javax.swing.JButton cssNanoCliPathBrowseButton;
    private javax.swing.JTextField cssNanoCliPathTextField;
    private javax.swing.JLabel cssNanoLabel;
    private javax.swing.JLabel extLabel;
    protected javax.swing.JEditorPane headerEditorPaneCSS;
    private javax.swing.JLabel headerLabelCSS;
    private javax.swing.JScrollPane headerScrollPaneCSS;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JCheckBox newCSSFile;
    public javax.swing.JTextField preExtensionCSS;
    public javax.swing.JLabel preExtensionCSS_Label;
    // End of variables declaration//GEN-END:variables

    void fireChange() {
        changeSupport.fireChange();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireChange();
    }

    private final class DefaultDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            processUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            processUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            processUpdate();
        }

        private void processUpdate() {
            fireChange();
        }

    }
}
