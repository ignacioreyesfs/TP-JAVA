package queMePongo.clima;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ProveedorClima {
	@Id @GeneratedValue
	private int id;
	public abstract double getTemperatura();
	public abstract double getTemperaturaFecha(LocalDateTime fecha);
}
