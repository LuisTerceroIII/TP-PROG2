package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

public class Moderna extends VacunaCovid19 {
	
    private Fecha fechaVencimiento;

    
	public Moderna(Fecha fechaIngreso) {
		super(fechaIngreso);
		super.setName("Moderna");
		super.setExclusivaMayores60(false);
		setFechaVencimiento(fechaIngreso);
		super.setStoreTemperature(-18);
	}
	
	private void setFechaVencimiento(Fecha fechaIngreso) {
		this.fechaVencimiento = new Fecha(fechaIngreso);
		this.fechaVencimiento.avanzar60Dias();
	}

	public Fecha getFechaVencimiento() {
		return fechaVencimiento;
	}
	
	
	//Verdadero si esta vencida !
	public boolean estaVencida() {
		return Fecha.hoy().compareTo(getFechaVencimiento()) > 0;
	}
	
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString());
    	sb.append(", Fecha de Vencimiento : ").append(fechaVencimiento);
        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isExclusivaMayores60() ? 1231 : 1237);
		result = prime * result + ((super.getFechaIngreso() == null) ? 0 : super.getFechaIngreso().hashCode());
		result = prime * result + ((getFechaVencimiento() == null) ? 0 : fechaVencimiento.hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + getStoreTemperature();
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
		Moderna other = (Moderna) obj;
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
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (getStoreTemperature() != other.getStoreTemperature())
			return false;
		return true;
	}
	
	

}
