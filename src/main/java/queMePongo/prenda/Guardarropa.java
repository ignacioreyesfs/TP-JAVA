package queMePongo.prenda;

import queMePongo.usuario.Usuario;

import javax.persistence.*;
import java.util.*;

@Entity
public class Guardarropa {
	@Id @GeneratedValue
	private Integer id;
	@OneToMany(cascade = CascadeType.PERSIST) 
	@JoinColumn(name = "guardarropa_id")
	private Set<Prenda> prendas = new HashSet<Prenda>();
	private String nombre;
	
	public Guardarropa() {}
	
	public Guardarropa(String nombre) {
		this.nombre = nombre;
	}

	public Set<Prenda> getPrendas() {
		return prendas;
	}
	
	public Prenda getPrendaById(int idPrenda) {
		return this.getPrendas().stream().filter(p -> p.getId().equals(idPrenda)).findFirst().orElse(null);
	}

	public void agregarPrenda(Prenda prenda) {
		prendas.add(prenda);
	}
	
	public void eliminarPrenda(Prenda prenda) {
		prendas.remove(prenda);
	}

	public boolean contienePrenda(Prenda prenda) {
		return this.prendas.contains(prenda);
	}

	public int cantidadPrendas() {
		return prendas.size();
	}
	
	public void compartirCon(Usuario usuario){
		usuario.agregarGuardarropas(this);
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
