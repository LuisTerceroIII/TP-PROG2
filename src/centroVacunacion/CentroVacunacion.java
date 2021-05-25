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
	    private int turnosPorDia; // creo esta copia para no modificar la variable de instancia y no perder el valor dado al inicio cuando tenga que "recargar" los cupos al avanzar de dia.

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
		    	this.turnosPorDia = capacidadVacunacionDiaria;
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
	    	CentroVacunacion centro = new CentroVacunacion("UNGS", 2);
			centro.ingresarVacunas("Sputnik", 10,new Fecha(20,3,2021));
			centro.ingresarVacunas("Moderna", 10,new Fecha(20,4,2021));
			centro.inscribirPersona(34701000, new Fecha(1, 5, 1999), true, true);
			centro.inscribirPersona(34701220, new Fecha(1, 5, 1995), false, false);
			centro.inscribirPersona(34701440, new Fecha(1, 5, 1989), true, true);
			centro.inscribirPersona(34701569, new Fecha(1, 5, 1989), false, true);
			centro.inscribirPersona(34701561, new Fecha(1, 5, 1989), true, false);
			centro.inscribirPersona(34701551, new Fecha(1, 5, 1989), true, false);
			
			centro.inscribirPersona(34701010, new Fecha(1, 5, 1999), true, true);
			centro.inscribirPersona(34701240, new Fecha(1, 5, 1995), false, false);
			centro.inscribirPersona(34701480, new Fecha(1, 5, 1989), true, true);
			centro.inscribirPersona(34701560, new Fecha(1, 5, 1989), false, true);
			centro.inscribirPersona(34701501, new Fecha(1, 5, 1989), true, false);

			
			centro.generarTurnos(Fecha.hoy());
			System.out.println(centro.turnos);
			System.out.println(centro.turnos.size());
			System.out.println(centro.personasConTurno);
			System.out.println(centro.personasSinTurno);
			
			
			
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
	    /**
	     * @param fechaInicial
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
	    	
	    /*  Por último, se procede a asignar los turnos a partir de la fecha inicial
	    * recibida según lo especificado en la 1ra parte.
	    * Cada vez que se registra un nuevo turno, la vacuna destinada a esa persona
	    * dejará de estar disponible. Dado que estará reservada para ser aplicada
	    * el día del turno. **/
	    	
	    	/*Generacion de turnos !*/
	    	
	    	/*1er Parte: Division de las personas inscriptas en categorias*/
	    	HashSet<Persona> trabajadoresSalud = new HashSet<>();
	    	HashSet<Persona> mayoresDe60 = new HashSet<>();
	    	HashSet<Persona> conEnfermedadPreexistente = new HashSet<>();
	    	HashSet<Persona> resto = new HashSet<>();
	    	
	    	Iterator<Integer> itPersonas = personasSinTurno.keySet().iterator();
	    	
	    	while(itPersonas.hasNext()) {
	    		Persona persona = personasSinTurno.get(itPersonas.next());
	    		
	    		//Por implemtancion me conviene enviarlo a mayoresDe60
    			//y no a trabajadores de salud, sino tendre que volver a validar la edad 
    			//en un grupo que no corresponde.
	    		if(persona.isTrabajadorSalud() && persona.isEnfermedadPreexistente() && persona.getEdad() > 60) {
	    			mayoresDe60.add(persona);
	    			
	    	
	    		} else if (persona.isTrabajadorSalud() && persona.getEdad() > 60) {
	    			mayoresDe60.add(persona);
	    			
	    		} else if (persona.isTrabajadorSalud() && persona.isEnfermedadPreexistente()) {
	    			trabajadoresSalud.add(persona);
	    			
	    		} else if(persona.isTrabajadorSalud()) {
	    			trabajadoresSalud.add(persona);
	    			
	    		} else if(persona.getEdad() > 60 && persona.isEnfermedadPreexistente()) {
	    			mayoresDe60.add(persona);
	    		
	    		} else if(persona.getEdad() > 60) {
	    			mayoresDe60.add(persona);
	    		}
	    		
	    		else if(persona.isEnfermedadPreexistente()) {
	    			conEnfermedadPreexistente.add(persona);
	    		} else {
	    			resto.add(persona);
	    		}
	    	}
	    	
	    	/* ----------Fin 1er. Parte--------- */
	    	
	    	/*2da Parte: Distribucion de los turnos*/
	    	
		    /* Cada vez que se registra un nuevo turno, la vacuna destinada a esa persona
		    * dejará de estar disponible. Dado que estará reservada para ser aplicada
		    * el día del turno. */
    	
    		/*Turnos para trabajadores de la salud*/
    	  	Iterator<Persona> itTrabajadores = trabajadoresSalud.iterator();
	    	while(itTrabajadores.hasNext()) {
	    		Persona persona = itTrabajadores.next();
	    		Iterator<VacunaCovid19> itVacTrabajador = vacunasEnStock.iterator();
    			while(itVacTrabajador.hasNext()) {
    				VacunaCovid19 vacuna = itVacTrabajador.next();
    				if(!(vacuna instanceof Pfizer || vacuna instanceof SputnikV) && persona.getTurno() == null) {
    					Fecha fecha = generarFechaTurno(fechaInicial);
    					Turno turno = new Turno(fecha,persona,vacuna);
    					persona.setTurno(turno);
    					persona.setVacuna(vacuna);
    					itVacTrabajador.remove();
    					vacunasReservadas.add(vacuna);	    					
    					personasSinTurno.remove(persona.getDni());
    					personasConTurno.put(persona.getDni(), persona);
    					turnos.add(turno);
    				}
    			}
	    	}
	    	
	    	System.out.println("Fuera de trabajadores : " + trabajadoresSalud);
	    	/*Turnos para mayores de 60*/
	    	Iterator<Persona> itMayores60 = mayoresDe60.iterator();
	    	while(itMayores60.hasNext()) {
	    		Persona persona = itMayores60.next();
	    		Iterator<VacunaCovid19> itVacMayores60 = vacunasEnStock.iterator();
	    		while(itVacMayores60.hasNext()) {
	    			VacunaCovid19 vacuna = itVacMayores60.next();
	    			if((vacuna instanceof Pfizer || vacuna instanceof SputnikV) && persona.getTurno() == null) {
    					Fecha fecha = generarFechaTurno(fechaInicial);
    					Turno turno = new Turno(fecha,persona,vacuna);
    					persona.setTurno(turno);
    					persona.setVacuna(vacuna);
    					itVacMayores60.remove();
    					vacunasReservadas.add(vacuna);	    					
    					personasSinTurno.remove(persona.getDni());
    					personasConTurno.put(persona.getDni(), persona);
    					turnos.add(turno);
    				}
	    		}    		
	    	}
	    	
	    	/*Turnos para personas con enfermedad preexistente*/
	    	Iterator<Persona> itPreexistente = conEnfermedadPreexistente.iterator();
	    	while(itPreexistente.hasNext()) {
	    		Persona persona = itPreexistente.next();
	    		Iterator<VacunaCovid19> itVacPreexistentes = vacunasEnStock.iterator();
	    		while(itVacPreexistentes.hasNext()) {
	    			VacunaCovid19 vacuna = itVacPreexistentes.next();
	    			if( !(vacuna instanceof Pfizer || vacuna instanceof SputnikV) && persona.getTurno() == null) {
    					Fecha fecha = generarFechaTurno(fechaInicial);
    					Turno turno = new Turno(fecha,persona,vacuna);
    					persona.setTurno(turno);
    					persona.setVacuna(vacuna);
    					itVacPreexistentes.remove();
    					vacunasReservadas.add(vacuna);	    					
    					personasSinTurno.remove(persona.getDni());
    					personasConTurno.put(persona.getDni(), persona);
    					turnos.add(turno);
    				}
	    		} 
	    		
	    	}
	    	/*Turnos para todos los demas*/
	    	Iterator<Persona> itResto = resto.iterator();
	    	while(itResto.hasNext()) {
	    		Persona persona = itResto.next();
	    		Iterator<VacunaCovid19> itVacResto = vacunasEnStock.iterator();
	    		while(itVacResto.hasNext()) {
	    			VacunaCovid19 vacuna = itVacResto.next();
	    			if( !(vacuna instanceof Pfizer || vacuna instanceof SputnikV) && persona.getTurno() == null) {
    					Fecha fecha = generarFechaTurno(fechaInicial);
    					Turno turno = new Turno(fecha,persona,vacuna);
    					persona.setTurno(turno);
    					persona.setVacuna(vacuna);
    					itVacResto.remove();
    					vacunasReservadas.add(vacuna);	    					
    					personasSinTurno.remove(persona.getDni());
    					personasConTurno.put(persona.getDni(), persona);
    					turnos.add(turno);
    				}
	    		}
	    		
	    	}
	    	
	    	
	  
	    	
    		
			/*
			 * System.out.println("SALUD : " + trabajadoresSalud.size());
			 * System.out.println("MAYOR 60 : " + mayoresDe60.size());
			 * System.out.println("ENFERMOS : " + conEnfermedadPreexistente.size());
			 * System.out.println("RESTO : " + resto.size());
			 */
	    	
	    }/*Retorna fecha usada para asignar turnos. Si no quedan turnos asgina fecha el dia siguiente.*/
	    private Fecha generarFechaTurno(Fecha fechaInicial) {
	    	if(turnosPorDia < 1) {
	    		fechaInicial.avanzarUnDia();
	    		turnosPorDia = capacidadVacunacionDiaria;
	    	}
	    	turnosPorDia--;
			return new Fecha(fechaInicial);
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
