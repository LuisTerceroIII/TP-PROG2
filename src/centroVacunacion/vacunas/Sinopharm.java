package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

public class Sinopharm extends VacunaCovid19 {

	public Sinopharm(Fecha fechaIngreso) {
		super(fechaIngreso);
		super.setName("Sinopharm");
		super.setExclusivaMayores60(false);
		super.setStoreTemperature(3);
	}
	
}
