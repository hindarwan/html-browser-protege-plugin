/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.protege.browser;

import com.wordpress.erenha.protege.browser.model.StaticVariabel;
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

/**
 *
 * @author Hindarwan
 */
public class ExtractedTextView extends AbstractOWLViewComponent {

    private DefaultListModel listModel;
    private JList extractedList;

    @Override
    protected void initialiseOWLView() throws Exception {
//        if (StaticVariabel.getListModel() == null) {
//            listModel = new DefaultListModel();
//        }
//        StaticVariabel.setListModel(listModel);
//        listModel.addElement("Jane Doe");
//        listModel.addElement("John Smith");
//        listModel.addElement("Kathy Green");
        extractedList = new JList();
        extractedList = new JList(StaticVariabel.getListModel());
//        extractedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        extractedList.setSelectedIndex(0);
        JScrollPane listScrollPane = new JScrollPane(extractedList);
        setLayout(new BorderLayout());
        add(listScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void disposeOWLView() {
    }
}
