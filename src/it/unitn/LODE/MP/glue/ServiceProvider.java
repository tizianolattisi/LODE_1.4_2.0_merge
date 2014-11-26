/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE.MP.glue;

import it.unitn.LODE.LODEParameters;
import it.unitn.LODE.MP.IF.LODEParametersIF;
import it.unitn.LODE.MP.IF.ServiceProviderIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.ProgramState;

/**
 *
 * @author ronchet
 */
public class ServiceProvider implements ServiceProviderIF {
    public String getCurrentLectureAcquisitionPath() {return ProgramState.getInstance().getCurrentLecture().getAcquisitionPath() + LODEConstants.FS;}
    public LODEParametersIF getLODEParameters() { return LODEParameters.getInstance();}
}
