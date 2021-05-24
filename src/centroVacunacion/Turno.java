package centroVacunacion;

import java.time.LocalDate;

public class Turno {

    private LocalDate fecha;
    private Persona persona;
    private VacunaCovid19 vacuna;

    public Turno(LocalDate fecha, Persona persona, VacunaCovid19 vacuna) {
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
    public void asignarFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public Persona getPersona() {
        return persona;
    }
    public VacunaCovid19 getVacuna() {
        return vacuna;
    }
}
