package centroVacunacion;

/*
 * IREP:
    fecha : fecha.posterior(Fecha.hoy()) == true
    persona : persona.getVacuna().equals(vacuna) == true
    vacuna : vacuna.equals(persona.getVacuna()) == true
 * */
public class Turno {

    private Fecha fecha;
    private Persona persona;
    private VacunaCovid19 vacuna;
    

    public Turno(Fecha fecha, Persona persona, VacunaCovid19 vacuna) {
        this.fecha = fecha;
        this.persona = persona;
        this.vacuna = vacuna;
    }
    public void asignarPersona(Persona persona) {
        this.persona = persona;
        this.vacuna = persona.getVacuna();
    }
    public void asignarVacuna(VacunaCovid19 vacuna) {
        this.vacuna = vacuna;
        persona.setVacuna(vacuna);
    }
    public void asignarFecha(Fecha fecha) {
        if(fecha.posterior(Fecha.hoy())) {
            this.fecha = fecha;
        } else throw new RuntimeException("Fecha invalida, seleccione una fecha del futuro");
    }
    public Fecha getFecha() {
        return fecha;
    }
    public Persona getPersona() {
        return persona;
    }
    public int dniPersona() {
    	return persona.getDni();
    }
    public VacunaCovid19 getVacuna() {
        return vacuna;
    }
	//Verdadero si esta vencida !
	public boolean estaVencido() {
		return Fecha.hoy().compareTo(fecha) > 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Turno [ Fecha : " ).append(fecha)
		.append(", Persona : DNI : ").append(persona.getDni())
		.append(", Vacuna : ").append(vacuna.getNombre())
		.append(" ]");
		return sb.toString();
	}
	
	
}
