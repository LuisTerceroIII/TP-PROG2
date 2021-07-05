package centroVacunacion;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/*
* IREP:
	personasSinTurno: personaSinTurno != null
	personasConTurno: personasConTurno.size() == turnos.size()
	personasVacunadas: personasVacunadas != null
	turnos: turnos.size() == personasConTurno.size()
*
* */
public class ControlTurnos {
	private HashMap<Integer,Persona> personasSinTurno;
 	private HashMap<Integer,Persona> personasConTurno;
    private HashMap<Integer,Persona> personasVacunadas;
	private HashSet<Turno> turnos;

	public ControlTurnos() {
			this.personasSinTurno = new HashMap<>();
	    	this.personasConTurno = new HashMap<>();
	    	this.personasVacunadas = new HashMap<>();
			this.turnos = new HashSet<>();
	}

    public void agregarPersonaSinTurno(Persona persona) {
        personasSinTurno.put(persona.getDni(), persona);
    }
    public void eliminarPersonaSinTurno(int dni) {
        personasSinTurno.remove(dni);
    }
    public Persona getPersonaSinTurnoPorDNI(Integer dni) {
        return personasSinTurno.get(dni);
    }
    public Collection<Integer> getDNIsPersonasSinTurno() {
        return personasSinTurno.keySet();
    }
    public int cantidadPersonasSinTurno() { return personasSinTurno.size(); }

    public void agregarPersonaConTurno(Persona persona) {
        personasConTurno.put(persona.getDni(), persona);
    }
    public void eliminarPersonaConTurno(int dni) {
        personasConTurno.remove(dni);
    }
    public Persona getPersonaConTurnoPorDNI(int dni) {
        return personasConTurno.get(dni);
    }
    public Collection<Integer> getDNIsPersonasConTurno() {
        return personasConTurno.keySet();
    }
    public int cantidadPersonasConTurno() { return personasConTurno.size(); }

    public void agregarPersonaVacunada(Persona persona) {
        personasVacunadas.put(persona.getDni(), persona);
    }
    public void eliminarPersonaVacunada(int dni) {
        personasVacunadas.remove(dni);
    }
    public Persona getPersonaVacunadaPorDNI(Integer dni) {
        return personasVacunadas.get(dni);
    }
    public Collection<Integer> getDNIsPersonasVacunadas() { return personasVacunadas.keySet(); }
    public int cantidadPersonasVacunadas() { return personasVacunadas.size(); }

    public void agregarTurno(Turno turno) { turnos.add(turno); }
    public void eliminarTurno(Turno turno) { turnos.remove(turno); }
    public HashSet<Turno> getTurnos() {
        return turnos;
    }

    public boolean personaEstaVacunada(int dni) {
        return personasVacunadas.containsKey(dni);
    }
    public boolean personaEstaInscripta(int dni) {
        return personasSinTurno.containsKey(dni);
    }
    public boolean personaTieneTurno(int dni) {
        return personasConTurno.containsKey(dni);
    }

    public Iterator<Turno> getIteratorTurnos() { return turnos.iterator(); }
    /*
    * Es importante notar que este metodo debe recibir un objeto deposito ya que la consiga es que, un turno vencido significa una vacuna liberada,
    * es por eso que se utiliza al objeto deposito para poder almacenar la vacuna del turno perdido.
    * */
    public void eliminarTurnosVencidos(DepositoVacunas deposito) {
        Iterator<Turno> itTurnos = getIteratorTurnos();
        while(itTurnos.hasNext()) {
            Turno turno = itTurnos.next();
            if(turno.estaVencido()) {
                int dni = turno.dniPersona();
                VacunaCovid19 vacuna = turno.getVacuna();
                eliminarPersonaConTurno(dni);
                deposito.eliminarVacunaReservada(vacuna);
                deposito.agregarVacunaAStock(vacuna);
                itTurnos.remove();
            }
        }
    }
}
