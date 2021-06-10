package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

/*
 * temperaturaAlmacenaje == -18
 *  exclusivaMayores60 == true
 * fechaIngreso.compareTo(fechaVencimiento) < 0 SIEMPRE
 * */
public class Pfizer extends VacunaCovid19 {
	
	private Fecha fechaIngreso;
    private Fecha fechaVencimiento;

	public Pfizer(Fecha fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		super.setNombre("Pfizer");
		super.setExclusivaMayores60(true);
		setFechaVencimiento(fechaIngreso);
		setTemperaturaAlmacenaje(-18);
	}
	@Override
	public void setTemperaturaAlmacenaje(int temperatura) {
		if(temperatura != -18) throw new RuntimeException("Pfizer debe almacenarse a -18 grados");
		this.temperaturaAlmacenaje = temperatura;
	}

	private void setFechaVencimiento(Fecha fechaIngreso) {
		this.fechaVencimiento = new Fecha(fechaIngreso);
		this.fechaVencimiento.avanzar30Dias();
	}

	public Fecha getFechaVencimiento() {
		return fechaVencimiento;
	}
	
	public Fecha getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Fecha fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public boolean estaVencida() {
		return Fecha.hoy().compareTo(getFechaVencimiento()) > 0;
	}
	
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString());
    	sb.append("Fecha de ingreso : ").append(fechaIngreso).append(", Fecha de Vencimiento : ").append(fechaVencimiento);
        return sb.toString();
    }
    
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isExclusivaMayores60() ? 1231 : 1237);
		result = prime * result + ((getFechaIngreso() == null) ? 0 : getFechaIngreso().hashCode());
		result = prime * result + ((getFechaVencimiento() == null) ? 0 : fechaVencimiento.hashCode());
		result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
		result = prime * result + getTemperaturaAlmacenaje();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pfizer other = (Pfizer) obj;
		if (isExclusivaMayores60() != other.isExclusivaMayores60())
			return false;
		if (getFechaIngreso() == null) {
			if (other.getFechaIngreso() != null)
				return false;
		} else if (!getFechaIngreso().equals(other.getFechaIngreso()))
			return false;
		if (getFechaVencimiento() == null) {
			if (other.getFechaVencimiento() != null)
				return false;
		} else if (!getFechaVencimiento().equals(other.getFechaVencimiento()))
			return false;
		if (getNombre() == null) {
			if (other.getNombre() != null)
				return false;
		} else if (!getNombre().equals(other.getNombre()))
			return false;
		if (getTemperaturaAlmacenaje() != other.getTemperaturaAlmacenaje())
			return false;
		return true;
	}
	
}


