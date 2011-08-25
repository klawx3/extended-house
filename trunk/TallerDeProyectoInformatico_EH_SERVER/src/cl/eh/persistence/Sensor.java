/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.persistence;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "sensor")
@NamedQueries({
    @NamedQuery(name = "Sensor.findAll", query = "SELECT s FROM Sensor s")})
public class Sensor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ubicacion")
    private String ubicacion;
    @Lob
    @Column(name = "puntero")
    private byte[] puntero;
    @OneToMany(mappedBy = "sensor")
    private Collection<Historial> historialCollection;
    @OneToMany(mappedBy = "sensorSec")
    private Collection<Evento> eventoCollection;
    @OneToMany(mappedBy = "sensor")
    private Collection<Evento> eventoCollection1;

    public Sensor() {
    }

    public Sensor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public byte[] getPuntero() {
        return puntero;
    }

    public void setPuntero(byte[] puntero) {
        this.puntero = puntero;
    }

    public Collection<Historial> getHistorialCollection() {
        return historialCollection;
    }

    public void setHistorialCollection(Collection<Historial> historialCollection) {
        this.historialCollection = historialCollection;
    }

    public Collection<Evento> getEventoCollection() {
        return eventoCollection;
    }

    public void setEventoCollection(Collection<Evento> eventoCollection) {
        this.eventoCollection = eventoCollection;
    }

    public Collection<Evento> getEventoCollection1() {
        return eventoCollection1;
    }

    public void setEventoCollection1(Collection<Evento> eventoCollection1) {
        this.eventoCollection1 = eventoCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sensor)) {
            return false;
        }
        Sensor other = (Sensor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.eh.persistence.Sensor[ id=" + id + " ]";
    }
    
}
