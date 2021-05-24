package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

public class Moderna extends VacunaCovid19 {
	
    private Fecha fechaVencimiento;

    
	public Moderna(Fecha fechaIngreso) {
		super(fechaIngreso);
		super.setExclusivaMayores60(false);
		setFechaVencimiento(fechaIngreso);
		super.setStoreTemperature(-18);
	}
	
	private void setFechaVencimiento(Fecha fechaIngreso) {
		this.fechaVencimiento = new Fecha(fechaIngreso);
		this.fechaVencimiento.avanzar30Dias();
		this.fechaVencimiento.avanzar30Dias();
	}

	public Fecha getFechaVencimiento() {
		return fechaVencimiento;
	}

}
