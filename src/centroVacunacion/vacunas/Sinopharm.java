package centroVacunacion.vacunas;

import centroVacunacion.VacunaCovid19;

/*
IREP:
	temperaturaAlmacenaje: temperaturaAlmacenaje == 3
	exclusivaMayores60 : exclusivaMayores60 == false
 * */
public class Sinopharm extends VacunaCovid19 {

	public Sinopharm() {
		super.setNombre("Sinopharm");
		super.setExclusivaMayores60(false);
		setTemperaturaAlmacenaje(3);
	}
	@Override
	public void setTemperaturaAlmacenaje(int temperatura) {
		if(temperatura != 3) throw new RuntimeException("Sinopharm debe almacenarse a 3 grados");
		this.temperaturaAlmacenaje = temperatura;
	}
	
}
