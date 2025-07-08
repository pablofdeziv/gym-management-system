/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.JPanelActividades;
import Vista.JPanelSocio;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UtilTablasSocio {

    private DefaultTableModel modeloTablaSocios;

    public void inicializarTablaSocios(JPanelSocio vSocio) {
        modeloTablaSocios  = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vSocio.jTableSocio.setModel(modeloTablaSocios);
    }
    
    public void inicializarTablaSocios(JPanelActividades vActividad) {
        modeloTablaSocios  = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vActividad.jTableSocio.setModel(modeloTablaSocios);
    }

    public void vaciarTablaSocios() {
        while (modeloTablaSocios.getRowCount() > 0) {
            modeloTablaSocios.removeRow(0);
        }
    }

    public void rellenarTablaSocios(ArrayList<Socio> socios) {
        Object[] fila = new Object[8];
        for (Socio socio : socios) {
            fila[0] = socio.getNumeroSocio();
            fila[1] = socio.getNombre();
            fila[2] = socio.getDni();
            fila[3] = socio.getFechaNacimiento();
            fila[4] = socio.getTelefono();
            fila[5] = socio.getCorreo();
            fila[6] = socio.getFechaEntrada();
            fila[7] = socio.getCategoria();
            modeloTablaSocios.addRow(fila);
        }
    }

    public void dibujarTablaSocios(JPanelSocio vSocio) {

        String[] columnasTabla = {"Socio", "Nombre", "DNI", "Fecha de nacimiento", "Teléfono", "Correo", "Fecha de Alta", "Cat."};
        modeloTablaSocios.setColumnIdentifiers(columnasTabla);

        vSocio.jTableSocio.getTableHeader().setResizingAllowed(false);
        vSocio.jTableSocio.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vSocio.jTableSocio.getColumnModel().getColumn(0).setPreferredWidth(30);
        vSocio.jTableSocio.getColumnModel().getColumn(1).setPreferredWidth(160);
        vSocio.jTableSocio.getColumnModel().getColumn(2).setPreferredWidth(60);
        vSocio.jTableSocio.getColumnModel().getColumn(3).setPreferredWidth(60);
        vSocio.jTableSocio.getColumnModel().getColumn(4).setPreferredWidth(60);
        vSocio.jTableSocio.getColumnModel().getColumn(5).setPreferredWidth(200);
        vSocio.jTableSocio.getColumnModel().getColumn(6).setPreferredWidth(60);
        vSocio.jTableSocio.getColumnModel().getColumn(7).setPreferredWidth(10);

    }
    
    public void dibujarTablaSocios(JPanelActividades vActividad) {

        String[] columnasTabla = {"Socio", "Nombre", "DNI", "Fecha de nacimiento", "Teléfono", "Correo", "Fecha de Alta", "Cat."};
        modeloTablaSocios.setColumnIdentifiers(columnasTabla);

        vActividad.jTableSocio.getTableHeader().setResizingAllowed(false);
        vActividad.jTableSocio.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vActividad.jTableSocio.getColumnModel().getColumn(0).setPreferredWidth(30);
        vActividad.jTableSocio.getColumnModel().getColumn(1).setPreferredWidth(160);
        vActividad.jTableSocio.getColumnModel().getColumn(2).setPreferredWidth(60);
        vActividad.jTableSocio.getColumnModel().getColumn(3).setPreferredWidth(60);
        vActividad.jTableSocio.getColumnModel().getColumn(4).setPreferredWidth(60);
        vActividad.jTableSocio.getColumnModel().getColumn(5).setPreferredWidth(200);
        vActividad.jTableSocio.getColumnModel().getColumn(6).setPreferredWidth(60);
        vActividad.jTableSocio.getColumnModel().getColumn(7).setPreferredWidth(10);

    }

    public UtilTablasSocio(JPanelSocio vSocio) {
        inicializarTablaSocios(vSocio);
    }
    
    public UtilTablasSocio(JPanelActividades vActividad) {
        inicializarTablaSocios(vActividad);
    }
}
