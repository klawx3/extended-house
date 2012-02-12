/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.scripts;

import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 *
 * @author Administrador
 */
public class JavaScriptModule {
    
    private ScriptEngine se;
    public final String ENGINE_NAME = "JavaScript";
    
    public JavaScriptModule() throws Exception{
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = mgr.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            List<String> engNames = factory.getNames();
            for (String name : engNames) {
                if(name.equals(ENGINE_NAME)){
                    se = factory.getScriptEngine();
                }
            }
            if(se == null){
                throw new Exception(ENGINE_NAME+" NOT FOUNDED");
            }
        }
    }
    public ScriptEngine getSE() {
        return se;
    }
    
    
}
