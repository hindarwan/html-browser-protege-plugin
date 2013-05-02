/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.protege.browser.model;

import javax.swing.DefaultListModel;

/**
 *
 * @author Hindarwan
 */
public class StaticVariabel {
    private static DefaultListModel listModel = new DefaultListModel();
    
    public static DefaultListModel getListModel() {
        return listModel;
    }

    public static void setListModel(DefaultListModel listModel) {
        StaticVariabel.listModel = listModel;
    }
    
    public static void listModel_addElement(Object o){
        listModel.addElement(o);
    }
    
    public static void listModel_clear(){
        listModel.clear();
    }
        
}
