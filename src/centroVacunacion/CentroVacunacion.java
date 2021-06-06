package centroVacunacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


import centroVacunacion.vacunas.Astrazeneca;
import centroVacunacion.vacunas.Moderna;
import centroVacunacion.vacunas.Pfizer;
import centroVacunacion.vacunas.Sinopharm;
import centroVacunacion.vacunas.SputnikV;


/*
 * IREP : 
 * personasConTurno.size() == turnos.size()
 * 0 <= turnosPorDia <= capacidadVacunacionDiaria  -> sucede en generarFechaTurno.
 * 
 * */
public class CentroVacunacion {
	
	  	private String nombreCentro;
	 	private HashMap<Integer,Persona> personasSinTurno;
	 	private HashMap<Integer,Persona> personasConTurno;
	    private HashMap<Integer,Persona> personasVacunadas; // Cuando se implemente una funcion "vacunar" tomara las personas de la lista personasConTurno y los movera a este conjunto.
	    private DepositoVacunas deposito;
	    private HashMap<String,Integer> vacunasVencidas;
	    private HashSet<Turno> turnos;
	    private int capacidadVacunacionDiaria;
	    private int turnosPorDia; // creo esta copia para no modificar la variable de instancia y no perder el valor dado al inicio cuando tenga que "recargar" los cupos al avanzar de dia.
	    private int turnosAsignados; // Contador historico de cuantos turnos se han dado.
	    
	    
	    public HashMap<Integer, Persona> getPersonasSinTurno() {
			return personasSinTurno;
		}
	    

		public HashMap<Integer, Persona> getPersonasConTurno() {
			return personasConTurno;
		}


