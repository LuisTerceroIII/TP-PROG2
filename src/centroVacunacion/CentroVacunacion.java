package centroVacunacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import centroVacunacion.vacunas.Pfizer;
import centroVacunacion.vacunas.SputnikV;

/*
 * IREP : 
	nombreCentro: nombreCentro.length() > 1 && nombreCentro != null
	deposito: deposito != null
	controlTurnos : controlTurnos != null
	capacidadVacunacionDiaria: 0 < capacidadDiaria < 1000
	turnosPorDia: 0 <= turnosPorDia <= capacidadVacunacionDiaria
	turnosAsignados: 0 <= turnosAsignados <= personasVacunadas.size()

 * */
public class CentroVacunacion {
	
	  	private String nombreCentro;
	    private DepositoVacunas deposito;//De aqui se sacan las vacunas.
		private ControlTurnos controlTurnos;// De aqui se sacan los turnos y las personas.
	    private int capacidadVacunacionDiaria;
		private int turnosPorDia; // creo esta copia para no modificar la variable de instancia y no perder el valor dado al inicio cuando tenga que "recargar" los cupos al avanzar de dia.
	    private int turnosAsignados; // Contador historico de cuantos turnos se han dado.

	    /*Constructor. Recibe el nombre del centro y la capacidad de vacunacion diaria.
	    * Si la capacidad de vacunacion no es positiva se genera una excepcion.
	    * Si el nombre no esta definido, se genera una excepcion.*/
 	    public CentroVacunacion(String nombreCentro, int capacidadVacunacionDiaria) {
	    	if(nombreCentro == null || nombreCentro.length() < 1) {
	    		throw new RuntimeException("Nombre invalido");
	    	} else if(capacidadVacunacionDiaria < 0) {
	    		throw new RuntimeException("Capacidad de vacunacion no puede ser menor a 0.");
	    	} else {
	    		this.nombreCentro = nombreCentro;
		    	setCapacidadVacunacionDiaria(capacidadVacunacionDiaria);
		    	setTurnosPorDia(capacidadVacunacionDiaria);
		    	this.controlTurnos = new ControlTurnos();
		    	this.deposito = new DepositoVacunas();
		    	this.turnosAsignados = 0;	
	    	}
	    }

