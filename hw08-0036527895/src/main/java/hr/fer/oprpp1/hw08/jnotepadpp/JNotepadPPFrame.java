package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class JNotepadPPFrame extends JFrame {

    public JNotepadPPFrame() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("JNotepad++");
        setLocation(1500, 700);
        setSize(500, 500);
        initGUI();
    }

    protected void initGUI() {
        this.getContentPane().setLayout(new BorderLayout());

        initMenuBar();
        initHeader();
        initFooter();
    }

    protected void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newDocument = new JMenuItem("New");
        JMenuItem openDocument = new JMenuItem("Open");
        JMenuItem saveDocument = new JMenuItem("Save");
        JMenuItem saveDocumentAs = new JMenuItem("Save as...");
        JMenuItem closeDocument = new JMenuItem("Close");

        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");

        JMenuItem stats = new JMenuItem("Stats");
        JMenuItem exit = new JMenuItem("Exit");

        fileMenu.add(newDocument);
        fileMenu.add(openDocument);
        fileMenu.add(saveDocument);
        fileMenu.add(saveDocumentAs);
        fileMenu.add(closeDocument);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        helpMenu.add(stats);
        helpMenu.add(exit);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        this.setJMenuBar(menuBar);
    }

    protected void initHeader() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(new TabComponent());

        this.getContentPane().add(tabbedPane);
    }

    protected void initFooter() {

    }


}
