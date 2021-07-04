package centroVacunacion;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ControlTurnos {
	
	private HashMap<Integer,Persona> personasSinTurno;
 	private HashMap<Integer,Persona> personasConTurno;
    private HashMap<Integer,Persona> personasVacunadas;
	private HashSet<Turno> turnos;

	ControlTurnos() {
			this.personasSinTurno = new HashMap<>();
	    	this.personasConTurno = new HashMap<>();
	    	this.personasVacunadas = new HashMap<>();
			this.turnos = new HashSet<>();
	}

    public Collection<Integer> getDNIsPersonasSinTurno() {
        return personasSinTurno.keySet();
    }
    public Collection<Integer> getDNIsPersonasVacunadas() {
        return personasVacunadas.keySet();
    }
    public int cantidadPersonasVacunadas() {
	    return personasVacunadas.size();
    }

    public Persona getPersonaSinTurnoPorDNI(Integer dni) {
        return personasSinTurno.get(dni);
    }

    public HashSet<Turno> getTurnos() {
	    return turnos;
    }

    public boolean personaEstaVacunada(int dni) {
        return personasVacunadas.containsKey(dni);
    }

    public boolean personaEstaInscripta(int dni) {
        return personasSinTurno.containsKey(dni);
    }

    public void agregarPersonaSinTurno(int dni, boolean tienePadecimientos, boolean esEmpleadoSalud, int edad) {
        personasSinTurno.put(dni, new Persona(dni, edad, esEmpleadoSalud, tienePadecimientos));
    }
    public void eliminarPersonaSinTurno(Persona persona) {
        personasSinTurno.remove(persona.getDni());
    }
    public void agregarPersonaConTurno(Persona persona) {
        personasConTurno.put(persona.getDni(), persona);
    }

    public void eliminarPersonaConTurno(int dni) {
        personasConTurno.remove(dni);
    }

    public void agregarPersonaVacunada(Persona persona) {
        personasVacunadas.put(persona.getDni(), persona);
    }

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }
    public void eliminarTurno(Persona persona) {
        turnos.remove(persona.getTurno());
    }

    public Persona getPersonaConTurno(int dni) {
        return personasConTurno.get(dni);
    }
    public boolean personaTieneTurno(int dni) {
        return personasConTurno.containsKey(dni);
    }

    public Persona getPersonaVacunada(Integer dni) {
        return personasVacunadas.get(dni);
    }

    public Iterator<Turno> getIteratorTurnos() {
	    return turnos.iterator();
    }




}
