package centroVacunacion;

public abstract class VacunaCovid19 {
    private String name;
    private int storeTemperature;
    private boolean exclusivaMayores60;
    private Fecha fechaIngreso;
    
    public VacunaCovid19(Fecha fechaIngreso) {
		super();
		this.setFechaIngreso(fechaIngreso);
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoreTemperature() {
        return storeTemperature;
    }

    public void setStoreTemperature(int storeTemperature) {
        this.storeTemperature = storeTemperature;
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
        return   "Vacuna : '" + name + '\'' +
                ", Temperatura de almacenaje : " + storeTemperature;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (exclusivaMayores60 ? 1231 : 1237);
		result = prime * result + ((fechaIngreso == null) ? 0 : fechaIngreso.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + storeTemperature;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (storeTemperature != other.storeTemperature)
			return false;
		return true;
	}
    
    




}
