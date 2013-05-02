/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.protege.browser;

import com.wordpress.erenha.protege.browser.model.StaticVariabel;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.StringWriter;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.browser.MozillaExecutor;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import ru.atomation.jbrowser.impl.JBrowserBuilder;
import ru.atomation.jbrowser.impl.JBrowserCanvas;
import ru.atomation.jbrowser.impl.JBrowserComponent;
import ru.atomation.jbrowser.impl.JComponentFactory;
import ru.atomation.jbrowser.interfaces.BrowserAdapter;
import ru.atomation.jbrowser.interfaces.BrowserManager;

/**
 *
 * @author Hindarwan
 */
public class BrowserView extends AbstractOWLViewComponent {

    private JPanel northPanel;
    private JPanel southPanel;
    private JTextField addressField;
    private JButton goButton;
    private JButton inspectButton;
    private JPanel westPanel;
    private JList extractedList;
    private DefaultListModel listModel;
    private JBrowserComponent<?> browser;

    @Override
    protected void initialiseOWLView() throws Exception {

        westPanel = new JPanel();
//        if (StaticVariabel.getListModel() == null) {
//            listModel = new DefaultListModel();
//        }
//        StaticVariabel.setListModel(listModel);
//        listModel.addElement("Jane Doe");
//        listModel.addElement("John Smith");
//        listModel.addElement("Kathy Green");

        extractedList = new JList();
        extractedList = new JList(StaticVariabel.getListModel());
        extractedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        extractedList.setSelectedIndex(0);
        JScrollPane listScrollPane = new JScrollPane(extractedList);
        westPanel.add(listScrollPane);

        //right panel
        northPanel = new JPanel();
        southPanel = new JPanel();
        addressField = new JTextField(60);
        goButton = new JButton("Go");
        inspectButton = new JButton("Inspect");
        BrowserManager browserManager = new JBrowserBuilder().buildBrowserManager();
        JComponentFactory<Canvas> canvasFactory = browserManager.getComponentFactory(JBrowserCanvas.class);
        browser = canvasFactory.createBrowser();


        northPanel.setLayout(new FlowLayout());
        northPanel.add(addressField);
        northPanel.add(goButton);
        northPanel.add(inspectButton);

        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.setUrl(addressField.getText());
            }
        });

        inspectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                MozillaAutomation.executeJavascript(browser, "function(){var s=document.createElement('div');s.innerHTML='Loading...';s.style.color='black';s.style.padding='20px';s.style.position='fixed';s.style.zIndex='9999';s.style.fontSize='3.0em';s.style.border='2px solid black';s.style.right='40px';s.style.top='40px';s.setAttribute('class','selector_gadget_loading');s.style.background='white';document.body.appendChild(s);s=document.createElement('script');s.setAttribute('type','text/javascript');s.setAttribute('src','http://www.selectorgadget.com/stable/lib/selectorgadget.js?raw=true');document.body.appendChild(s);})();");
                MozillaExecutor.mozAsyncExec(new Runnable() {
                    @Override
                    public void run() {
                        boolean a = browser.setUrl("javascript:(function() { var s = document.createElement('div'); s.innerHTML = 'Loading...'; s.style.color = 'black'; s.style.padding = '5px'; s.style.margin = '5px'; s.style.position = 'fixed'; s.style.zIndex = '9999'; s.style.fontSize = '24px'; s.style.border = '1px solid black'; s.style.right = '5px'; s.style.top = '5px'; s.setAttribute('class', 'selector_gadget_loading'); s.style.background = 'white'; document.body.appendChild(s); s = document.createElement('script'); s.setAttribute('type', 'text/javascript'); s.setAttribute('src', 'http://localhost/selectorgadget/lib/selectorgadget.js?raw=true'); document.body.appendChild(s); })();");
                        System.out.println("execute JS selector : " + a);
                    }
                });
            }
        });


        browser.addBrowserListener(new BrowserAdapter() {
            @Override
            public void onSetUrlbarText(String url) {
                super.onSetUrlbarText(url); //To change body of generated methods, choose Tools | Templates.
                addressField.setText(url);
            }
        });

        browser.getComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                MozillaExecutor.mozAsyncExec(new Runnable() {
                    @Override
                    public void run() {
//                        listModel.clear();
                        StaticVariabel.listModel_clear();
                        org.jsoup.nodes.Document doc = Jsoup.parse(html());
                        String selector = doc.select("#xxx").attr("value");
                        System.out.println("selector = \"" + selector + "\"");
                        System.out.println("__________________________________________________");

                        if (!selector.trim().equals("")) {
                            try {
                                String[] split = selector.split(",");
                                for (String string : split) {
                                    Elements select = doc.select(string);
                                    for (Element e : select) {
                                        System.out.println(e.text());
//                                        listModel.addElement(e.text());
                                        StaticVariabel.listModel_addElement(e.text());
                                    }
                                }
                            } catch (Exception ex) {
                                Elements select = doc.select(selector);
                                for (Element e : select) {
                                    System.out.println(e.text());
//                                    listModel.addElement(e.text());
                                    StaticVariabel.listModel_addElement(e.text());
                                }
                            }
                            System.out.println("__________________________________________________");
                        } else {
                            System.out.println("No selector");
                        }
                    }

                    private String html() {
                        StringWriter sw = new StringWriter();
                        try {
                            org.w3c.dom.Document document = browser.getDocument();
                            DOMSource domSource = new DOMSource(document);
                            StreamResult result = new StreamResult(sw);
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer transformer = tf.newTransformer();
                            transformer.transform(domSource, result);

                        } catch (TransformerConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        } catch (TransformerFactoryConfigurationError e) {
                            e.printStackTrace();
                        }
                        return sw.getBuffer().toString();
                    }
                });
            }
        });



        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(browser.getComponent(), BorderLayout.CENTER);
//        add(southPanel, BorderLayout.SOUTH);
//        add(westPanel, BorderLayout.WEST);

        browser.setUrl("file:///G:/git/selectorgadget/sites/test/testCSSTokenizing.html");
    }

    @Override
    protected void disposeOWLView() {
    }
}