	    /* Solo se pueden ingresar los tipos de vacunas planteados en la 1ra parte.
	    * Si el nombre de la vacuna no coincide con los especificados se debe generar una excepcion.
	    * Tambien se genera excepcion si la cantidad es negativa.
	    * La cantidad se suma al stock existente*/
	    public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) {
	    	if(cantidad <= 0) throw new RuntimeException("Cantidad de vacunas debe ser mayor a 0");
			for (int i = 0; i < cantidad ; i++) {
				switch (nombreVacuna.toUpperCase()) {
					case "ASTRAZENECA":
						deposito.ingresarAstrazeneca(cantidad);
						break;
					case "MODERNA":
						deposito.ingresarModerna(cantidad,fechaIngreso);
						break;
					case "PFIZER":
						deposito.ingresarPfizer(cantidad,fechaIngreso);
						break;
					case "SINOPHARM":
						deposito.ingresarSinopharm(cantidad);
						break;
					case "SPUTNIK":
						deposito.ingresarSputnikV(cantidad);
						break;
					default:
						throw new RuntimeException("Vacuna no se reconoce");
				}
			}
	    }

	    //Total de vacunas disponibles no vencidas sin distincion por tipo.
	    public int vacunasDisponibles() {
	    	int total = deposito.cantidadVacunasEnStock();
	    	Iterator<VacunaCovid19> it = deposito.getIteratorVacunasEnStock();
	    	while(it.hasNext()) {
	    		VacunaCovid19 vacuna = it.next();
	    		if(vacuna.estaVencida()) {
	    			total--;
	    		}
	    	}
	    	return total;
	    }


	/*Total de vacunas disponibles no vencidas que coincida con el nombre de
      vacuna especificado. */
	    public int vacunasDisponibles(String nombreVacuna) {
	    	int disponibles = 0;
	    	Iterator<VacunaCovid19> it = deposito.getIteratorVacunasEnStock();
	    	while(it.hasNext()) {
	    		VacunaCovid19 vacuna = it.next();
	    		if(vacuna.getNombre().toUpperCase().equals(nombreVacuna.toUpperCase()) ) {
	    			disponibles++;
	    			if(vacuna.estaVencida()) {
   					 disponibles--;
	    			}
	    		}
	    	}
	    	return disponibles;
		}

	    /*Se inscribe una persona en lista de espera.
	    * Si la persona ya se encuentra inscripta o es menor de 18 annios, se genera una excepcion.
	    * Si la persona ya fue vacunada, tambien genera una excepcion.*/
	    public void inscribirPersona(int dni, Fecha nacimiento, boolean tienePadecimientos, boolean esEmpleadoSalud) {
	    	int edad = Fecha.diferenciaAnios(Fecha.hoy(), nacimiento);
	    	if(edad < 18) {
	    		throw new RuntimeException("Debe ser mayor de edad para inscribirse.");
	    	} else if(controlTurnos.personaEstaInscripta(dni)) {
	    		throw new RuntimeException("Persona ya inscripta.");
	    	} else if(controlTurnos.personaEstaVacunada(dni)) {
	    		throw new RuntimeException("Persona ya fue vacunada.");
	    	} else {
				controlTurnos.agregarPersonaSinTurno(dni, tienePadecimientos, esEmpleadoSalud, edad);
			}
	    }

	/*Devuelve una lista con los DNI de todos los inscriptos que no se vacunaron y que no tienen turno asignado.
	    * Si no quedan inscriptos sin vacunas debe devolver una lista vacia*/
	    public java.util.List<Integer> listaDeEspera() {
	    	ArrayList<Integer> listaEspera = new ArrayList<>();
	    	listaEspera.addAll(controlTurnos.getDNIsPersonasSinTurno());
	    	return listaEspera;
	    }

	    /* Primero se verifica si hay turnos vencidos. En caso de haber turnos
	    * vencidos, la persona que no asistio al turno se borra del sistema
	    * y la vacuna reservada vuelve a estar disponible.
	    *
	    * Segundo, se verifica si hay vacunas vencidas y se quitan del sistema.
	    *
	    * Por utimo, se procede a asignar los turnos a partir de la fecha inicial
	    * recibida segun lo especificado en la 1ra parte.
	    * Cada vez que se registra un nuevo turno, la vacuna destinada a esa persona
	    * deja de estar disponible. Dado que estara reservada para ser aplicada
	    * el dia del turno.*/
	    public void generarTurnos(Fecha fechaInicial) {
	    	if(fechaInicial.anterior(Fecha.hoy())) throw new RuntimeException("La fecha es anterior a la de hoy !");
	    	/* Verificar turnos vencidos*/
	    	controlTurnos.verificarTurnosVencidos(deposito);
	    	/*Verificar vacunas vencidas*/
	    	deposito.verificarVencimientoVacunas();
	    	/*Generacion de turnos !*/
	    	/*1er Parte: Division de las personas inscriptas en categorias*/
	    	HashSet<Persona> trabajadoresSalud = new HashSet<>();
	    	HashSet<Persona> mayoresDe60 = new HashSet<>();
	    	HashSet<Persona> conEnfermedadPreexistente = new HashSet<>();
	    	HashSet<Persona> resto = new HashSet<>();

			for (Integer dni : controlTurnos.getDNIsPersonasSinTurno()) {
				Persona persona = controlTurnos.getPersonaSinTurnoPorDNI(dni);
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
	    	/*2da Parte: Distribucion de los turnos*/
			fechaInicial = asignarTurnos(trabajadoresSalud,fechaInicial);
			fechaInicial = asignarTurnos(mayoresDe60,fechaInicial);
			fechaInicial = asignarTurnos(conEnfermedadPreexistente,fechaInicial);
			asignarTurnos(resto,fechaInicial);
	    }

	    private Fecha asignarTurnos(HashSet<Persona> grupo,Fecha fechaInicial) {
    	  	Iterator<Persona> itGrupo = grupo.iterator();
	    	while(itGrupo.hasNext()) {
	    		Persona persona = itGrupo.next();
	    		Iterator<VacunaCovid19> itVacunas = deposito.getIteratorVacunasEnStock();
	    		while(itVacunas.hasNext()) {
	    			VacunaCovid19 vacuna = itVacunas.next();
	    			boolean esFitzerOSputnikV = (vacuna instanceof Pfizer || vacuna instanceof SputnikV);
	    			boolean noTieneTurno = persona.getTurno() == null;

					if (personaMayor60(persona) && esFitzerOSputnikV && noTieneTurno){
						fechaInicial = asignarTurnoAux(fechaInicial, persona, itVacunas, vacuna);
					} else if(!personaMayor60(persona) && !esFitzerOSputnikV && noTieneTurno){
						fechaInicial = asignarTurnoAux(fechaInicial, persona, itVacunas, vacuna);
					}
	    		}
	    	}
	    	return fechaInicial;
	    }

	    /*Este metodo nace de extraer logica duplicada en asignarTurno().
		  Hace toda la logica que conlleva asignar el turno: genera una fecha, crea un turno, asigna el turno a la persona,
		  elimina la vacuna de las vacunas disponibles, agrega la vacuna a las vacunas reservadas, cambia a la persona de la lista
		  sin turno a las que si tienen, se agrega el turno a "turnos" y se aumenta la variable que cuenta los turnos dados.
		  de personas sin turno, etc.*/
		private Fecha asignarTurnoAux(Fecha fechaInicial, Persona persona, Iterator<VacunaCovid19> itVacunas, VacunaCovid19 vacuna) {
			Fecha fecha = generarFechaTurno(fechaInicial);
			fechaInicial = fecha;
			Turno turno = new Turno(fecha,persona,vacuna);
			persona.setTurno(turno);
			itVacunas.remove();
			deposito.agregarVacunaAReservadas(vacuna);
			controlTurnos.eliminarPersonaSinTurno(persona);
			controlTurnos.agregarPersonaConTurno(persona);
			controlTurnos.agregarTurno(turno);
			incrementarTurnosAsignados();
			return fechaInicial;
	}

	/* Devuelve una lista con los dni de las personas que tienen turno asignado para la fecha pasada por parametro.
	    * Si no hay turnos asignados para ese dia, se  devuelve una lista vacia.
	    * La cantidad de turnos no puede exceder la capacidad por dia de la ungs.*/
	    public java.util.List<Integer> turnosConFecha(Fecha fecha) {
	    	ArrayList<Integer> dnis = new ArrayList<>();
	    	for(Turno turno : controlTurnos.getTurnos()) {
	    		if(turno.getFecha().equals(fecha)) {
	    			dnis.add(turno.dniPersona());
	    		}
	    	}
	    	return dnis;
	    }

	    /* Dado el DNI de la persona y la fecha de vacunacion
	    * se valida que este inscripto y que tenga turno para ese dia.
	    * - Si tiene turno y esta inscripto se registrar la persona como vacunada y la vacuna se quita del deposito.
	    * - Si no esta inscripto o no tiene turno ese dia, se genera una Excepcion.
	    */
		public void vacunarInscripto(int dni, Fecha fechaVacunacion) {
	    	if(controlTurnos.personaTieneTurno(dni)) {
	    		Persona persona = controlTurnos.getPersonaConTurnoPorDNI(dni);
	    		if(persona.fechaTurno().equals(fechaVacunacion)) {
					controlTurnos.eliminarPersonaConTurno(dni);
					controlTurnos.agregarPersonaVacunada(persona);
					deposito.eliminarVacunaReservada(persona.getVacuna());
	    			deposito.agregarVacunaAAplicadas(persona.getVacuna());
					deposito.liberarUnEspacioFrigorifico(persona.getVacuna());
					controlTurnos.eliminarTurno(persona);
				} else {throw new RuntimeException("Persona no tiene turno hoy");}

	    	} else { throw new RuntimeException("Persona no tiene turno");}   	
	    }

	/* Devuelve un Diccionario donde
	    * - la clave es el dni de las personas vacunadas
	    * - Y, el valor es el nombre de la vacuna aplicada. */
	    public Map<Integer, String> reporteVacunacion() {
	    	HashMap<Integer,String> vacunados = new HashMap<>();
			for (Integer dni : controlTurnos.getDNIsPersonasVacunadas()) {
				Persona persona = controlTurnos.getPersonaVacunadaPorDNI(dni);
				vacunados.put(persona.getDni(), persona.nombreVacunaAsignada());
			}
	    	return vacunados;
	    }

	/* Devuelve en O(1) un Diccionario:
	    * - clave: nombre de la vacuna
	    * - valor: cantidad de vacunas vencidas conocidas hasta el momento. */
	    public Map<String, Integer> reporteVacunasVencidas() {
	    	return deposito.getVacunasVencidas();
	    }

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Nombre del centro : ").append(nombreCentro).append(". \n");
			sb.append("Capacidad de vacunacion diaria : ").append(capacidadVacunacionDiaria).append(". \n");
			sb.append("Cantidad de vacunas disponibles : ").append(vacunasDisponibles()).append(". \n");
			sb.append("Cantidad de personas en lista de espera : ").append(listaDeEspera().size()).append(". \n");
			sb.append("Cantidad de turnos asignados  : ").append(turnosAsignados).append(". \n");
			sb.append("Cantidad de vacunas aplicadas : ").append(controlTurnos.cantidadPersonasVacunadas()).append(". \n");
			return sb.toString();
		}

		//Funciones AUX :

		/*Recibe fecha para generar turno, si no hay turnos disponibles se asgina fecha para "manana"*/
		private Fecha generarFechaTurno(Fecha fechaInicial) {
			Fecha fecha = new Fecha(fechaInicial);
			if(getTurnosPorDia() < 1) {
				fecha.avanzarUnDia();
				setTurnosPorDia(getCapacidadVacunacionDiaria());
			}
			setTurnosPorDia(getTurnosPorDia() - 1);
			return fecha;
		}

		private boolean personaMayor60(Persona persona) {
			return persona.getPrioridad() == 2 || persona.getEdad() > 60;
		}

		private int getCapacidadVacunacionDiaria() {
			return capacidadVacunacionDiaria;
		}

		private void setCapacidadVacunacionDiaria(int capacidadVacunacionDiaria) {
			if(capacidadVacunacionDiaria > 0 && capacidadVacunacionDiaria < 1000) {
				this.capacidadVacunacionDiaria = capacidadVacunacionDiaria;
			} else throw new RuntimeException("Capacidad de vacunatorio aceptada es entre 1 y 999");

		}

		private int getTurnosPorDia() {
			return turnosPorDia;
		}

		private void setTurnosPorDia(int turnosPorDia) {
			if(turnosPorDia >= 0 && turnosPorDia <= getCapacidadVacunacionDiaria()) {
				this.turnosPorDia = turnosPorDia;
			}
		}

		private void incrementarTurnosAsignados() {
			this.turnosAsignados++;
		}
}
