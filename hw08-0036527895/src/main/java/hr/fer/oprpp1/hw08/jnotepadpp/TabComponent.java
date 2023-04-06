package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class TabComponent extends JComponent {

    private String name;
    private ImageIcon modified;
    private ImageIcon unmodified;

    public TabComponent() {
        this("(unnamed)");
    }

    public TabComponent(String name) {
        this.name = name;
        try {
            unmodified = getUnmodified();
            modified = getModified();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File not found!");
            System.exit(0);
        }
    }

    private ImageIcon getUnmodified() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("green.png");
        if(is == null) throw new IOException("File \"/icons/green.png\" not found!");
        byte[] bytes = is.readAllBytes();
        is.close();
        return new ImageIcon(bytes);
    }

    private ImageIcon getModified() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("red.png");
        if(is == null) throw new IOException("File \"/icons/red.png\" not found!");
        byte[] bytes = is.readAllBytes();
        is.close();
        return new ImageIcon(bytes);
    }
}
