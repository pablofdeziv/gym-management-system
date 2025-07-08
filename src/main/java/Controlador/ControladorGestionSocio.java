/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Socio;
import Modelo.SocioDAO;
import Modelo.UtilTablasSocio;
import Vista.JDialogActualizaSocio;
import Vista.JDialogNuevoSocio;
import Vista.JPanelSocio;
import Vista.VistaMensajes;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author pablo
 */
public class ControladorGestionSocio implements ActionListener {

    private Transaction tr;
    private Session sesion;
    private SessionFactory sf;
    private JPanelSocio vSocio;
    private JDialogNuevoSocio vCRUDSocio;
    private JDialogActualizaSocio vCRUDActualizaSocio;
    private VistaMensajes v;
    private SimpleDateFormat formatoFecha;
    private Date fechaActual;
    private UtilTablasSocio uTablasSocio;

    private void addListeners() {
        vSocio.jAltaSocio.addActionListener(this);
        vSocio.jBajaSocio.addActionListener(this);
        vSocio.jActualizaSocio.addActionListener(this);
        vCRUDSocio.NuevoSocioInsertar.addActionListener(this);
        vCRUDSocio.NuevoSocioCancelar.addActionListener(this);
        vCRUDActualizaSocio.ActualizaSocioActualizar.addActionListener(this);
        vCRUDActualizaSocio.ActualizaSocioCancelar.addActionListener(this);
        vSocio.jButtonBuscar.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Alta Socio":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                vaciarCampos();
                vCRUDSocio.NuevoSocioCod.setText(nuevoCodigoSocio());
                try {
                    tr.commit();
                    vCRUDSocio.setVisible(true);
                } catch (Exception ex) {
                    tr.rollback();
                    v.mensajeConsola(ex.getMessage(), ex.getCause().getMessage());
                } finally {
                    if (sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Baja Socio":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                int socioselec = vSocio.jTableSocio.getSelectedRow();
                try {
                    if (socioselec != -1) {

                        String cod = (String) vSocio.jTableSocio.getValueAt(socioselec, 0);
                        String nombre = (String) vSocio.jTableSocio.getValueAt(socioselec, 1);
                        String dni = (String) vSocio.jTableSocio.getValueAt(socioselec, 2);
                        String fechaNaString = (String) vSocio.jTableSocio.getValueAt(socioselec, 3);
                        String telefono = (String) vSocio.jTableSocio.getValueAt(socioselec, 4);
                        String correo = (String) vSocio.jTableSocio.getValueAt(socioselec, 5);
                        String fechaEnString = (String) vSocio.jTableSocio.getValueAt(socioselec, 6);
                        char categoria = (char) vSocio.jTableSocio.getValueAt(socioselec, 7);

                        int opcion = v.Dialogo("¿Seguro que quieres dar de baja a " + nombre + "?");
                        if (opcion == JOptionPane.YES_OPTION) {
                            SocioDAO.eliminaSocio(sesion, new Socio(cod, nombre, dni, fechaNaString, telefono, correo, fechaEnString, categoria));
                            actualizaTablas();
                        }
                    } else {
                        v.mensajeConsola("Selecciona socio de la lista", "");
                    }
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
            case "Actualizacion Socio":
                socioselec = vSocio.jTableSocio.getSelectedRow();

                sesion = sf.openSession();
                tr = sesion.beginTransaction();

                try {

                    if (socioselec != -1) {
                        vCRUDActualizaSocio.ActualizaSocioCod.setText((String) vSocio.jTableSocio.getValueAt(socioselec, 0));
                        vCRUDActualizaSocio.ActualizaSocioNom.setText((String) vSocio.jTableSocio.getValueAt(socioselec, 1));
                        vCRUDActualizaSocio.ActualizaSocioDNI.setText((String) vSocio.jTableSocio.getValueAt(socioselec, 2));
                        String fechaNaString = (String) vSocio.jTableSocio.getValueAt(socioselec, 3);
                        vCRUDActualizaSocio.ActualizaSocioFna.setDate(formatoFecha.parse(fechaNaString));
                        vCRUDActualizaSocio.ActualizaSocioTel.setText((String) vSocio.jTableSocio.getValueAt(socioselec, 4));
                        vCRUDActualizaSocio.ActualizaSocioCorreo.setText((String) vSocio.jTableSocio.getValueAt(socioselec, 5));
                        String fechaEnString = (String) vSocio.jTableSocio.getValueAt(socioselec, 6);
                        vCRUDActualizaSocio.ActualizaSocioFen.setDate(formatoFecha.parse(fechaEnString));
                        char cat = (char) vSocio.jTableSocio.getValueAt(socioselec, 7);
                        vCRUDActualizaSocio.ActualizaSocioCategoria.setSelectedItem((String.valueOf(cat)));

                        tr.commit();
                        vCRUDActualizaSocio.setVisible(true);
                    } else {
                        v.mensajeConsola("Selecciona socio de la lista", "");
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

                    String cod = vCRUDSocio.NuevoSocioCod.getText();
                    String nombre = vCRUDSocio.NuevoSocioNom.getText();
                    String dni = vCRUDSocio.NuevoSocioDNI.getText();
                    String telefono = vCRUDSocio.NuevoSocioTel.getText();
                    String correo = vCRUDSocio.NuevoSocioCorreo.getText();
                    String categoria = (String) vCRUDSocio.NuevoSocioCategoria.getSelectedItem();
                    Date fen = vCRUDSocio.NuevoSocioFen.getDate();
                    String fechaEnString = "";
                    if (fen != null) {
                        fechaEnString = formatoFecha.format(fen);
                    }
                    Date fna = vCRUDSocio.NuevoSocioFna.getDate();
                    String fechaNaString = "";
                    if (fna != null) {
                        fechaNaString = formatoFecha.format(fna);
                    }

                    String fechaActualS = formatoFecha.format(fechaActual);

                    if (dni == "") {
                        v.mensajeConsola("El DNI esta vacio, introduzca uno para insertar al socio", "NOT_DNI");
                    } else if (!validezDNI(dni)) {
                        v.mensajeConsola("El formato del DNI es incorrecto, introduzcalo correctamente para insertar al socio", "NOT_VALID_DNI");
                    } else if (nombre == "") {
                        v.mensajeConsola("El nombre esta vacio, introduzca uno para insertar al socio", "NOT_NAME");
                    } else if (fen.after(fechaActual)) {
                        v.mensajeConsola("La fecha de entrada seleccionada es posterior a la actual, introduzca una valida para insertar al socio", "NOT_VALID_DATE");
                    } else if (fna.after(fechaActual)) {
                        v.mensajeConsola("La fecha de nacimiento seleccionada es posterior a la actual, introduzca una valida para insertar al socio", "NOT_VALID_DATE");
                    } else if (!validarFormatoCorreo(correo)) {
                        v.mensajeConsola("El formato del correo es incorrecto, introduzca uno valido para insertar al socio", "NOT_VALID_MAIL");
                    } else if (telefono.length() != 9) {
                        v.mensajeConsola("El numero de telefono actual es invalido, introduzca uno correcto para insertar al monitor", "NOT_VALID_TELF");
                    } else if (!esMayorDe18(fechaNaString, fechaActualS)) {
                        v.mensajeConsola("El socio es menor de 18, solo se pueden dar de alta adultos", "NOT_VALID_AGE");
                    } else {
                        SocioDAO.insertaSocio(sesion, new Socio(cod, nombre, dni, fechaNaString, telefono, correo, fechaEnString, categoria.charAt(0)));
                        actualizaTablas();
                        v.mensajeConsola("Socio introducido correctamente");
                        vCRUDSocio.dispose();
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
                    String cod = vCRUDActualizaSocio.ActualizaSocioCod.getText();
                    String nombre = vCRUDActualizaSocio.ActualizaSocioNom.getText();
                    String dni = vCRUDActualizaSocio.ActualizaSocioDNI.getText();
                    String telefono = vCRUDActualizaSocio.ActualizaSocioTel.getText();
                    String correo = vCRUDActualizaSocio.ActualizaSocioCorreo.getText();
                    String categoria = (String) vCRUDActualizaSocio.ActualizaSocioCategoria.getSelectedItem();
                    Date fen = vCRUDActualizaSocio.ActualizaSocioFen.getDate();
                    String fechaEnString = "";
                    if (fen != null) {
                        fechaEnString = formatoFecha.format(fen);
                    }
                    Date fna = vCRUDActualizaSocio.ActualizaSocioFna.getDate();
                    String fechaNaString = "";
                    if (fna != null) {
                        fechaNaString = formatoFecha.format(fna);
                    }

                    String fechaActualS = formatoFecha.format(fechaActual);

                    if (dni == "") {
                        v.mensajeConsola("El DNI esta vacio, introduzca uno para insertar al socio", "NOT_DNI");
                    } else if (!validezDNI(dni)) {
                        v.mensajeConsola("El formato del DNI es incorrecto, introduzcalo correctamente para insertar al socio", "NOT_VALID_DNI");
                    } else if (nombre == "") {
                        v.mensajeConsola("El nombre esta vacio, introduzca uno para insertar al socio", "NOT_NAME");
                    } else if (fen.after(fechaActual)) {
                        v.mensajeConsola("La fecha de entrada seleccionada es posterior a la actual, introduzca una valida para insertar al socio", "NOT_VALID_DATE");
                    } else if (fna.after(fechaActual)) {
                        v.mensajeConsola("La fecha de nacimiento seleccionada es posterior a la actual, introduzca una valida para insertar al socio", "NOT_VALID_DATE");
                    } else if (telefono.length() != 9) {
                        v.mensajeConsola("El numero de telefono actual es invalido, introduzca uno correcto para insertar al monitor", "NOT_VALID_TELF");
                    } else if (!validarFormatoCorreo(correo)) {
                        v.mensajeConsola("El formato del correo es incorrecto, introduzca uno valido para insertar al socio", "NOT_VALID_MAIL");
                    } else if (!esMayorDe18(fechaNaString, fechaActualS)) {
                        v.mensajeConsola("El socio es menor de 18, solo se pueden dar de alta adultos", "NOT_VALID_AGE");
                    } else {
                        SocioDAO.insertaSocio(sesion, new Socio(cod, nombre, dni, fechaNaString, telefono, correo, fechaEnString, categoria.charAt(0)));
                        actualizaTablas();
                        v.mensajeConsola("Socio introducido correctamente");
                        vCRUDActualizaSocio.dispose();
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
                vCRUDSocio.dispose();
                break;
            case "CancelarActualizar":
                vCRUDActualizaSocio.dispose();
                break;
            case "Buscar":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                String tipo = vSocio.jEleccionBusqueda.getSelectedItem().toString();
                String filtro = vSocio.jTextoFiltro.getText();
                try {
                    uTablasSocio.vaciarTablaSocios();
                    ArrayList<Socio> lSocios = listaSociosFiltro(tipo, filtro);
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
        }
    }

    public void vaciarCampos() {
        vCRUDSocio.NuevoSocioNom.setText("");
        vCRUDSocio.NuevoSocioDNI.setText("");
        vCRUDSocio.NuevoSocioTel.setText("");
        vCRUDSocio.NuevoSocioCorreo.setText("");
        vCRUDSocio.NuevoSocioCod.setText("");
        vCRUDSocio.NuevoSocioFen.setDate(fechaActual);
        vCRUDSocio.NuevoSocioFen.setDate(fechaActual);
    }

    private String nuevoCodigoSocio() {
        ArrayList<String> valor = SocioDAO.ultimoCodSocio(sesion);
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

    public static boolean esMayorDe18(String fechaNacimientoS, String fechaActualS) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaNacimiento = sdf.parse(fechaNacimientoS);
            Date fechaActual = sdf.parse(fechaActualS);

            Calendar calNacimiento = Calendar.getInstance();
            calNacimiento.setTime(fechaNacimiento);

            Calendar calActual = Calendar.getInstance();
            calActual.setTime(fechaActual);

            int aniosDiferencia = calActual.get(Calendar.YEAR) - calNacimiento.get(Calendar.YEAR);
            int mesesDiferencia = calActual.get(Calendar.MONTH) - calNacimiento.get(Calendar.MONTH);
            int diasDiferencia = calActual.get(Calendar.DAY_OF_MONTH) - calNacimiento.get(Calendar.DAY_OF_MONTH);

            if (mesesDiferencia < 0 || (mesesDiferencia == 0 && diasDiferencia < 0)) {
                aniosDiferencia--;
            }

            return aniosDiferencia >= 18;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void actualizaTablas() throws Exception {
        ArrayList<Socio> lSocios = listaSocios();
        uTablasSocio.vaciarTablaSocios();
        uTablasSocio.rellenarTablaSocios(lSocios);
    }

    private ArrayList<Socio> listaSocios() throws Exception {

        ArrayList<Socio> lSocios = SocioDAO.listaSocioConHQL(sesion);
        return lSocios;
    }
    
    private ArrayList<Socio> listaSociosFiltro(String tipo, String filtro) throws Exception {

        ArrayList<Socio> lSocios = SocioDAO.ListaSociosFiltro(sesion, tipo, filtro);
        return lSocios;
    }

    public ControladorGestionSocio(SessionFactory sesionFactory, JPanelSocio vSocio, UtilTablasSocio uTablasSocio) {

        sf = sesionFactory;
        this.vSocio = vSocio;
        this.uTablasSocio = uTablasSocio;
        v = new VistaMensajes();
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechaActual = new Date();

        vCRUDSocio = new JDialogNuevoSocio();
        vCRUDSocio.setLocationRelativeTo(null);
        vCRUDSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        vCRUDSocio.setResizable(false);
        vCRUDSocio.setVisible(false);

        vCRUDActualizaSocio = new JDialogActualizaSocio();
        vCRUDActualizaSocio.setLocationRelativeTo(null);
        vCRUDActualizaSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        vCRUDActualizaSocio.setResizable(false);
        vCRUDActualizaSocio.setVisible(false);
        addListeners();

    }

}
