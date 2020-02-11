package queMePongo.prenda;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import queMePongo.persistencia.ColorConverter;
import queMePongo.usuario.Usuario;
import javax.persistence.*;

@Entity
public class Prenda {
    @Id @GeneratedValue
    private Integer id;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private TipoPrenda tipoPrenda;
    @Enumerated(EnumType.STRING)
    private Material material;
    @Convert(converter = ColorConverter.class)
    private Color colorPrimario;
    @Convert(converter = ColorConverter.class)
    private Color colorSecundario;
    private String urlImagen;
    private boolean disponible;
    @ElementCollection
    @CollectionTable(name = "prenda_selecciones", joinColumns = {@JoinColumn(name = "prenda_id")})
    @Column(name = "seleccion")
    @MapKeyJoinColumn(name = "usuario_id")
    private Map<Usuario, Integer> historialSelecciones = new HashMap<>();

    public Prenda() {}

    public Prenda(String nombre, TipoPrenda tipoPrenda, Material material, Color colorPrimario, Color colorSecundario, String urlImagen) {
        this.nombre = nombre;
    	this.tipoPrenda = tipoPrenda;
        this.material = material;
        this.colorPrimario = colorPrimario;
        this.colorSecundario = colorSecundario;
        this.disponible = true;
        this.urlImagen = urlImagen;
    }

    public TipoPrenda getTipoPrenda() {
        return tipoPrenda;
    }
    public Color getColorPrimario() {
        return colorPrimario;
    }
    public Color getColorSecundario() {
        return colorSecundario;
    }
    public Material getMaterial() {
        return material;
    }
    public String getUrlImagen() { return urlImagen; }
    public Categoria getCategoria() {
        return this.tipoPrenda.getCategoria();
    }

    public boolean estaDisponible() {
        return this.disponible;
    }
    
    public void setDisponibilidad(boolean disponible) {
    	this.disponible = disponible;
    }
    
    public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
		return this.tipoPrenda.sePuedeUsarATemperatura(usuario, temperatura);
    }

	public boolean esSuperiorBasica() {
		return this.tipoPrenda.esPrendaSuperiorBasica();
	}

	public int cantidadSelecciones(Usuario usuario) {
		if(historialSelecciones.get(usuario) == null)
			historialSelecciones.put(usuario, 0);
		return historialSelecciones.get(usuario).intValue();
	}

	public void ajustarCoeficientePreferencia(Usuario usuario, int value) {
		int oldValue = this.cantidadSelecciones(usuario);
		historialSelecciones.replace(usuario, oldValue + value);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Integer getId() {
		return id;
	}
}
