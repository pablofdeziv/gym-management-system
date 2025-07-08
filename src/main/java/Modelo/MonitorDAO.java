package Modelo;

import java.util.ArrayList;
import javax.persistence.Query;
import org.hibernate.Session;

public class MonitorDAO {
    
    static public ArrayList<Object[]> MonitorResponsable(Session sesion, String actividad) throws Exception { //busca el monitor responsable de una actividad
        Query consulta = sesion.createNativeQuery("SELECT m.* FROM ACTIVIDAD a INNER JOIN MONITOR m ON a.monitorResponsable = m.codMonitor WHERE a.idActividad =:act");
        consulta.setParameter("act", actividad);
        ArrayList<Object[]> responsable = (ArrayList<Object[]>) consulta.getResultList();
        return responsable;
    }
    
    static public ArrayList<Monitor> listaMonitor(Session sesion) throws Exception { //muestra lista de monitores
        Query consulta = sesion.createQuery("SELECT s FROM Monitor s", Monitor.class);
        ArrayList<Monitor> socios = (ArrayList<Monitor>) consulta.getResultList();
        return socios;
    }
    
    static public boolean existeMonitor(Session sesion, Monitor monitor) { //comprueba si existe un monitor dentro de la lista de monitores
        if (sesion.get(Monitor.class, monitor.getCodMonitor()) == null) {
            return false;
        } else {
            return true;
        }
    }
    
    static public void insertaMonitor(Session sesion, Monitor monitor) throws Exception { //inserta un monitor en la lista de monitores
        sesion.saveOrUpdate(monitor);
    }
    
    static public void eliminaMonitor(Session sesion, Monitor monitor) throws Exception { //borra un monitor de la lista de monitores
        sesion.delete(monitor);
    }
    
    static public ArrayList<String> ultimoCodMonitor(Session sesion){ //devuelve el codigo del ultimo monitor insertado 
        Query consulta = sesion.createNativeQuery("SELECT MAX(codMonitor) AS max_valor FROM MONITOR");
        ArrayList<String> codMonitor = (ArrayList<String>) consulta.getResultList();
        return codMonitor;
    }

}
