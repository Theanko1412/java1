package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;

public class JNotepadPPApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {
                    JNotepadPPFrame frame = new JNotepadPPFrame();
                    frame.setVisible(true);
                }
        );
    }

}
