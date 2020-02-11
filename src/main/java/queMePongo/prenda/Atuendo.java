package queMePongo.prenda;

import queMePongo.usuario.Usuario;

import javax.persistence.*;

import java.awt.Color;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Atuendo {
	@Id @GeneratedValue
	private Integer id;
	@ManyToMany(cascade = CascadeType.PERSIST) 
    private Set<Prenda> prendas;
	@Transient
	private List<Prenda> cabeza;
	@Transient
	private List<Prenda> cuello;
	@Transient
	private List<Prenda> superior;
	@Transient
	private List<Prenda> inferior;
	@Transient
	private List<Prenda> manos;
	@Transient
	private List<Prenda> calzado;

    public Atuendo() {}

    public Atuendo(Set<Prenda> prendas) {
        this.prendas = prendas;
    }

    public Set<Prenda> getPrendas() {
        return prendas;
    }

	public boolean adecuadoTemperatura(Usuario usuario, double temperatura) {
		double temperaturaInicioCalor = 20;
		if (temperatura >= temperaturaInicioCalor) {
			return this.sePuedeUsarATemperatura(usuario, temperatura);
		} else {
			return this.poseeSuficienteAbrigo(usuario, temperatura);
		}
	}
    
    public boolean poseeSuficienteAbrigo(Usuario usuario, double temperatura) {
    	return prendas.stream().filter(prenda -> prenda.getCategoria() == Categoria.SUPERIOR).
    				anyMatch(prenda -> prenda.sePuedeUsarATemperatura(usuario, temperatura) && !prenda.esSuperiorBasica())
				&& sePuedeUsarATemperatura(usuario, temperatura);
	}

	private boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
		return this.getPrendas().stream().allMatch(prenda -> prenda.sePuedeUsarATemperatura(usuario, temperatura));
	}

	public boolean estaDisponible() {
		return this.getPrendas().stream().allMatch(prenda -> prenda.estaDisponible());
	}

	public void ocuparPrendas() { prendas.forEach(prenda -> prenda.setDisponibilidad(false));}

	public void liberarPrendas() {
		prendas.forEach(prenda -> prenda.setDisponibilidad(true));
	}

	public Boolean estanLiberadasLasPrendas() {
		return prendas.stream().allMatch(prenda -> prenda.estaDisponible());
	}

	public int coeficientePreferencia(Usuario usuario) {
    	return prendas.stream().map(prenda -> prenda.cantidadSelecciones(usuario))
    			.mapToInt(Integer::intValue).sum();
	}

	public void ajustarCoeficientePreferencia(Usuario usuario, int value) {
		prendas.forEach(prenda -> prenda.ajustarCoeficientePreferencia(usuario, value));
	}
	
	public Integer getId() {
		return id;
	}
	
	public List<Prenda> filtrarPorCategoria(Categoria categoria, Set<Prenda> prendas) {
		return prendas.stream().filter(prenda -> prenda.getCategoria() == categoria).collect(Collectors.toList());
	}
	
	public List<Prenda> getCabeza() {
		return this.filtrarPorCategoria(Categoria.CABEZA, prendas);
	}
	

	public List<Prenda> getCuello() {
		return this.filtrarPorCategoria(Categoria.CUELLO, prendas);
	}

	public List<Prenda> getSuperior() {
		return this.filtrarPorCategoria(Categoria.SUPERIOR, prendas);
	}

	public List<Prenda> getInferior() {
		return this.filtrarPorCategoria(Categoria.INFERIOR, prendas);
	}

	public List<Prenda> getManos() {
		return this.filtrarPorCategoria(Categoria.MANOS, prendas);
	}
	
	public List<Prenda> getCalzado(){
		return this.filtrarPorCategoria(Categoria.CALZADO, prendas);
	}
}