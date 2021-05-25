package centroVacunacion;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.management.RuntimeErrorException;

import centroVacunacion.vacunas.Astrazeneca;
import centroVacunacion.vacunas.Moderna;
import centroVacunacion.vacunas.Pfizer;
import centroVacunacion.vacunas.Sinopharm;
import centroVacunacion.vacunas.SputnikV;

public class CentroVacunacion {
	
	 	private HashMap<Integer,Persona> personasSinTurno;
	    private HashMap<Integer,Persona> personasConTurno;
	    private HashMap<Integer,Persona> personasVacunadas; // Cuando se implemente una funcion "vacunar" tomara las personas de la lista personasConTurno y los movera a este conjunto.
	    private ArrayList<VacunaCovid19> vacunasEnStock;
	    private ArrayList<VacunaCovid19> vacunasReservadas;
	    private ArrayList<VacunaCovid19> vacunasVencidas;
	    private HashSet<Turno> turnos;
	    private int capacidadVacunacionDiaria;
	    private String nombreCentro;

	    /**
	    * Constructor.
	    * recibe el nombre del centro y la capacidad de vacunación diaria.
	    * Si la capacidad de vacunación no es positiva se debe generar una excepción.
	    * Si el nombre no está definido, se debe generar una excepción.
	    */
	    public CentroVacunacion(String nombreCentro, int capacidadVacunacionDiaria) {
	    	if(nombreCentro == null || nombreCentro.length() < 1) {
	    		throw new RuntimeException("Nombre invalido");
	    	} else if(capacidadVacunacionDiaria < 0) {
	    		throw new RuntimeException("Capacidad de vacunacion no puede ser menor a 0.");
	    	} else {
	    		this.nombreCentro = nombreCentro;
		    	this.capacidadVacunacionDiaria = capacidadVacunacionDiaria;
		    	this.personasSinTurno = new HashMap<>();
		    	this.personasConTurno = new HashMap<>();
		    	this.personasVacunadas = new HashMap<>();
		    	this.vacunasEnStock = new ArrayList<>();
		    	this.vacunasReservadas = new ArrayList<>();
		    	this.vacunasVencidas = new ArrayList<>();
		    	this.turnos = new HashSet<>();
	    	}
	    
	    }
	    
	    
	    /**
	    * Solo se pueden ingresar los tipos de vacunas planteados en la 1ra parte.
	    * Si el nombre de la vacuna no coincidiera con los especificados se debe generar
	    * una excepción.
	    * También se genera excepción si la cantidad es negativa.
	    * La cantidad se debe
	    * sumar al stock existente, tomando en cuenta las vacunas ya utilizadas.
	    */
	    public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) {
	    	if(cantidad <= 0) throw new RuntimeException("Cantidad de vacunas debe ser mayor a 0");
	    	switch (nombreVacuna.toUpperCase()) {
			case "ASTRAZENECA":
				for (int i = 0; i < cantidad ; i++) {
					vacunasEnStock.add(new Astrazeneca(fechaIngreso));	
				}
				break;
				
			case "MODERNA":
				for (int i = 0; i < cantidad ; i++) {
					vacunasEnStock.add(new Moderna(fechaIngreso));	
				}
				break;
				
			case "PFIZER":
				for (int i = 0; i < cantidad ; i++) {
					vacunasEnStock.add(new Pfizer(fechaIngreso));	
				}
				break;
				
			case "SINOPHARM":
				for (int i = 0; i < cantidad ; i++) {
					vacunasEnStock.add(new Sinopharm(fechaIngreso));	
				}
				break;
				
			case "SPUTNIK":
				for (int i = 0; i < cantidad ; i++) {
					vacunasEnStock.add(new SputnikV(fechaIngreso));	
				}
				break;
				
			default:
				throw new RuntimeException("Vacuna no se reconoce");
			}
	    	
	    	System.out.println(vacunasEnStock.size());
	    	
	    	
	    }
	
	    /**
	    * total de vacunas disponibles no vencidas sin distinción por tipo.
	    */
	    //TODO: Elimino las vacunas, porque aun no estan asignadas a personas o turnos.!
	    public int vacunasDisponibles() {
	    	int total = vacunasEnStock.size();
	    	Iterator<VacunaCovid19> it = vacunasEnStock.iterator();
	    	while(it.hasNext()) {
	    		VacunaCovid19 vacuna = it.next();
	    		if(vacuna instanceof Pfizer) {
	    			Pfizer pfizer = (Pfizer) vacuna;
	    			if(pfizer.estaVencida()) {
	    				total--;
	    				it.remove();
	    			}
	    		} else if(vacuna instanceof Moderna) {
	    			Moderna moderna = (Moderna) vacuna;
	    			if(moderna.estaVencida()) {
	    				total--;
	    				it.remove();
	    			}
	    		}
	    	}
	    	return total;
	    }
	    
	    /**
	    * total de vacunas disponibles no vencidas que coincida con el nombre de
	    * vacuna especificado.
	    */
	    public int vacunasDisponibles(String nombreVacuna) {
	    	int disponibles = 0;
	    	Iterator<VacunaCovid19> it = vacunasEnStock.iterator();
	    	while(it.hasNext()) {
	    		VacunaCovid19 vacuna = it.next();
	    		if(vacuna.getName().toUpperCase().equals(nombreVacuna.toUpperCase()) ) {
	    			disponibles++;
	    			if(vacuna instanceof Pfizer) {
	    				Pfizer pfizer = (Pfizer) vacuna;
	    				if(pfizer.estaVencida()) {
	    					 it.remove();
	    					 disponibles--;
	    				}
	    			} else if(vacuna instanceof Moderna) {
	    				Moderna moderna = (Moderna) vacuna;
	    				if(moderna.estaVencida()) {
	    					it.remove();
	    					disponibles--;
	    				}
	    			}
	    		}
	    	}
	    	return disponibles;
		}
	    
	    
	    /**
	    * Se inscribe una persona en lista de espera.
	    * Si la persona ya se encuentra inscripta o es menor de 18 años, se debe
	    * generar una excepción.
	    * Si la persona ya fue vacunada, también debe generar una excepción.
	    */
	    public void inscribirPersona(int dni, Fecha nacimiento, boolean tienePadecimientos, boolean esEmpleadoSalud) {
	    	//System.out.println(Fecha.diferenciaAnios(Fecha.hoy(), new Fecha(5, 4, 1964)));
	    	int edad = Fecha.diferenciaAnios(Fecha.hoy(), nacimiento);
	    	if(edad < 18) {
	    		throw new RuntimeException("Debe ser mayor de edad para inscribirse.");
	    	} else if(personasSinTurno.containsKey(dni)) {
	    		throw new RuntimeException("Persona ya inscripta.");
	    	} else if(personasVacunadas.containsKey(dni)) {
	    		throw new RuntimeException("Persona ya fue vacunada.");
	    	} else {
	    		
	    		personasSinTurno.put(dni, new Persona(dni,edad,esEmpleadoSalud,tienePadecimientos));
	    		
	    	}
	    }
	    
	    
	    
	    /**
	    * Devuelve una lista con los DNI de todos los inscriptos que no se vacunaron
	    * y que no tienen turno asignado.
	    * Si no quedan inscriptos sin vacunas debe devolver una lista vacía.
	    */
	    public java.util.List<Integer> listaDeEspera() {
	    	ArrayList<Integer> listaEspera = new ArrayList<>();
	    	listaEspera.addAll(this.personasSinTurno.keySet());
	    	return listaEspera;
	    }
	   
	    /**
	     * @param args
	     */
	    public static void main(String[] args) {
	    	CentroVacunacion centro = new CentroVacunacion("UNGS", 5);
			centro.ingresarVacunas("Sputnik", 10,new Fecha(20,3,2021));
			centro.ingresarVacunas("AstraZeneca", 10,new Fecha(20,3,2021));
			System.out.println(centro.vacunasEnStock);
			centro.inscribirPersona(34701000, new Fecha(1, 5, 1989), false, false);
			
			
			
		}
	    /**
	    * Primero se verifica si hay turnos vencidos. En caso de haber turnos
	    * vencidos, la persona que no asistió al turno debe ser borrada del sistema
	    * y la vacuna reservada debe volver a estar disponible.
	    *
	    * Segundo, se deben verificar si hay vacunas vencidas y quitarlas del sistema.
	    *
	    * Por último, se procede a asignar los turnos a partir de la fecha inicial
	    * recibida según lo especificado en la 1ra parte.
	    * Cada vez que se registra un nuevo turno, la vacuna destinada a esa persona
	    * dejará de estar disponible. Dado que estará reservada para ser aplicada
	    * el día del turno.
	    *
	    *
	    */
	    public void generarTurnos(Fecha fechaInicial) {
	    	
	    	Fecha hoy = Fecha.hoy();
	    	
	    	/* Verificar turnos vencidos*/
	    	Iterator<Turno> itTurnos = turnos.iterator();
	    	while(itTurnos.hasNext()) {
	    		Turno turno = itTurnos.next();
	    		if(turno.estaVencido()) {
	    			int dni = turno.getPersona().getDni();
	    			VacunaCovid19 vacuna = turno.getVacuna();
	    			personasConTurno.remove(dni);
	    			vacunasReservadas.remove(vacuna);
	    			vacunasEnStock.add(vacuna);
	    			itTurnos.remove();
	    		}
	    	}
	    	/* ------------------- */
	    	
	    	/*Segundo, se deben verificar si hay vacunas vencidas y quitarlas del sistema.*/
	    	/*Verificar vacunas vencidas*/
	    	Iterator<VacunaCovid19> itVacunas = vacunasEnStock.iterator();
	    	while(itVacunas.hasNext()) {
	    		VacunaCovid19 vacuna = itVacunas.next();
	    		if(vacuna instanceof Pfizer) {
	    			Pfizer pfizer = (Pfizer) vacuna;
	    			if(pfizer.estaVencida()) {
	    				itVacunas.remove();
	    			} 
	    		} else if(vacuna instanceof Moderna) {
	    			Moderna moderna = (Moderna) vacuna;
	    			if(moderna.estaVencida()) {
	    				itVacunas.remove();
	    			}
	    		}
	    	}
	    	
	    	
	    	
	    	/* ------------------- */
	    	
	    }
	    /**
	    * Devuelve una lista con los dni de las personas que tienen turno asignado
	    * para la fecha pasada por parámetro.
	    * Si no hay turnos asignados para ese día, se debe devolver una lista vacía.
	    * La cantidad de turnos no puede exceder la capacidad por día de la ungs.
	    */
	    public java.util.List<Integer> turnosConFecha(Fecha fecha) {
	    	return new ArrayList<Integer>();
	    }
	    /**
	    * Dado el DNI de la persona y la fecha de vacunación
	    * se valida que esté inscripto y que tenga turno para ese dia.
	    * - Si tiene turno y está inscripto se debe registrar la persona como
	    * vacunada y la vacuna se quita del depósito.
	    * - Si no está inscripto o no tiene turno ese día, se genera una Excepcion.
	    */
	    public void vacunarInscripto(int dni, Fecha fechaVacunacion) {
	    	
	    }
	    /**
	    * Devuelve un Diccionario donde
	    * - la clave es el dni de las personas vacunadas
	    * - Y, el valor es el nombre de la vacuna aplicada.
	    */
	    public Map<Integer, String> reporteVacunacion() {
	    	return new HashMap<Integer,String>();
	    }
	    /**
	    * Devuelve en O(1) un Diccionario:
	    * - clave: nombre de la vacuna
	    * - valor: cantidad de vacunas vencidas conocidas hasta el momento.
	    */
	    public Map<String, Integer> reporteVacunasVencidas() {
	    	return new HashMap<String,Integer>();
	    }

	

}
