import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;


public class VistaPrincipal extends JFrame {


    DecimalFormat formateador = new DecimalFormat("000.00");
    private JPanel panel;
    private JTextField Fitxer;
    private JButton Acceptar;
    private JComboBox Funcio;
    private JComboBox Algoritme;
    private JTextArea Est;
    private JScrollPane ESTADISTICAMOBIL;


    private double ratiCompressio;
    private double velocitat;
    private double midaO;
    private double midaC;
    private double temps;

    String v;
    String r;




    private static CtrlPresentacio ControladorPresentacio;

    public VistaPrincipal(){
        Algoritme.setSelectedIndex(5);
        VistaPrincipal v = this;

        Acceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Fitxer.getText().isEmpty())  JOptionPane.showMessageDialog(null, "Siusplau, ompli tots els camps");
                else {
                    try {
                        System.out.println("abans crear");
                        ControladorPresentacio = new CtrlPresentacio(v,Funcio.getSelectedIndex(),Fitxer.getText(),Algoritme.getSelectedIndex());
                        System.out.println("despres crear");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                    String aux = "- Mida original: " + midaO + " Bytes\n" + "- Mida comprimit: " + midaC + " Bytes\n" + "- Rati compressió: " + ratiCompressio + "\n" + "- Velocitat: " + v + " kBytes/s \n\n";
                    System.out.println("aux" + aux);
                    Est.append(aux);


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


    public void mostrarEstadistiques(double midaO, double midaC, double temps) {
        this.midaO = midaO;
        this.midaC = midaC;
        ratiCompressio = midaC / midaO;
        r = formateador.format(ratiCompressio);
        velocitat = ((midaO / temps)*Math.pow(10,9))/Math.pow(10,3);
        v = formateador.format(velocitat);
    }
}
