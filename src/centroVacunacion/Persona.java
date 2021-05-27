package centroVacunacion;

public class Persona {
    private int dni;
    private int edad;
    private boolean trabajadorSalud;
    private boolean enfermedadPreexistente;
    private int prioridad;// Numero del 1 al 4.
    private VacunaCovid19 vacuna;
    private Turno turno;

    public Persona(int dni, int edad, boolean trabajadorSalud, boolean enfermedadPreexistente) {
        this.dni = dni;
        this.edad = edad;
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
		if(this.isTrabajadorSalud() && this.isEnfermedadPreexistente() && this.getEdad() > 60) {
			return 2;
		} else if (this.isTrabajadorSalud() && this.getEdad() > 60) {
			return 2;
			
		} else if (this.isTrabajadorSalud() && this.isEnfermedadPreexistente()) {
			return 1;
			
		} else if(this.isTrabajadorSalud()) {
			return 1;
			
		} else if(this.getEdad() > 60 && this.isEnfermedadPreexistente()) {
			return 2;
		
		} else if(this.getEdad() > 60) {
			return 2;
		}
		
		else if(this.isEnfermedadPreexistente()) {
			return 3;
		} else {
			return 4;
		}
    }
    
    public int getDni() {
        return dni;
    }
    public void setDni(int dni) {
        this.dni = dni;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
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
		this.vacuna = vacuna;
	}
	public Turno getTurno() {
		return turno;
	}
	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	@Override
	public String toString() {
		
		return new StringBuilder()
				.append("Persona [ DNI :").append(dni)
				.append(", Edad :").append(edad)
				.append(", Trabajador de la salud : ").append(trabajadorSalud)
				.append(", Enfermedad Preexistente : ").append(enfermedadPreexistente)
				.append(", Vacuna : ").append(vacuna != null ? vacuna : "" )
				.append(", Turno : ").append(turno != null ? turno.getFecha() : "").append(" ]").toString();
	}
    
    

}
