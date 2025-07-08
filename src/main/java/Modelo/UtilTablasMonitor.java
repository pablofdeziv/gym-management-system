/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.JPanelMonitor;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pablo
 */
public class UtilTablasMonitor {
    
    private DefaultTableModel modeloTablaMonitores;
    
    public void inicializarTablaMonitores(JPanelMonitor vMonitor){
        modeloTablaMonitores = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        
        vMonitor.jTableMonitor.setModel(modeloTablaMonitores);
    }
    
    public void vaciarTablaMonitores(){
        while (modeloTablaMonitores.getRowCount() > 0){
            modeloTablaMonitores.removeRow(0);
        }
    }
    
    public void rellenarTablaMonitores(ArrayList<Monitor> monitores){
        Object[] fila = new Object[7];
        for (Monitor monitor : monitores) {
            fila[0] = monitor.getCodMonitor();
            fila[1] = monitor.getNombre();
            fila[2] = monitor.getDni();
            fila[3] = monitor.getTelefono();
            fila[4] = monitor.getCorreo();
            fila[5] = monitor.getFechaEntrada();
            fila[6] = monitor.getNick();
            modeloTablaMonitores.addRow(fila);
        }
    }
    
    
    public void dibujarTablaMonitores(JPanelMonitor vMonitor){
        
        String[] columnasTabla = {"Código", "Nombre", "DNI", "Teléfono" , "Correo", "Fecha incorporación", "Nick"};
        modeloTablaMonitores.setColumnIdentifiers(columnasTabla);
        
        vMonitor.jTableMonitor.getTableHeader().setResizingAllowed(false);
        vMonitor.jTableMonitor.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        vMonitor.jTableMonitor.getColumnModel().getColumn(0).setPreferredWidth(60);
        vMonitor.jTableMonitor.getColumnModel().getColumn(1).setPreferredWidth(240);
        vMonitor.jTableMonitor.getColumnModel().getColumn(2).setPreferredWidth(70);
        vMonitor.jTableMonitor.getColumnModel().getColumn(3).setPreferredWidth(70);
        vMonitor.jTableMonitor.getColumnModel().getColumn(4).setPreferredWidth(200);
        vMonitor.jTableMonitor.getColumnModel().getColumn(5).setPreferredWidth(150);
        vMonitor.jTableMonitor.getColumnModel().getColumn(6).setPreferredWidth(60);
        
    }

    public UtilTablasMonitor(JPanelMonitor vMonitor) {
        inicializarTablaMonitores(vMonitor);
    }
    
    
    
}
