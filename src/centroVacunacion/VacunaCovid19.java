package centroVacunacion;

/*
 * -25 <= temperaturaAlmacenaje <= 5  -> En relacion a los frigorificos.
 * 
 * */
public abstract class VacunaCovid19 {
    private String nombre;
    protected int temperaturaAlmacenaje;
    private boolean exclusivaMayores60;
    private Fecha fechaIngreso;
    
    public VacunaCovid19(Fecha fechaIngreso) {
		super();
		this.setFechaIngreso(fechaIngreso);
	}

	public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTemperaturaAlmacenaje() {
        return temperaturaAlmacenaje;
    }

    public void setTemperaturaAlmacenaje(int temperaturaAlmacenaje) {
    	if(-25 <= temperaturaAlmacenaje && temperaturaAlmacenaje <= 5) {
    		this.temperaturaAlmacenaje = temperaturaAlmacenaje;
    	} else {
    		throw new RuntimeException("Temperatura de almacenaje no disponible");
    	}
        
    }
    
	public Fecha getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Fecha fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

    public boolean isExclusivaMayores60() {
        return exclusivaMayores60;
    }

    public void setExclusivaMayores60(boolean exclusivaMayores60) {
        this.exclusivaMayores60 = exclusivaMayores60;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Vacuna Nombre : ").append(nombre)
    	.append(", Temperatura de almacenaje : ").append(temperaturaAlmacenaje)
    	.append(", Fecha de ingreso : ").append(fechaIngreso);
    	
        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (exclusivaMayores60 ? 1231 : 1237);
		result = prime * result + ((fechaIngreso == null) ? 0 : fechaIngreso.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + temperaturaAlmacenaje;
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
		VacunaCovid19 other = (VacunaCovid19) obj;
		if (exclusivaMayores60 != other.exclusivaMayores60)
			return false;
		if (fechaIngreso == null) {
			if (other.fechaIngreso != null)
				return false;
		} else if (!fechaIngreso.equals(other.fechaIngreso))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (temperaturaAlmacenaje != other.temperaturaAlmacenaje)
			return false;
		return true;
	}
    
    




}
