package centroVacunacion;

public class Persona {
    private int dni;
    private int edad;
    private boolean trabajadorSalud;
    private boolean enfermedadPreexistente;
    private VacunaCovid19 vacuna;
    private Turno turno;

    public Persona(int dni, int edad, boolean trabajadorSalud, boolean enfermedadPreexistente) {
        this.dni = dni;
        this.edad = edad;
        this.trabajadorSalud = trabajadorSalud;
        this.enfermedadPreexistente = enfermedadPreexistente;
    }
    public void asignarVacuna(VacunaCovid19 vacuna) {
        this.vacuna = vacuna;
    }
    public void asignarTurno(Turno turno) {
        this.turno = turno;
    }
    public VacunaCovid19 verVacuna() {
        return vacuna;
    }
    public Turno verTurno() {
        return turno;
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

}
