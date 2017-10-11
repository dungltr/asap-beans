/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irisa.enssat.rennes1.thesis;

import gr.ntua.cslab.asap.operators.AbstractOperator;
import gr.ntua.cslab.asap.operators.Dataset;
import gr.ntua.cslab.asap.operators.Operator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author le
 */
public class WorkflowNode {
    private boolean visited;
    private Double optimalCost,execTime;
    public boolean isOperator,isAbstract;
    public Operator operator;
    public AbstractOperator abstractOperator;
    public Dataset dataset;
    public List<gr.ntua.cslab.asap.workflow.WorkflowNode> inputs, outputs;
    private static Logger logger = Logger.getLogger(gr.ntua.cslab.asap.workflow.WorkflowNode.class.getName());
    public boolean copyToLocal=false, copyToHDFS=false;
    public String inMonitorValues;
    
    public void setExecTime(Double execTime) {
		this.execTime=execTime;
	}
    
}
