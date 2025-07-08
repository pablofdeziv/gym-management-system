/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.JDialogBajaActividad;
import Vista.JDialogNuevaActividad;
import Vista.JDialogNuevoMonitor;
import Vista.JPanelActividades;
import Vista.JPanelSocio;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UtilTablasActividad {

    private DefaultTableModel modeloTablaActividades;
    
    public void inicializarTablaActividades(JDialogNuevaActividad vActividad) {
        modeloTablaActividades  = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vActividad.jTableActividades.setModel(modeloTablaActividades);
    }
    
    public void inicializarTablaActividades(JDialogBajaActividad vActividad) {
        modeloTablaActividades  = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vActividad.jTableActividades.setModel(modeloTablaActividades);
    }

    public void vaciarTablaActividades() {
        while (modeloTablaActividades.getRowCount() > 0) {
            modeloTablaActividades.removeRow(0);
        }
    }

    public void rellenarTablaActividadesAlta(ArrayList<Actividad> actividades) {
        Object[] fila = new Object[5];
        for (Actividad actividad : actividades) {
            fila[0] = actividad.getIdActividad();
            fila[1] = actividad.getNombre();
            fila[2] = actividad.getDescripcion();
            fila[3] = actividad.getPrecioBaseMes();
            fila[4] = actividad.getMonitorResponsable().getCodMonitor();
            modeloTablaActividades.addRow(fila);
        }
    }
    
    public void rellenarTablaActividadesBaja(ArrayList<Object[]> actividades) {
        Object[] fila = new Object[5];
        for (Object[] actividad : actividades) {
            fila[0] = actividad[0];
            fila[1] = actividad[1];
            fila[2] = actividad[2];
            fila[3] = actividad[3];
            fila[4] = actividad[4];
            /*fila[0] = actividad.getIdActividad();
            fila[1] = actividad.getNombre();
            fila[2] = actividad.getDescripcion();
            fila[3] = actividad.getPrecioBaseMes();
            fila[4] = actividad.getMonitorResponsable();*/
            modeloTablaActividades.addRow(fila);
        }
    }
    
    public void dibujarTablaActividades(JDialogNuevaActividad vActividad) {

        String[] columnasTabla = {"Actividad", "Nombre", "Descripción", "Precio/Mes", "Monitor Responsable"};
        modeloTablaActividades.setColumnIdentifiers(columnasTabla);

        vActividad.jTableActividades.getTableHeader().setResizingAllowed(false);
        vActividad.jTableActividades.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vActividad.jTableActividades.getColumnModel().getColumn(0).setPreferredWidth(15);
        vActividad.jTableActividades.getColumnModel().getColumn(1).setPreferredWidth(50);
        vActividad.jTableActividades.getColumnModel().getColumn(2).setPreferredWidth(270);
        vActividad.jTableActividades.getColumnModel().getColumn(3).setPreferredWidth(20);
        vActividad.jTableActividades.getColumnModel().getColumn(4).setPreferredWidth(25);
        
    }
    
    public void dibujarTablaActividades(JDialogBajaActividad vActividad) {

        String[] columnasTabla = {"Actividad", "Nombre", "Descripción", "Precio/Mes", "Monitor Responsable"};
        modeloTablaActividades.setColumnIdentifiers(columnasTabla);

        vActividad.jTableActividades.getTableHeader().setResizingAllowed(false);
        vActividad.jTableActividades.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vActividad.jTableActividades.getColumnModel().getColumn(0).setPreferredWidth(15);
        vActividad.jTableActividades.getColumnModel().getColumn(1).setPreferredWidth(50);
        vActividad.jTableActividades.getColumnModel().getColumn(2).setPreferredWidth(270);
        vActividad.jTableActividades.getColumnModel().getColumn(3).setPreferredWidth(20);
        vActividad.jTableActividades.getColumnModel().getColumn(4).setPreferredWidth(25);

    }
}
