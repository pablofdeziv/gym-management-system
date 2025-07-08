/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Monitor;
import Modelo.Socio;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class VistaMensajes {

    public void mensajeConsola(String texto) {
        /*System.out.println("***************************************");
        System.out.println(texto);
        System.out.println("***************************************");*/
        JOptionPane.showMessageDialog(null, texto);
    }

    public void mensajeConsola(String texto, String error) {
        /*System.out.println("***************************************");
        System.out.println(texto);
        System.out.println(error);
        System.out.println("***************************************");*/
        JOptionPane.showMessageDialog(null, texto + " " + error, "Error", JOptionPane.ERROR_MESSAGE );
    }
    
    public int Dialogo(String texto){
        int opcion = JOptionPane.showConfirmDialog(null, texto, "Atención", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        return opcion;
    }

    public void vistaMenu() {
        System.out.println("**************MENU**************");
        System.out.println("¿Que operación quieres realizar?");
        System.out.println("1.-Mostrar listado de socios (HQL)");
        System.out.println("2.-Mostrar listado de socios (SQL)");
        System.out.println("3.-Mostrar listado de socios (Consulta nombrada)");
        System.out.println("4.-Consultar nombre y telefono de los socios");
        System.out.println("5.-Consultar socios de una categoria");
        System.out.println("6.-Consultar responsable de una actividad");
        System.out.println("7.-Consultar socios inscritos en una actividad");
        System.out.println("8.-Alta de un socio");
        System.out.println("9.-Baja de un socio");
        System.out.println("10.-Actualizacion de la categoria de un socio");
        System.out.println("11.-Inscripcion de un socio en una actividad");
        System.out.println("12.-Baja de un socio de una actividad");
        System.out.println("13.-Listado de socios inscritos en una actividad");
        System.out.println("14.-Listado de actividades de un socio");
        System.out.println("15.-Salir");
        System.out.println("Introduce un numero:");
    }

    public void verLista(ArrayList<Socio> lista) {
        for (Socio socio : lista) {
            System.out.println(socio.getNumeroSocio() + " " + socio.getNombre() + " " + socio.getDni() + " " + socio.getFechaNacimiento() + " " + socio.getTelefono() + " " + socio.getCorreo() + " " + socio.getFechaEntrada() + " " + socio.getCategoria());
        }
    }

    public void verListaNomTel(ArrayList<Object[]> lista) {
        for (Object[] socio : lista) {
            System.out.println(socio[0] + " " + socio[1]);
        }
    }

    public void verListaNomCat(ArrayList<Object[]> lista) { 
        for (Object[] socio : lista) {
            System.out.println(socio[0] + " " + socio[1]);
        }
    }

    public void verMonitorResponsable(ArrayList<Object[]> lista) {
        for (Object[] m : lista) 
            System.out.println(m[0] + " " + m[1] + " " + m[2] + " " + m[3] + " " + m[4] + " " + m[5] + " " + m[6]);
    }
    
    public void verListaSociosAct(ArrayList<String> lista) {
        for (int i=0; i < lista.size(); i++) {
            System.out.println(lista.get(i));
        }
    }
    
}
