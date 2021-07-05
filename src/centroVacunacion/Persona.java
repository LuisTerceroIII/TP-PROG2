package centroVacunacion;


/*
 * IREP : 
 	dni:  7 <= dni.toString().length() <= 9 -> reconoce números a partir de 1 millón, hasta 100 millones.
	edad : 18 <= edad <= 200
	trabajadorSalud : trabajadorSalud != null
	enfermedadPreexistente : enfermedadPreexistente != null
	prioridad : 1 <= prioridad <= 4
	vacuna : vacuna.equals(turno.getVacuna()) == true
	turno : turno.getVacuna().equals(vacuna) == true
 * */
public class Persona {
    private int dni;
    private int edad;
    private boolean trabajadorSalud;
    private boolean enfermedadPreexistente;
    private int prioridad;// Numero del 1 al 4.
    private VacunaCovid19 vacuna;
    private Turno turno;

    public Persona(int dni, int edad, boolean trabajadorSalud, boolean enfermedadPreexistente) {
    	this.setDni(dni);
    	this.setEdad(edad);
        this.trabajadorSalud = trabajadorSalud;
        this.enfermedadPreexistente = enfermedadPreexistente;
        this.vacuna = null;
        this.turno = null;
        this.prioridad = prioridad();
    }
    
    public int getPrioridad() {
    	return prioridad;
    }
    
    // Valores de retorno:
    //  Trabajadores de la salud = 1
    //  Mayores de 60 = 2
    //  Enfemedades preexistentes = 3
    //  Resto de la poblacion = 4
    private int prioridad() {
		if(this.isTrabajadorSalud()) {
			return 1;	
		} else if(this.getEdad() > 60) {
			return 2;
		}
		else if(this.isEnfermedadPreexistente()) {
			return 3;
		} else {
			return 4;
		}
    }

    // retorna fecha del turno asociado
    public Fecha fechaTurno() {
    	return turno.getFecha();
    }
    
    public String nombreVacunaAsignada() {
    	return vacuna.getNombre();
    }
    
    public int getDni() {
        return dni;
    }
    public void setDni(int dni) {
    	int lenDNI = String.valueOf(dni).length();
    	if(lenDNI >= 7 && lenDNI <= 9 ) {
    		this.dni = dni;
    	} else {
    		throw new RuntimeException("DNI invalido, verifique y vuelva a ingresar.");
    	}
        
    }
    public int getEdad() {
        return edad;
    }
    
    //Si bien lo que me importa es que sea mayor de edad, le pongo el limite de 200 por sentido comun.
    public void setEdad(int edad) {
    	if( edad >= 18 && edad <= 200) {
    		this.edad = edad;
    	} else {
    		throw new RuntimeException("Edad invalida, solo mayores de 18");
    	}
        
    }
    public boolean isTrabajadorSalud() {
        return trabajadorSalud;
    }
    public void setTrabajadorSalud(boolean trabajadorSalud) {
        this.trabajadorSalud = trabajadorSalud;
    }
    public boolean isEnfermedadPreexistente() {
        return enfermedadPreexistente;
    }
    public void setEnfermedadPreexistente(boolean enfermedadPreexistente) {
        this.enfermedadPreexistente = enfermedadPreexistente;
    }
    
	public VacunaCovid19 getVacuna() {
		return vacuna;
	}
	public void setVacuna(VacunaCovid19 vacuna) {
		if(turno != null) {
			this.vacuna = vacuna;
			turno.asignarVacuna(vacuna);
		}
	}
	public Turno getTurno() {
		return turno;
	}
	//Dado que la vacuna solo se puede obtener por medio de un turno, elegi hacerlo asi, se asigna una vacuna cuando se asigne un turno.
	public void setTurno(Turno turno) {
		this.vacuna = turno.getVacuna();
		this.turno = turno;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Persona [ DNI :").append(dni)
				.append(", Edad :").append(edad)
				.append(", Trabajador de la salud : ").append(trabajadorSalud)
				.append(", Enfermedad Preexistente : ").append(enfermedadPreexistente)
				.append(", Vacuna : ").append(vacuna != null ? vacuna.getNombre() : "" )
				.append(", Turno : ").append(turno != null ? turno.getFecha() : "").append(" ]").toString();
	}
}
