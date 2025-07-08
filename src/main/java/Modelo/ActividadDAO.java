package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import static org.hibernate.criterion.Projections.id;
import org.hibernate.query.Query;

public class ActividadDAO {

    static public boolean existeActividad(Session sesion, Actividad actividad) { //comprueba si existe una actividad dentro de la lista de actividades
        if (sesion.get(Actividad.class, actividad.getIdActividad()) == null) {
            return false;
        } else {
            return true;
        }
    }

    static public void ListaActividadesdeSocio(Session sesion, String numsoc) { //muestra las actividades a la que esta inscrito un socio
        Query consulta = sesion.createQuery("SELECT s FROM Socio s WHERE s.numeroSocio = :numsoc", Socio.class);
        consulta.setParameter("numsoc", numsoc);
        List<Socio> socios = consulta.getResultList();
        for (Socio socio : socios) {
            Set<Actividad> actividades = socio.getActividades();
            for (Actividad actividad : actividades) {
                System.out.println(actividad.getNombre() + " " + actividad.getPrecioBaseMes());
            }
        }
    }
    
    static ArrayList<Actividad> listaActividadesHQL(Session sesion) { //muestra la lista de actividades
        Query consulta = sesion.createQuery("SELECT a FROM Actividad a", Actividad.class);
        ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list();
        return actividades;
    }
    
    static public ArrayList<Object[]> ListaActividadesdeSocioConcreto(Session sesion, String numsoc) { //muestra las actividades a la que esta inscrito un socio
        Query consulta = sesion.createNativeQuery("SELECT a.idActividad, a.nombre, a.descripcion, a.precioBaseMes, a.monitorResponsable FROM ACTIVIDAD a INNER JOIN REALIZA r ON (r.idActividad = a.idActividad) INNER JOIN SOCIO s ON (s.numeroSocio = r.numeroSocio) WHERE s.numeroSocio = :id").setParameter("id", numsoc);
        ArrayList<Object[]> actividades = (ArrayList<Object[]>) consulta.getResultList();
        return actividades;
    }
    
    static public ArrayList<Actividad> ListaActividadesNodeSocio(Session sesion, ArrayList<Object[]> lAct) { //muestra las actividades a la que esta inscrito un socio
        ArrayList<Actividad> lista = listaActividadesHQL(sesion);
        for(Object[] actividad : lAct) {
            lista.remove(sesion.get(Actividad.class, (String)actividad[0]));
        }
        return lista;
    }
}