		/**
	    * Constructor.
	    * recibe el nombre del centro y la capacidad de vacunaci�n diaria.
	    * Si la capacidad de vacunaci�n no es positiva se debe generar una excepci�n.
	    * Si el nombre no est� definido, se debe generar una excepci�n.
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
		    	this.deposito = new DepositoVacunas(new Frigorifico(3, 50000),new Frigorifico(-18, 50000));
		    	this.vacunasVencidas = new HashMap<>();
		    	this.turnos = new HashSet<>();
		    	this.turnosAsignados = 0;	
		    	vacunasVencidas.put("Moderna", 0);
		    	vacunasVencidas.put("Pfizer", 0);
	    	}
	    }
	    
	    /**
	    * Solo se pueden ingresar los tipos de vacunas planteados en la 1ra parte.
	    * Si el nombre de la vacuna no coincidiera con los especificados se debe generar
	    * una excepci�n.
	    * Tambi�n se genera excepci�n si la cantidad es negativa.
	    * La cantidad se debe
	    * sumar al stock existente, tomando en cuenta las vacunas ya utilizadas.
	    */
	    public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) {
	    	if(cantidad <= 0) throw new RuntimeException("Cantidad de vacunas debe ser mayor a 0");
	    	switch (nombreVacuna.toUpperCase()) {
			case "ASTRAZENECA":
				for (int i = 0; i < cantidad ; i++) {
					deposito.getVacunasEnStock().add(new Astrazeneca(fechaIngreso));
					deposito.almacenar("ASTRAZENECA");
				}
				break;
				
			case "MODERNA":
				for (int i = 0; i < cantidad ; i++) {
					deposito.getVacunasEnStock().add(new Moderna(fechaIngreso));
					deposito.almacenar("MODERNA");
				}
				break;
				
			case "PFIZER":
				for (int i = 0; i < cantidad ; i++) {
					deposito.getVacunasEnStock().add(new Pfizer(fechaIngreso));
					deposito.almacenar("PFIZER");
				}
				break;
				
			case "SINOPHARM":
				for (int i = 0; i < cantidad ; i++) {
					deposito.getVacunasEnStock().add(new Sinopharm(fechaIngreso));
					deposito.almacenar("SINOPHARM");
				}
				break;
				
			case "SPUTNIK":
				for (int i = 0; i < cantidad ; i++) {
					deposito.getVacunasEnStock().add(new SputnikV(fechaIngreso));
					deposito.almacenar("SPUTNIK");
				}
				break;
				
			default:
				throw new RuntimeException("Vacuna no se reconoce");
			}
	    	
	    }
	
	    /**
	    * total de vacunas disponibles no vencidas sin distinci�n por tipo.
	    */
	    public int vacunasDisponibles() {
	    	int total = deposito.getVacunasEnStock().size();
	    	Iterator<VacunaCovid19> it = deposito.getVacunasEnStock().iterator();
	    	while(it.hasNext()) {
	    		VacunaCovid19 vacuna = it.next();
	    		if(vacuna instanceof Pfizer) {
	    			Pfizer pfizer = (Pfizer) vacuna;
	    			if(pfizer.estaVencida()) {
	    				total--;
	    			}
	    		} else if(vacuna instanceof Moderna) {
	    			Moderna moderna = (Moderna) vacuna;
	    			if(moderna.estaVencida()) {
	    				total--;
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
	    	Iterator<VacunaCovid19> it = deposito.getVacunasEnStock().iterator();
	    	while(it.hasNext()) {
	    		VacunaCovid19 vacuna = it.next();
	    		if(vacuna.getName().toUpperCase().equals(nombreVacuna.toUpperCase()) ) {
	    			disponibles++;
	    			if(vacuna instanceof Pfizer) {
	    				Pfizer pfizer = (Pfizer) vacuna;
	    				if(pfizer.estaVencida()) {
	    					 
	    					 disponibles--;
	    				}
	    			} else if(vacuna instanceof Moderna) {
	    				Moderna moderna = (Moderna) vacuna;
	    				if(moderna.estaVencida()) {
	    					
	    					disponibles--;
	    				}
	    			}
	    		}
	    	}
	    	return disponibles;
		}
	    
	    /**
	    * Se inscribe una persona en lista de espera.
	    * Si la persona ya se encuentra inscripta o es menor de 18 a�os, se debe
	    * generar una excepci�n.
	    * Si la persona ya fue vacunada, tambi�n debe generar una excepci�n.
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
	    * Si no quedan inscriptos sin vacunas debe devolver una lista vac�a.
	    */
	    public java.util.List<Integer> listaDeEspera() {
	    	ArrayList<Integer> listaEspera = new ArrayList<>();
	    	listaEspera.addAll(this.personasSinTurno.keySet());
	    	return listaEspera;
	    }

	    /**
	    * Primero se verifica si hay turnos vencidos. En caso de haber turnos
	    * vencidos, la persona que no asisti� al turno debe ser borrada del sistema
	    * y la vacuna reservada debe volver a estar disponible.
	    *
	    * Segundo, se deben verificar si hay vacunas vencidas y quitarlas del sistema.
	    *
	    * Por �ltimo, se procede a asignar los turnos a partir de la fecha inicial
	    * recibida seg�n lo especificado en la 1ra parte.
	    * Cada vez que se registra un nuevo turno, la vacuna destinada a esa persona
	    * dejar� de estar disponible. Dado que estar� reservada para ser aplicada
	    * el d�a del turno.
	    *
	    *
	    */
	    public void generarTurnos(Fecha fechaInicial) { 
	    	if(fechaInicial.anterior(Fecha.hoy())) {
	    		throw new RuntimeException("La fecha es anterior a la de hoy !");
	    	}
	    	
	    	/* Verificar turnos vencidos*/
	    	eliminarTurnosVencidos();
	    	
	    	/*Verificar vacunas vencidas*/
	    	verificarVencimientoVacunas();
	    	
	    	/*Generacion de turnos !*/
	    	/*1er Parte: Division de las personas inscriptas en categorias*/
	    	HashSet<Persona> trabajadoresSalud = new HashSet<>();
	    	HashSet<Persona> mayoresDe60 = new HashSet<>();
	    	HashSet<Persona> conEnfermedadPreexistente = new HashSet<>();
	    	HashSet<Persona> resto = new HashSet<>();

	    	Iterator<Integer> itPersonas = personasSinTurno.keySet().iterator();
	    	while(itPersonas.hasNext()) {
	    		Persona persona = personasSinTurno.get(itPersonas.next());
	    		switch (persona.getPrioridad()) {
				case 1:
					trabajadoresSalud.add(persona);
					break;
				case 2:
					mayoresDe60.add(persona);
					break;
				case 3:
					conEnfermedadPreexistente.add(persona);
					break;
				default:
					resto.add(persona);
					break;
				}
	    	}
	    	/* ----------Fin 1er. Parte--------- */
	    	
	    	/*2da Parte: Distribucion de los turnos*/
			fechaInicial = asignarTurnos(trabajadoresSalud,fechaInicial);
			fechaInicial = asignarTurnos(mayoresDe60,fechaInicial);
			fechaInicial = asignarTurnos(conEnfermedadPreexistente,fechaInicial);
			fechaInicial = asignarTurnos(resto,fechaInicial);
	    }
	    
	    
	 
	    private void verificarVencimientoVacunas() {
	    	Iterator<VacunaCovid19> itVacunas = deposito.getVacunasEnStock().iterator();
	    	while(itVacunas.hasNext()) {
	    		VacunaCovid19 vacuna = itVacunas.next();
	    		if(vacuna instanceof Pfizer) {
	    			Pfizer pfizer = (Pfizer) vacuna;
	    			if(pfizer.estaVencida()) {
	    				itVacunas.remove();
	    				Integer cantVencidas = vacunasVencidas.get(pfizer.getName());
	    				cantVencidas++;
	    				vacunasVencidas.put(pfizer.getName(), cantVencidas);
	    			} 
	    		} else if(vacuna instanceof Moderna) {
	    			Moderna moderna = (Moderna) vacuna;
	    			if(moderna.estaVencida()) {
	    				itVacunas.remove();
	    				Integer cantVencidas = vacunasVencidas.get(moderna.getName());
	    				cantVencidas++;
	    				vacunasVencidas.put(moderna.getName(), cantVencidas);
	    			}
	    		}
	    	}		
		}

		private void eliminarTurnosVencidos() {
	    	Iterator<Turno> itTurnos = turnos.iterator();
	    	while(itTurnos.hasNext()) {
	    		Turno turno = itTurnos.next();
	    		if(turno.estaVencido()) {
	    			int dni = turno.getPersona().getDni();
	    			VacunaCovid19 vacuna = turno.getVacuna();
	    			personasConTurno.remove(dni);
	    			deposito.getVacunasReservadas().remove(vacuna);
	    			deposito.getVacunasEnStock().add(vacuna);
	    			itTurnos.remove();
	    		}
	    	}
			
		}

		//Recibe fecha para generar turno, si no hay turnos disponibles se asgina fecha para "ma�ana"
	    private Fecha generarFechaTurno(Fecha fechaInicial) {
	    	Fecha fecha = new Fecha(fechaInicial);
	    	if(turnosPorDia < 1) {
	    		fecha.avanzarUnDia();
	    		turnosPorDia = capacidadVacunacionDiaria;
	    	}
	    	turnosPorDia--;
	    	return fecha;
		}
	    
	    private Fecha asignarTurnos(HashSet<Persona> grupo,Fecha fechaInicial) {
    	  	Iterator<Persona> itGrupo = grupo.iterator();
	    	while(itGrupo.hasNext()) {
	    		Persona persona = itGrupo.next();
	    		Iterator<VacunaCovid19> itVacunas = deposito.getVacunasEnStock().iterator();
	    		
	    		while(itVacunas.hasNext()) {
	    			VacunaCovid19 vacuna = itVacunas.next();
	    			if(persona.getPrioridad() == 2) {
	    				if((vacuna instanceof Pfizer || vacuna instanceof SputnikV) && persona.getTurno() == null) {
	    					Fecha fecha = generarFechaTurno(fechaInicial);
	    					fechaInicial = fecha;
	    					Turno turno = new Turno(fecha,persona,vacuna);
	    					persona.setTurno(turno);
	    					itVacunas.remove();
	    					deposito.getVacunasReservadas().add(vacuna);	    					
	    					personasSinTurno.remove(persona.getDni());
	    					personasConTurno.put(persona.getDni(), persona);
	    					turnos.add(turno);
	    					this.turnosAsignados++;
	    				}
	    			} else {
		    			if( !(vacuna instanceof Pfizer || vacuna instanceof SputnikV) && persona.getTurno() == null) {
	    					Fecha fecha = generarFechaTurno(fechaInicial);
	    					fechaInicial = fecha;
	    					Turno turno = new Turno(fecha,persona,vacuna);
	    					persona.setTurno(turno);
	    					itVacunas.remove();
	    					deposito.getVacunasReservadas().add(vacuna);	    					
	    					personasSinTurno.remove(persona.getDni());
	    					personasConTurno.put(persona.getDni(), persona);
	    					turnos.add(turno);
	    					this.turnosAsignados++;
	    				}
	    			}
	    		}   
	    	}
	    	return fechaInicial;
	    } 
	    
	    
	    
		/**
	    * Devuelve una lista con los dni de las personas que tienen turno asignado
	    * para la fecha pasada por par�metro.
	    * Si no hay turnos asignados para ese d�a, se debe devolver una lista vac�a.
	    * La cantidad de turnos no puede exceder la capacidad por d�a de la ungs.
	    */
	    public java.util.List<Integer> turnosConFecha(Fecha fecha) {
	    	ArrayList<Integer> dnis = new ArrayList<>();
	    	for(Turno turno : turnos) {
	    		if(turno.getFecha().equals(fecha)) {
	    			dnis.add(turno.getPersona().getDni());
	    		}
	    	}   	
	    	return dnis;
	    }
	    
	    /*** Dado el DNI de la persona y la fecha de vacunaci�n
	    * se valida que est� inscripto y que tenga turno para ese dia.
	    * - Si tiene turno y est� inscripto se debe registrar la persona como
	    * vacunada y la vacuna se quita del dep�sito.
	    * - Si no est� inscripto o no tiene turno ese d�a, se genera una Excepcion.
	    */
	    public void vacunarInscripto(int dni, Fecha fechaVacunacion) {
	    	if(personasConTurno.containsKey(dni)) {
	    		Persona persona = personasConTurno.get(dni);
	    		if(persona.getTurno().getFecha().equals(fechaVacunacion)) {
	    			personasConTurno.remove(dni);
	    			personasVacunadas.put(dni, persona);
	    			deposito.getVacunasReservadas().remove(persona.getVacuna());
	    			deposito.getVacunasAplicadas().add(persona.getVacuna());
					deposito.liberarUnEspacioFrigorifico(persona.getVacuna());
					turnos.remove(persona.getTurno());
				} else {throw new RuntimeException("Persona no tiene turno hoy");}

	    	} else { throw new RuntimeException("Persona no tiene turno");}   	
	    }

	/**
	    * Devuelve un Diccionario donde
	    * - la clave es el dni de las personas vacunadas
	    * - Y, el valor es el nombre de la vacuna aplicada.
	    */
	    public Map<Integer, String> reporteVacunacion() {
	    	HashMap<Integer,String> vacunados = new HashMap<>();
	    	Iterator<Integer> itPersonas = personasVacunadas.keySet().iterator();
	    	while(itPersonas.hasNext()) {
	    		Persona persona = personasVacunadas.get(itPersonas.next());
	    		vacunados.put(persona.getDni(),persona.getVacuna().getName());
	    	}
	    	return vacunados;
	    }
	    
	    /**
	    * Devuelve en O(1) un Diccionario:
	    * - clave: nombre de la vacuna
	    * - valor: cantidad de vacunas vencidas conocidas hasta el momento.
	    */
	    public Map<String, Integer> reporteVacunasVencidas() {
	    	return vacunasVencidas;
	    }
	    
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Nombre del centro : ").append(nombreCentro).append(". \n");
			sb.append("Capacidad de vacunacion diaria : ").append(capacidadVacunacionDiaria).append(". \n");
			sb.append("Cantidad de vacunas disponibles : ").append(vacunasDisponibles()).append(". \n");
			sb.append("Cantidad de personas en lista de espera : ").append(listaDeEspera().size()).append(". \n");
			sb.append("Cantidad de turnos asignados  : ").append(turnosAsignados).append(". \n");
			sb.append("Cantidad de vacunas aplicadas : ").append(personasVacunadas.size()).append(". \n");
			return sb.toString();
		}
	    
	    
	    
	    
}
