/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Socio;
import Modelo.UtilTablasActividad;
import Modelo.UtilTablasSocio;
import Vista.JDialogBajaActividad;
import Vista.JDialogNuevaActividad;
import Vista.JPanelActividades;
import Vista.VistaMensajes;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author pablo
 */
public class ControladorGestionActividades implements ActionListener {

    private Transaction tr;
    private Session sesion;
    private SessionFactory sf;
    private JPanelActividades vActividad;
    private UtilTablasActividad uTablasActividades;
    private VistaMensajes v;
    private JDialogNuevaActividad vCRUDNuevaActividad;
    private JDialogBajaActividad vCRUDBajaActividad;
    private String codsocio;

    private void addListeners() {
        vActividad.jAltaActividad.addActionListener(this);
        vActividad.jBajaActividad.addActionListener(this);
        vCRUDBajaActividad.jBajaActividades.addActionListener(this);
        vCRUDBajaActividad.jCancelarBaja.addActionListener(this);
        vCRUDNuevaActividad.jAltaActividad.addActionListener(this);
        vCRUDNuevaActividad.jCancelarAlta.addActionListener(this);
    }

    private ArrayList<Actividad> pideActividadesRestantes(String numsoc) throws Exception {

        ArrayList<Actividad> lActividades = ActividadDAO.ListaActividadesNodeSocio(sesion, ActividadDAO.ListaActividadesdeSocioConcreto(sesion, numsoc));
        return lActividades;
    }
    
    private ArrayList<Object[]> pideActividades(String numsoc) throws Exception {

        ArrayList<Object[]> lActividades = ActividadDAO.ListaActividadesdeSocioConcreto(sesion, numsoc);
        return lActividades;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Alta Actividad":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                int socioselec = vActividad.jTableSocio.getSelectedRow();
                try {
                    if (socioselec != -1) {
                        codsocio = (String) vActividad.jTableSocio.getValueAt(socioselec, 0);
                        uTablasActividades.inicializarTablaActividades(vCRUDNuevaActividad);
                        uTablasActividades.dibujarTablaActividades(vCRUDNuevaActividad);
                        ArrayList<Actividad> lActividades = pideActividadesRestantes(codsocio);
                        uTablasActividades.vaciarTablaActividades();
                        uTablasActividades.rellenarTablaActividadesAlta(lActividades);
                        
                        tr.commit();
                        vCRUDNuevaActividad.setVisible(true);
                        
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
            case "Baja Actividad":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                socioselec = vActividad.jTableSocio.getSelectedRow();
                try {

                    if (socioselec != -1) {
                        codsocio = (String) vActividad.jTableSocio.getValueAt(socioselec, 0);
                        uTablasActividades.inicializarTablaActividades(vCRUDBajaActividad);
                        uTablasActividades.dibujarTablaActividades(vCRUDBajaActividad);
                        ArrayList<Object[]> lActividades = pideActividades(codsocio);
                        uTablasActividades.vaciarTablaActividades();
                        uTablasActividades.rellenarTablaActividadesBaja(lActividades);

                        tr.commit();
                        vCRUDBajaActividad.setVisible(true);
                        
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
            case "Alta Actividades":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                int actividadselec = vCRUDNuevaActividad.jTableActividades.getSelectedRow();
                try {

                    if (actividadselec != -1) {
                        
                        String cod = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 0);
                        //String nombre = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 1);
                        //String precio = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 2);
                        //String descripcion = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 3);
                        
                        Actividad actividad = sesion.get(Actividad.class, cod);
                        Socio socio = sesion.get(Socio.class, codsocio);
                        actividad.altaSocio(socio);
                        //actualizatablas
                        v.mensajeConsola("Actividad introducida correctamente");
                        vCRUDNuevaActividad.dispose();
                        
                    } else {
                        v.mensajeConsola("Selecciona actividad de la lista", "");
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
                
            case "Baja Actividades":
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                actividadselec = vCRUDBajaActividad.jTableActividades.getSelectedRow();
                try {
                    if (actividadselec != -1) {
                        
                        String cod = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 0);
                        String nombre = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 1);
                        //String precio = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 2);
                        //String descripcion = (String) vCRUDNuevaActividad.jTableActividades.getValueAt(actividadselec, 3);
                        
                        Actividad actividad = sesion.get(Actividad.class, cod);
                        Socio socio = sesion.get(Socio.class, codsocio);
                        actividad.bajaSocio(socio);
                        
                        v.mensajeConsola("Socio dado de baja de " + nombre + " correctamente");
                        vCRUDBajaActividad.dispose();
                        
                    } else {
                        v.mensajeConsola("Selecciona actividad de la lista", "");
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
                
            case "CancelarAlta":
                vCRUDNuevaActividad.dispose();
                break;
                
            case "CancelarBaja":
                vCRUDBajaActividad.dispose();
                break;   
        }
    }
    
    public ControladorGestionActividades(SessionFactory sesionFactory, JPanelActividades vActividades, UtilTablasActividad uTablasActividad){
        sf = sesionFactory;
        this.vActividad=vActividades;
        this.uTablasActividades= uTablasActividad;
        v = new VistaMensajes();
        codsocio = "";
        
        vCRUDNuevaActividad = new JDialogNuevaActividad();
        vCRUDNuevaActividad.setLocationRelativeTo(null);
        vCRUDNuevaActividad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        vCRUDNuevaActividad.setResizable(false);
        vCRUDNuevaActividad.setVisible(false);

        vCRUDBajaActividad = new JDialogBajaActividad();
        vCRUDBajaActividad.setLocationRelativeTo(null);
        vCRUDBajaActividad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        vCRUDBajaActividad.setResizable(false);
        vCRUDBajaActividad.setVisible(false);
        addListeners();
        
    }

}
