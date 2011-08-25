/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "evento")
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e")})
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre_evento")
    private String nombreEvento;
    @Column(name = "tiempo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tiempo;
    @Column(name = "incluyente")
    private Integer incluyente;
    @JoinColumn(name = "sensor_sec", referencedColumnName = "id")
    @ManyToOne
    private Sensor sensorSec;
    @JoinColumn(name = "sensor", referencedColumnName = "id")
    @ManyToOne
    private Sensor sensor;
    @JoinColumn(name = "actuador", referencedColumnName = "id")
    @ManyToOne
    private Actuador actuador;

    public Evento() {
    }

    public Evento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getIncluyente() {
        return incluyente;
    }

    public void setIncluyente(Integer incluyente) {
        this.incluyente = incluyente;
    }

    public Sensor getSensorSec() {
        return sensorSec;
    }

    public void setSensorSec(Sensor sensorSec) {
        this.sensorSec = sensorSec;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Actuador getActuador() {
        return actuador;
    }

    public void setActuador(Actuador actuador) {
        this.actuador = actuador;
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
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.eh.persistence.Evento[ id=" + id + " ]";
    }
    
}
