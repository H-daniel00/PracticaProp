import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class VistaPrincipal extends JFrame {


    private JPanel panel;
    private JTextField Fitxer;
    private JButton Acceptar;
    private JComboBox Funcio;
    private JComboBox Algoritme;



    private static CtrlPresentacio ControladorPresentacio;

    public VistaPrincipal(){


        Acceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Fitxer.getText().isEmpty())  JOptionPane.showMessageDialog(null, "Siusplau, ompli tots els camps");
                else {
                    try {
                        ControladorPresentacio = new CtrlPresentacio(Funcio.getSelectedIndex(),Fitxer.getText(),Algoritme.getSelectedIndex());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Compresio/Descompresio de fitxers");
        frame.setContentPane(new VistaPrincipal().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



}
