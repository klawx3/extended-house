/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.eh.db.model;

/**
 *
 * @author Usuario
 */
public class Usuario {

    private String usuario;
    private String contraseña;
    private int id_rol;

    public Usuario(String usuario, String contraseña, int id_rol) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.id_rol = id_rol;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
