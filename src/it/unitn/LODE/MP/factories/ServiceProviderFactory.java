package it.unitn.LODE.MP.factories;

import it.unitn.LODE.MP.IF.ServiceProviderIF;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceProviderFactory {
    static ServiceProviderIF instance;
    public ServiceProviderFactory(){
        if (instance==null) try {
                instance=(ServiceProviderIF)(Class.forName("it.unitn.LODE.MP.glue.ServiceProvider").newInstance());
            } catch (InstantiationException ex) {
                Logger.getLogger(ServiceProviderFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ServiceProviderFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServiceProviderFactory.class.getName()).log(Level.SEVERE, null, ex);
            }   
    }
    public ServiceProviderIF getServiceProvider() {return instance;}
}
