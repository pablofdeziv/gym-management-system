package Modelo;

import java.util.ArrayList;
import javax.persistence.Query;
import org.hibernate.Session;
import Modelo.Socio;
import java.util.List;
import java.util.Set;

public class SocioDAO {

    static public ArrayList<Socio> listaSocioConHQL(Session sesion) throws Exception { //muestra la lista de socios usando HQL
        Query consulta = sesion.createQuery("SELECT s FROM Socio s", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    }

    static public ArrayList<Socio> listaSocioConSQL(Session sesion) throws Exception { //muestra la lista de socios usando SQL
        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO S", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    }

    static public ArrayList<Socio> listaSocioConsultaNombrada(Session sesion) throws Exception { //muestra la lista de socios con una consulta nombrada
        Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    }

    static public ArrayList<Object[]> listaNombreTelefonoSocios(Session sesion) throws Exception { //muestra el nombre y telefono de un socio
        Query consulta = sesion.createQuery("SELECT s.nombre, s.telefono FROM Socio s");
        ArrayList<Object[]> socios = (ArrayList<Object[]>) consulta.getResultList();
        return socios;
    }

    static public ArrayList<Object[]> listaNombreCategoriaSocios(Session sesion, char categoria) throws Exception { //muestra el nombre y categoria de un socio
        Query consulta = sesion.createQuery("SELECT s.nombre, s.categoria FROM Socio s WHERE s.categoria = :categ");
        consulta.setParameter("categ", categoria);
        ArrayList<Object[]> socios = (ArrayList<Object[]>) consulta.getResultList();
        return socios;
    }

    static public ArrayList<String> listaNombreActividadesSocio(Session sesion, String actividad) throws Exception { //muestra el nombre y las actividades de un socio
        Query consulta = sesion.createNativeQuery("SELECT s.nombre FROM SOCIO s INNER JOIN REALIZA r ON s.numeroSocio = r.numeroSocio INNER JOIN ACTIVIDAD a ON r.idActividad = a.idActividad WHERE a.idActividad = :act");
        consulta.setParameter("act", actividad);
        ArrayList<String> socios = (ArrayList<String>) consulta.getResultList();
        return socios;
    }

    static public boolean existeSocio(Session sesion, Socio socio) { //comprueba si existe un socio
        if (sesion.get(Socio.class, socio.getNumeroSocio()) == null) {
            return false;
        } else {
            return true;
        }
    }

    static public void insertaSocio(Session sesion, Socio socio) throws Exception { //inserta socio en la lista de socios
        sesion.saveOrUpdate(socio);
    }

    static public void eliminaSocio(Session sesion, Socio socio) throws Exception { //elimina socio en la lista de socios
        sesion.delete(socio);
    }

    static public void actualizaCategoriaSocio(Session sesion, Socio socio, char categoria) throws Exception { //actualizar la categoria de un socio
        socio.setCategoria(categoria);
        sesion.saveOrUpdate(socio);
    }

    static public void insertaSocioenActividad(Session sesion, Socio socio, Actividad actividad) throws Exception { //inserta un socio en una actividad
        actividad.altaSocio(socio);
        sesion.saveOrUpdate(actividad);
    }

    static public void eliminaSociodeActividad(Session sesion, Socio socio, Actividad actividad) throws Exception { //elimina un socio en una actividad
        actividad.bajaSocio(socio);
        sesion.saveOrUpdate(actividad);
    }

    static public ArrayList<String> ultimoCodSocio(Session sesion) { //devuelve el ultimo codigo de socio añadido en la lista
        Query consulta = sesion.createNativeQuery("SELECT MAX(numeroSocio) AS max_valor FROM SOCIO");
        ArrayList<String> codSocio = (ArrayList<String>) consulta.getResultList();
        return codSocio;
    }

    static public void ListaSociosenActividad(Session sesion, String codact) { //devuelve la lista de socios en una actividad
        Query consulta = sesion.createQuery("SELECT a FROM Actividad a WHERE a.idActividad = :codact", Actividad.class);
        consulta.setParameter("codact", codact);
        List<Actividad> actividades = consulta.getResultList();
        for (Actividad act : actividades) {
            Set<Socio> socios = act.getSocios();
            for (Socio socio : socios) {
                System.out.println(socio.getNombre() + " " + socio.getTelefono());
            }
        }
    }

    static public ArrayList<Socio> ListaSociosFiltro(Session sesion, String tipo, String filtro) { //devuelve la lista de socios con el filtro que queramos
        Query consulta;
        if (tipo == "Nombre") {
            consulta = sesion.createQuery("SELECT s FROM Socio s WHERE s.nombre LIKE :filtro", Socio.class);
            consulta.setParameter("filtro", filtro + "%");
        } else {
            consulta = sesion.createQuery("SELECT s FROM Socio s WHERE s.categoria LIKE :filtro", Socio.class);
            char filtrochar = filtro.charAt(0);
            consulta.setParameter("filtro", filtrochar);
        }
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    }
}
