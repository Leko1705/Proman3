package proman.diagrams.graph.nodes;

import proman.view.style.Styles;
import proman.view.style.TextStyle;
import proman.utils.SwingUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public abstract class ImageNode implements Node {

    private final View view;

    protected ImageNode(Path path, int width, int height) {
        this(path, width, height, "");
    }

    protected ImageNode(Path path, int width, int height, String initialText) {
        this.view = new View(path, width, height, initialText);
    }

    public abstract NodeType<?> getType();

    public abstract Node copy();

    public TextStyle getTextStyle() {
        return Styles.DEFAULT.getTextStyle();
    }

    public void setImage(Image image) {
        view.image = image;
    }

    public Image getImage() {
        return view.image;
    }

    public void setText(String text) {
        view.textPane.setText(text);
    }

    public String getText() {
        return view.textPane.getText();
    }

    @Override
    public Component getView() {
        return view;
    }


    private class View extends JPanel {

        private final JTextPane textPane = new JTextPane();

        private Image image;

        public View(Path path, int width, int height, String subText) {
            setLayout(new BorderLayout());
            setSize(width, height);

            try {
                image = ImageIO.read(path.toFile());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            add(textPane, BorderLayout.SOUTH);

            initTextPane(subText);
            setUpTextListeners(this);
        }

        private void setUpTextListeners(Component component) {

            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        textPane.setEnabled(true);
                        textPane.requestFocusInWindow();
                    }
                }
            });

            component.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    textPane.setEnabled(false);
                }
            });

            component.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        textPane.setEnabled(false);
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
                    }
                }
            });

            component.addKeyListener(new KeyAdapter() {
                private final Set<Integer> pressedKeys = new HashSet<>();

                @Override public void keyPressed(KeyEvent e) {
                    pressedKeys.add(e.getKeyCode());
                    if (pressedKeys.contains(KeyEvent.VK_ENTER) && pressedKeys.contains(KeyEvent.VK_SHIFT)){
                        textPane.setText(textPane.getText() + "\n");
                    }
                }
                @Override public void keyReleased(KeyEvent e) {
                    pressedKeys.remove(e.getKeyCode());
                }
            });
        }

        private void initTextPane(String text) {
            textPane.setText(text);
            textPane.setOpaque(false);
            textPane.setEnabled(false);
            textPane.setDisabledTextColor(Color.BLACK);

            StyledDocument documentStyle = textPane.getStyledDocument();
            SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
            documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

            SwingUtils.applyTextStyle(documentStyle, ImageNode.this.getTextStyle());

            ((AbstractDocument) textPane.getDocument()).setDocumentFilter(new DocumentFilter(){
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text.equals("\n")){
                        textPane.setEnabled(false);
                        return;
                    }
                    super.replace(fb, offset, length, text, attrs);
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight()-20, this);
        }
    }

}
