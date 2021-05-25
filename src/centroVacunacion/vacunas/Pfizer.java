package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

public class Pfizer extends VacunaCovid19 {
	
    private Fecha fechaVencimiento;

    
	public Pfizer(Fecha fechaIngreso) {
		super(fechaIngreso);
		super.setName("Pfizer");
		super.setExclusivaMayores60(true);
		setFechaVencimiento(fechaIngreso);
		super.setStoreTemperature(-18);
	}
	

	private void setFechaVencimiento(Fecha fechaIngreso) {
		this.fechaVencimiento = new Fecha(fechaIngreso);
		this.fechaVencimiento.avanzar30Dias();
	}

	public Fecha getFechaVencimiento() {
		return fechaVencimiento;
	}
	public boolean estaVencida() {
		return Fecha.hoy().compareTo(getFechaVencimiento()) > 0;
	}
}


