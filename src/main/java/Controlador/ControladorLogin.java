package Controlador;

import Config.HibernateUtilMariaDB;
import Config.HibernateUtilOracle;
import Vista.VistaInfoBD;
import Vista.VistaLogin;
import Vista.VistaMensajes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.hibernate.SessionFactory;

public class ControladorLogin implements ActionListener {

    private SessionFactory connect;
    private VistaMensajes vMensaje = null;
    private VistaInfoBD vInfoBD = null;
    private VistaLogin vLogin = null;

    private void conectarBD() {
        boolean conexioncorrecta = false;
        while (!conexioncorrecta) {
            try {
                //vMensaje.mensajeConsola("Introduce 1 para Oracle. 2 para MariaDB");
                //Scanner sc = new Scanner(System.in);
                int num; //= sc.nextInt();
                num = vLogin.EleccionServidor.getSelectedIndex();
                if (num == 1) {
                    connect = HibernateUtilOracle.getSessionFactory();
                } else if (num == 0) {
                    connect = HibernateUtilMariaDB.getSessionFactory();
                }conexioncorrecta = true;
                vMensaje.mensajeConsola("Conexión Correcta con Hibernate");
                //JOptionPane.showMessageDialog(vLogin, "La conexion ha tenido exito");
            } catch (ExceptionInInitializerError e) {
                Throwable cause = e.getCause();
                System.out.println("Error en la conexión. Revise el fichero .cfg.xml: " + cause.getMessage());
                //JOptionPane.showMessageDialog(vLogin, "Error en la conexion. Revise el fichero .cfg.xml " + cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
                vMensaje.mensajeConsola("Error en la conexión. Revise el fichero .cfg.xml:", cause.getMessage());
            }
        }

    }

    private void addListeners() {
        vLogin.BotonConectar.addActionListener(this);
        vLogin.BotonSalirEleccion.addActionListener(this);
    }

    public ControladorLogin() {
        vMensaje = new VistaMensajes();
        vInfoBD = new VistaInfoBD();
        vLogin = new VistaLogin();

        addListeners();

        vLogin.setLocationRelativeTo(null);
        vLogin.setVisible(true);

        vLogin.EleccionServidor.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "Conectar":
                conectarBD();
                vLogin.dispose();
                ControladorPrincipal controladorP = new ControladorPrincipal(connect);
                break;

            case "SalirDialogoConexion":
                vLogin.dispose();
                System.exit(0);
                break;

        }
    }

}
