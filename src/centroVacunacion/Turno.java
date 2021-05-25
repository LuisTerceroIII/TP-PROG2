package centroVacunacion;


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
    }
    public void asignarVacuna(VacunaCovid19 vacuna) {
        this.vacuna = vacuna;
    }
    public void asignarFecha(Fecha fecha) {
        this.fecha = fecha;
    }
    public Fecha getFecha() {
        return fecha;
    }
    public Persona getPersona() {
        return persona;
    }
    public VacunaCovid19 getVacuna() {
        return vacuna;
    }
	//Verdadero si esta vencida !
	public boolean estaVencido() {
		return Fecha.hoy().compareTo(fecha) > 0;
	}
}
