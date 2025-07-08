package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Modelo.UtilTablasActividad;
import Modelo.UtilTablasMonitor;
import Modelo.UtilTablasSocio;
import Vista.JPanelActividades;
import Vista.JPanelMonitor;
import Vista.JPanelPrincipal;
import Vista.JPanelSocio;
import Vista.VistaMensajes;
import Vista.VistaPrincipal;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.persistence.Query;
import javax.swing.JPanel;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ControladorPrincipal implements ActionListener {

    private Transaction tr;
    private Session sesion;
    private SessionFactory sf;
    private VistaMensajes v;
    private SocioDAO s;
    private MonitorDAO m;
    private VistaPrincipal vPrincipal;
    private JPanelPrincipal pPrincipal;
    private JPanelMonitor vMonitor;
    private JPanelSocio vSocio;
    private JPanelActividades vActividad;
    private UtilTablasSocio uTablasSocio;
    private UtilTablasSocio uTablasSocio2;
    private UtilTablasMonitor uTablasMonitor;
    private UtilTablasActividad uTablasActividad;
    private ControladorGestionMonitor cGestionMonitor;
    private ControladorGestionSocio cGestionSocio;
    private ControladorGestionActividades cGestionActividad;

    private void muestraPanel(JPanel panel) {
        pPrincipal.setVisible(false);
        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vActividad.setVisible(false);
        panel.setVisible(true);
    }

    private ArrayList<Monitor> pideMonitores() throws Exception {

        ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitor(sesion);
        return lMonitores;
    }

    private ArrayList<Socio> pideSocios() throws Exception {

        ArrayList<Socio> lSocios = SocioDAO.listaSocioConHQL(sesion);
        return lSocios;
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Gestión de Monitores":
                muestraPanel(vMonitor);
                uTablasMonitor.dibujarTablaMonitores(vMonitor);
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                try {
                    ArrayList<Monitor> lMonitores = pideMonitores();
                    tr.commit();
                    uTablasMonitor.vaciarTablaMonitores();
                    uTablasMonitor.rellenarTablaMonitores(lMonitores);
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), ex.getCause().getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            
            case "Gestión de Socios":
                muestraPanel(vSocio);
                uTablasSocio.dibujarTablaSocios(vSocio);
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                try {
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasSocio.vaciarTablaSocios();
                    uTablasSocio.rellenarTablaSocios(lSocios);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), ex.getCause().getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
                
            case "Gestión de Actividades":
                muestraPanel(vActividad);
                uTablasSocio2.dibujarTablaSocios(vActividad);
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                try {
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasSocio2.vaciarTablaSocios();
                    uTablasSocio2.rellenarTablaSocios(lSocios);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), ex.getCause().getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;    
                
            case "Salir de la aplicación":
                vPrincipal.dispose();
                System.exit(0);
                break;
            case "Pág. Principal":
                muestraPanel(pPrincipal);
                break;
        }
    }

    private void addListeners() {
        vPrincipal.PágPrincipal.addActionListener(this);
        vPrincipal.jMenuGestionActividades.addActionListener(this);        
        vPrincipal.jMenuGestionMonitores.addActionListener(this);
        vPrincipal.jMenuGestionSocios.addActionListener(this);
        vPrincipal.jMenuSalirAplicacion.addActionListener(this);
    }

    public ControladorPrincipal(SessionFactory sesionFactory) {

        sf = sesionFactory;
        v = new VistaMensajes();
        s = new SocioDAO();
        m = new MonitorDAO();
        int numero;
        vPrincipal = new VistaPrincipal();
        pPrincipal = new JPanelPrincipal();
        vMonitor = new JPanelMonitor();
        vSocio = new JPanelSocio();
        vActividad = new JPanelActividades();
        uTablasMonitor = new UtilTablasMonitor(vMonitor);
        uTablasSocio = new UtilTablasSocio(vSocio);
        uTablasSocio2 = new UtilTablasSocio(vActividad);
        uTablasActividad = new UtilTablasActividad();
        cGestionMonitor = new ControladorGestionMonitor(sf,vMonitor,uTablasMonitor);
        cGestionSocio = new ControladorGestionSocio(sf, vSocio, uTablasSocio);
        cGestionActividad = new ControladorGestionActividades(sf, vActividad, uTablasActividad);
        addListeners();

        vPrincipal.getContentPane().setLayout(new CardLayout());
        vPrincipal.add(pPrincipal);
        vPrincipal.add(vMonitor);
        vPrincipal.add(vSocio);
        vPrincipal.add(vActividad);

        vPrincipal.setLocationRelativeTo(null);
        vPrincipal.setVisible(true);
        pPrincipal.setVisible(true);
        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vActividad.setVisible(false);
    }

}
