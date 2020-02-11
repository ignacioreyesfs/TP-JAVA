package queMePongo.prenda;

import queMePongo.exceptions.AccionIncorrectaException;
import queMePongo.usuario.Usuario;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Sugerencia {
    @Id @GeneratedValue
    private Integer id;
	@Enumerated(EnumType.STRING)
    private Estado estado;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Atuendo atuendo;
    @ElementCollection(targetClass = Accion.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="Sugerencia_Accion")
    @Column(name="accion")
    @OrderColumn
    private List<Accion> accionHistory = new ArrayList<>();
    private Boolean calificado = false;
    
    public Sugerencia() {}

    public Sugerencia(Atuendo atuendo) {
        this.atuendo = atuendo;
    }

    public Atuendo getAtuendo() {
        return atuendo;
    }
    public Estado getEstado() { return estado; }

    public Set<Prenda> getPrendasAtuendo() { return atuendo.getPrendas(); }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void ocuparPrendas() { atuendo.ocuparPrendas(); }

    public void liberarPrendas() { atuendo.liberarPrendas(); }

    public boolean atuendoDisponible() { return atuendo.estaDisponible(); }

    public boolean estaAceptada() { return estado == Estado.ACEPTADO; }

    public void ejecutarAccion(Accion accion, Usuario usuario) {
        accion.ejecutar(this, usuario);
        accionHistory.add(accion);
    }

    public void deshacerAccion(Usuario usuario) {
        if (!accionHistory.isEmpty()) {
        	int indiceUltimoElemento = accionHistory.size() - 1;
            Accion ultimaAccion = accionHistory.get(indiceUltimoElemento);        
            ultimaAccion.deshacer(this, usuario);
            accionHistory.remove(indiceUltimoElemento);
        }
        else {
            throw new AccionIncorrectaException();
        }
    }
    
    public int coeficientePreferencia(Usuario usuario) { 
    	return this.atuendo.coeficientePreferencia(usuario); 
    }

	public void ajustarCoeficientePreferencia(Usuario usuario, int value) {
		this.atuendo.ajustarCoeficientePreferencia(usuario, value);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void calificar() {
		calificado = true;
	}

	public Boolean getCalificado() {
		return calificado;
	}
}