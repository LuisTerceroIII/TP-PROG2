package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

public class SputnikV extends VacunaCovid19 {

	public SputnikV(Fecha fechaIngreso) {
		super(fechaIngreso);
		super.setName("Sputnik");
		super.setExclusivaMayores60(true);
		super.setStoreTemperature(3);
	}
	
}
