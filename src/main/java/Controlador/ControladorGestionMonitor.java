/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static Controlador.ControladorGestionSocio.validarFormatoCorreo;
import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.UtilTablasMonitor;
import Vista.JDialogActualizaMonitor;
import Vista.JDialogNuevoMonitor;
import Vista.JPanelMonitor;
import Vista.VistaMensajes;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author pablo
 */
public class ControladorGestionMonitor implements ActionListener {

    private Transaction tr;
    private Session sesion;
    private SessionFactory sf;
    private JPanelMonitor vMonitor;
    private JDialogNuevoMonitor vCRUDMonitor;
    private JDialogActualizaMonitor vCRUDActualizaMonitor;
    private VistaMensajes v;
    private SimpleDateFormat formatoFecha;
    private Date fechaActual;
    private UtilTablasMonitor uTablasMonitor;

    private void addListeners() {
        vMonitor.jAltaMonitor.addActionListener(this);
        vMonitor.jBajaMonitor.addActionListener(this);
        vMonitor.jActualizarMonitor.addActionListener(this);
        vCRUDMonitor.NuevoMonitorInsertar.addActionListener(this);
        vCRUDMonitor.NuevoMonitorCancelar.addActionListener(this);
        vCRUDActualizaMonitor.ActualizaMonitorActualiza.addActionListener(this);
        vCRUDActualizaMonitor.ActualizaMonitorCancelar.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Alta Monitor":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                vaciarCampos();
                vCRUDMonitor.NuevoMonitorCod.setText(nuevoCodigoMonitor());
                vMonitor.jActualizarMonitor.setEnabled(true);
                try {
                    tr.commit();
                    vCRUDMonitor.setVisible(true);
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), ex.getCause().getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Baja Monitor":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                int monitorselec = vMonitor.jTableMonitor.getSelectedRow();
                try {
                    if (monitorselec != -1) {

                        String cod = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 0);
                        String nombre = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 1);
                        String dni = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 2);
                        String telefono = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 3);
                        String correo = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 4);
                        String nick = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 6);
                        String fechaString = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 5);

                        int opcion = v.Dialogo("¿Seguro que quieres dar de baja a " + nombre + "?");
                        if (opcion == JOptionPane.YES_OPTION) {
                            MonitorDAO.eliminaMonitor(sesion, new Monitor(cod, nombre, dni, telefono, correo, fechaString, nick));
                            actualizaTablas();
                        }
                    }
                    else
                        v.mensajeConsola("Selecciona un monitor de la lista", "");
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Actualizacion Monitor":
                
                monitorselec = vMonitor.jTableMonitor.getSelectedRow();

                sesion = sf.openSession();
                tr = sesion.beginTransaction();

                try {
                    
                    if (monitorselec != -1) {

                        vCRUDActualizaMonitor.ActualizaMonitorCod.setText((String) vMonitor.jTableMonitor.getValueAt(monitorselec, 0));
                        vCRUDActualizaMonitor.ActualizaMonitorNom.setText((String) vMonitor.jTableMonitor.getValueAt(monitorselec, 1));
                        vCRUDActualizaMonitor.ActualizaMonitorDNI.setText((String) vMonitor.jTableMonitor.getValueAt(monitorselec, 2));
                        vCRUDActualizaMonitor.ActualizaMonitorTel.setText((String) vMonitor.jTableMonitor.getValueAt(monitorselec, 3));
                        vCRUDActualizaMonitor.ActualizaMonitorCorreo.setText((String) vMonitor.jTableMonitor.getValueAt(monitorselec, 4));
                        String fechaString = (String) vMonitor.jTableMonitor.getValueAt(monitorselec, 5);
                        vCRUDActualizaMonitor.ActualizaMonitorFen.setDate(formatoFecha.parse(fechaString));
                        vCRUDActualizaMonitor.ActualizaMonitorNick.setText((String) vMonitor.jTableMonitor.getValueAt(monitorselec, 6));

                        tr.commit();

                        vCRUDActualizaMonitor.setVisible(true);
                    } else {
                        v.mensajeConsola("Selecciona un monitor de la lista", "");
                        tr.commit();
                    }
                    
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), ex.getCause().getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }

                }
                break;
            case "Insertar":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();

                try {

                    String cod = vCRUDMonitor.NuevoMonitorCod.getText();
                    String nombre = vCRUDMonitor.NuevoMonitorNom.getText();
                    String dni = vCRUDMonitor.NuevoMonitorDNI.getText();
                    String telefono = vCRUDMonitor.NuevoMonitorTel.getText();
                    String correo = vCRUDMonitor.NuevoMonitorCorreo.getText();
                    String nick = vCRUDMonitor.NuevoMonitorNick.getText();
                    Date fen = vCRUDMonitor.NuevoMonitorFen.getDate();
                    String fechaString = "";
                    if (fen != null) {
                        fechaString = formatoFecha.format(fen);
                    }
                    if (dni == "") {
                        v.mensajeConsola("El DNI esta vacio, introduzca uno para insertar al monitor", "NOT_DNI");
                    } else if (!validezDNI(dni)) {
                        v.mensajeConsola("El formato del DNI es incorrecto, introduzcalo correctamente para insertar al monitor", "NOT_VALID_DNI");
                    } else if (nombre == "") {
                        v.mensajeConsola("El nombre esta vacio, introduzca uno para insertar al monitor", "NOT_NAME");
                    } else if (fen.after(fechaActual)) {
                        v.mensajeConsola("La fecha seleccionada es posterior a la actual, introduzca una valida para insertar al monitor", "NOT_VALID_DATE");
                    } else if(telefono.length() != 9){
                         v.mensajeConsola("El numero de telefono actual es invalido, introduzca uno correcto para insertar al monitor", "NOT_VALID_TELF");
                    } else if (!validarFormatoCorreo(correo)) {
                        v.mensajeConsola("El formato del correo es incorrecto, introduzca uno valido para insertar al socio", "NOT_VALID_MAIL"); 
                    } else {
                        MonitorDAO.insertaMonitor(sesion, new Monitor(cod, nombre, dni, telefono, correo, fechaString, nick));
                        actualizaTablas();
                        v.mensajeConsola("Monitor introducido correctamente");
                        vCRUDMonitor.dispose();
                    }
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), "");
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Actualizar":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                try {
                    String cod = vCRUDActualizaMonitor.ActualizaMonitorCod.getText();
                    String nombre = vCRUDActualizaMonitor.ActualizaMonitorNom.getText();
                    String dni = vCRUDActualizaMonitor.ActualizaMonitorDNI.getText();
                    String telefono = vCRUDActualizaMonitor.ActualizaMonitorTel.getText();
                    String correo = vCRUDActualizaMonitor.ActualizaMonitorCorreo.getText();
                    String nick = vCRUDActualizaMonitor.ActualizaMonitorNick.getText();
                    Date fen = vCRUDActualizaMonitor.ActualizaMonitorFen.getDate();
                    String fechaString = "";
                    if (fen != null) {
                        fechaString = formatoFecha.format(fen);
                    }
                    if (dni == "") {
                        v.mensajeConsola("El DNI esta vacio, introduzca uno para insertar al monitor", "NOT_DNI");
                    } else if (!validezDNI(dni)) {
                        v.mensajeConsola("El formato del DNI es incorrecto, introduzcalo correctamente para insertar al monitor", "NOT_VALID_DNI");
                    } else if (nombre == "") {
                        v.mensajeConsola("El nombre esta vacio, introduzca uno para insertar al monitor", "NOT_NAME");
                    } else if (fen.after(fechaActual)) {
                        v.mensajeConsola("La fecha seleccionada es posterior a la actual, introduzca una valida para insertar al monitor", "NOT_VALID_DATE");
                    } else if(telefono.length() != 9){
                         v.mensajeConsola("El numero de telefono actual es invalido, introduzca uno correcto para insertar al monitor", "NOT_VALID_TELF");
                    } else if (!validarFormatoCorreo(correo)) {
                        v.mensajeConsola("El formato del correo es incorrecto, introduzca uno valido para insertar al socio", "NOT_VALID_MAIL");
                    } else {
                        MonitorDAO.insertaMonitor(sesion, new Monitor(cod, nombre, dni, telefono, correo, fechaString, nick));
                        actualizaTablas();
                        v.mensajeConsola("Monitor actualizado correctamente");
                        vCRUDActualizaMonitor.dispose();
                    }
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
            case "CancelarInsertar":
                vCRUDMonitor.dispose();
                break;
            case "CancelarActualizar":
                vCRUDActualizaMonitor.dispose();
                break;
        }
    }

    public void vaciarCampos() {
        vCRUDMonitor.NuevoMonitorNom.setText("");
        vCRUDMonitor.NuevoMonitorDNI.setText("");
        vCRUDMonitor.NuevoMonitorTel.setText("");
        vCRUDMonitor.NuevoMonitorCorreo.setText("");
        vCRUDMonitor.NuevoMonitorCod.setText("");
        vCRUDMonitor.NuevoMonitorNick.setText("");
        vCRUDMonitor.NuevoMonitorFen.setDate(fechaActual);
    }

    private String nuevoCodigoMonitor() {
        ArrayList<String> valor = MonitorDAO.ultimoCodMonitor(sesion);
        String codigo = valor.get(0);

        String prefijo = codigo.substring(0, codigo.length() - 3);
        int numero = Integer.parseInt(codigo.substring(codigo.length() - 3));
        numero++;

        codigo = prefijo + String.format("%03d", numero);

        return codigo;
    }

    public static boolean validezDNI(String dni) {

        String patronDNIenEspana = "\\d{8}[A-HJ-NP-TV-Z]";

        Pattern patron = Pattern.compile(patronDNIenEspana);

        Matcher emparejador = patron.matcher(dni);

        return emparejador.matches();
    }
    
    public static boolean validarFormatoCorreo(String correo) {
        // Expresión regular para validar el formato mínimo de xxx@xxx
        String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{3}$";
        
        Pattern pattern = Pattern.compile(regex);
        
        Matcher matcher = pattern.matcher(correo);
        
        return matcher.matches();
    }

    public void actualizaTablas() throws Exception {
        ArrayList<Monitor> lMonitores = listaMonitores();
        uTablasMonitor.vaciarTablaMonitores();
        uTablasMonitor.rellenarTablaMonitores(lMonitores);
    }

    private ArrayList<Monitor> listaMonitores() throws Exception {

        ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitor(sesion);
        return lMonitores;
    }

    public ControladorGestionMonitor(SessionFactory sesionFactory, JPanelMonitor vMonitor, UtilTablasMonitor uTablasMonitor) {

        sf = sesionFactory;
        this.vMonitor = vMonitor;
        this.uTablasMonitor = uTablasMonitor;
        v = new VistaMensajes();
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechaActual = new Date();

        vCRUDMonitor = new JDialogNuevoMonitor();
        vCRUDMonitor.setLocationRelativeTo(null);
        vCRUDMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        vCRUDMonitor.setResizable(false);
        vCRUDMonitor.setVisible(false);

        vCRUDActualizaMonitor = new JDialogActualizaMonitor();
        vCRUDActualizaMonitor.setLocationRelativeTo(null);
        vCRUDActualizaMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        vCRUDActualizaMonitor.setResizable(false);
        vCRUDActualizaMonitor.setVisible(false);
        
        vMonitor.jActualizarMonitor.setEnabled(false);

        addListeners();

    }

}
