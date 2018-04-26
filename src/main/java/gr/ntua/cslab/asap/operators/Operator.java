/*
 * Copyright 2016 ASAP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package gr.ntua.cslab.asap.operators;

import gr.ntua.cslab.asap.optimization.ML;
import gr.ntua.cslab.asap.optimization.OptimizeMissingMetrics;
import gr.ntua.cslab.asap.rest.beans.OperatorDescription;
import gr.ntua.cslab.asap.utils.DataSource;
import gr.ntua.cslab.asap.utils.MongoDB;
import gr.ntua.cslab.asap.utils.ReadFile;
import gr.ntua.cslab.asap.utils.Utils;
import gr.ntua.cslab.asap.utils.CsvFileReader;
import static gr.ntua.cslab.asap.utils.ReadMatrixCSV.readMatrix;
import static gr.ntua.cslab.asap.utils.dream.estimateSizeOfMatrix;
import gr.ntua.cslab.asap.workflow.WorkflowNode;
import gr.ntua.ece.cslab.panic.core.client.Benchmark;
import gr.ntua.ece.cslab.panic.core.containers.beans.InputSpacePoint;
import gr.ntua.ece.cslab.panic.core.containers.beans.OutputSpacePoint;
import gr.ntua.ece.cslab.panic.core.models.AbstractWekaModel;
import gr.ntua.ece.cslab.panic.core.models.LeastSquares;
import gr.ntua.ece.cslab.panic.core.models.GaussianCurves;
import gr.ntua.ece.cslab.panic.core.models.IsoRegression;
import gr.ntua.ece.cslab.panic.core.models.MLPerceptron;
import gr.ntua.ece.cslab.panic.core.models.RBF;
import gr.ntua.ece.cslab.panic.core.models.Model;
import gr.ntua.ece.cslab.panic.core.models.RandomCommittee;
import gr.ntua.ece.cslab.panic.core.models.UserFunction;
import gr.ntua.ece.cslab.panic.core.samplers.Sampler;
import gr.ntua.ece.cslab.panic.core.samplers.UniformSampler;
import gr.ntua.ece.cslab.panic.core.utils.CSVFileManager;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils.Null;


public class Operator {
	public HashMap<String, List<Model>> models;
	public HashMap<String, String> inputSpace, outputSpace;
	public SpecTree optree;
	public String opName;
	private DataSource dataSource;
	//private Model bestModel;
	private double minTotalError;
	private static Logger logger = Logger.getLogger(Operator.class.getName());
	public String directory;
	private String inputSource;
	static String IRES_HOME = ReadFile.readhome("IRES_HOME");
        static String ASAP_HOME = IRES_HOME;
        static String IRES_library = ASAP_HOME+"/asap-platform/asap-server";
        static String Library = IRES_library+"/target";

	public Operator(String name, String directory) {
		optree = new SpecTree();
		opName = name;
		models = new HashMap<String, List<Model>>();
		this.directory = directory;
	}


	/*public void readModel(File file) throws Exception {
		String modelClass = optree.getParameter("Optimization.model");
		if(modelClass==null){
			performanceModel = AbstractWekaModel.readFromFile(file.toString()+"/model");
		}
	}*/

	/**
	 * @throws Exception
	 */
	public void reConfigureModel() throws Exception {
            logger.info("reconfiguring model for: " + opName);
            System.out.println("reconfiguring model for: " + opName);
            String modelClass;
            List<OutputSpacePoint> outPoints = new ArrayList<>();
            minTotalError = Double.MAX_VALUE;
            for (Entry<String, String> e : outputSpace.entrySet()) {
		Model bestModel = null;
                if (inputSource != null && inputSource.equalsIgnoreCase("csv")) {
                    List<Model> performanceModels = new ArrayList<Model>();
                    modelClass = optree.getParameter("Optimization.model." + e.getKey());
                    if (modelClass.contains("AbstractWekaModel")) {
                        String modelDir = directory + "/models";
                        File modelFile = new File(modelDir);
                        if (modelFile.exists()) {
                           try {
			//Deleting the directory recursively using FileUtils.
                               FileUtils.deleteDirectory(modelFile);
                               System.out.println("Directory has been deleted recursively !");
                               } catch (IOException ee) {
                           	      System.out.println("Problem occurs when deleting the directory : " + modelDir);
                                      ee.printStackTrace();
                                 }
                        }
                              //modelFile.delete();                             
                        int i = 0;
                        System.out.println("CSV");
                        //this.initializeDatasource();
                        //outPoints = dataSource.getOutputSpacePoints(e.getKey());
                        CSVFileManager file = new CSVFileManager();
                        file.setFilename(directory + "/data/" + e.getKey() + ".csv");
                        //////Dung edit to put DREAM in this step
                        /*String fileName = directory + "/data/" + e.getKey() + ".csv";
                        double R_2_limit = 0.8;
                        double [][] realValue = readMatrix(fileName, 1);
                        int variables = realValue[0].length - 1;
                        int Max_line_estimate = estimateSizeOfMatrix(CsvFileReader.count(fileName)-1,variables,fileName,R_2_limit);
                        System.out.println("the maximum line for UserFunction is: " + Max_line_estimate);
                        System.out.println("la la la");
                        */
                        ////////////Dung edit is finish to put DREAM
                        
                        for (InputSpacePoint in : file.getInputSpacePoints()) {
                            OutputSpacePoint out = file.getActualValue(in);
                            outPoints.add(out);
                            System.out.println(out);// Dung edit
                        }    
                        System.out.println(outPoints);
                        for (Class<? extends Model> c : Benchmark.discoverModels()) {
                            if (c.equals(UserFunction.class))
                                continue;
                            if (c.equals(LeastSquares.class))
                                continue;
                            if (c.equals(RandomCommittee.class))
                                continue;
                            if (c.equals(GaussianCurves.class))
                                continue;
                            if (c.equals(IsoRegression.class))
                                continue;
                            if (c.equals(MLPerceptron.class))
                                continue;
                            if (c.equals(RBF.class))
                                continue;
                            Model model = (Model) c.getConstructor().newInstance();
                            if(outPoints==null || outPoints.size()<2){
                                bestModel=null;
                            }
                            else if(outPoints.size()<=7){
                                    double error =0;
                                    for (OutputSpacePoint point : outPoints){
                                        model.feed(point, false);
                                    }
                                    try {
                                        model.train();
                                        error = ML.totalError(model);
                                    }   catch (Exception e1) {
                                            logger.info("Exception in training: "+e1.getMessage());
                                            continue;
                                        }
                                    System.out.println(model.getClass()+" error: "+error);
                                    if (error < minTotalError){
                                        bestModel = model;
                                        minTotalError = error;
                                    }
                                }
                                else {
                                    double error =0;
                                    int iterations=1;
                                    if(outPoints.size()<=20)
                                        iterations=1;
                                    for (int j = 0; j < iterations; j++) {
                                        ArrayList<OutputSpacePoint> trainPoints = new ArrayList<OutputSpacePoint>();
                                        ArrayList<OutputSpacePoint> testPoints = new ArrayList<OutputSpacePoint>();
                                        Random r = new Random();
                                        for (OutputSpacePoint point : outPoints){
                                            if(r.nextDouble()<=0.75 && trainPoints.size()<0.75*outPoints.size())
                                                trainPoints.add(point);
                                            else
                                                testPoints.add(point);
                                        }
                                        for (OutputSpacePoint point : trainPoints){
                                            model.feed(point, false);
                                        }
                                        try {
                                            model.train();
                                            double terror = ML.totalSquaredError(model, testPoints);
                                            error+=terror;
                                        } catch (Exception e1) {
                                            logger.info("Exception in training: "+e1.getMessage());
                                            continue;
                                        }
                                    }
                                    System.out.println(model.getClass()+" error: "+error);
                                    if (error < minTotalError){
                                        for (OutputSpacePoint point : outPoints){
                                            model.feed(point, false);
                                        }
                                        try {
                                            model.train();
                                        } catch (Exception e1) {
                                            logger.info("Exception in training: "+e1.getMessage());
                                            continue;
                                        }
                                        bestModel = model;
                                        minTotalError = error;
                                    }
                                  //model.serialize(modelDir + "/" + e.getKey() + "_" + i + ".model");
                                  //performanceModels.add(model);

                                }
                                i++;
                        }
                        if(bestModel!=null){
                            modelFile.mkdir();
                            bestModel.serialize(modelDir + "/" + e.getKey() + "_" + i + ".model");
                            bestModel.setInputSpace(inputSpace);
                            HashMap<String, String> temp = new HashMap<String, String>();
                            temp.put(e.getKey(), e.getValue());
                            bestModel.setOutputSpace(temp);
                            HashMap<String, String> conf = new HashMap<String, String>();
                            optree.getNode("Optimization").toKeyValues("", conf);
                            bestModel.configureClassifier(conf);
                            performanceModels.add(bestModel);
                            models.remove(e.getKey());
                            models.put(e.getKey(), performanceModels);
                        }
                    }
                }
            }
        }	/**
	 * @throws Exception
	 */
	public void configureModel() throws Exception {
		System.out.println("Begining Configuring model: ");
		String modelClass;
		List<Model> performanceModels;
        List<OutputSpacePoint> outPoints = new ArrayList<>();
		inputSpace = new HashMap<String, String>();
		outputSpace = new HashMap<String, String>();
		/* vpapa: Optimization.inputSpace and Optimization.outputSpace are mandatory
			fields of an operator description file
		*/
		try{
			if(optree.getNode("Optimization.inputSpace")!=null)
				optree.getNode("Optimization.inputSpace").toKeyValues("", inputSpace);
			if(optree.getNode("Optimization.outputSpace")!=null)
				optree.getNode("Optimization.outputSpace").toKeyValues("", outputSpace);
		}
		catch( NullPointerException npe ){
			System.out.println( "ERROR: From operator " + opName + "'s description file either"
								+ " Optimization.inputSpace or Optimization.outputSpace"
								+ " parameter or both are missing. Add them appropriately.");
			logger.info( "ERROR: From operator " + opName + "'s description file either"
								+ " Optimization.inputSpace or Optimization.outputSpace"
								+ " parameter or both are missing. Add them appropriately.");
			npe.printStackTrace();
		}

		inputSource = optree.getParameter("Optimization.inputSource.type");
		minTotalError = Double.MAX_VALUE;

		logger.info("Metrics for operator " + opName);
		for (String metric : outputSpace.keySet()) {
			logger.info("--"+metric);
		}

		for (Entry<String, String> e : outputSpace.entrySet()) {
			Model bestModel = null;
			logger.info("Configuring model for metric: "+e.getKey());

			performanceModels = new ArrayList<>();
			modelClass = optree.getParameter("Optimization.model." + e.getKey());

			if (modelClass.contains("AbstractWekaModel")) {
				String modelDirPath = Library+"/"+directory + "/models";
				File modelDir = new File(modelDirPath);
				/* Current metric's name */
				final String metricName = e.getKey();

				/* Keeps the models for the specific metric (eg. execTime) */
				File[] metricModels = modelDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File file, String name) {
						return name.startsWith(metricName);
					}
				});

				if (metricModels != null)
					logger.info(metricModels.length + " models found for metric: " + e.getKey());
				else
					logger.info("No models found for metric: "+e.getKey());

				if (modelDir.exists() && metricModels != null &&metricModels.length > 0) {
					for (File file : metricModels) {
						if (file.getName().endsWith(".model")) {
							try{
								String path = file.getAbsolutePath();
								logger.info("Adding model "+path + " for metric "
										+ metricName + " for operator " + opName);
								performanceModels.add(AbstractWekaModel.readFromFile(file.getAbsolutePath()));
							}
							catch( EOFException eofe){
								logger.info( "ERROR: There is a problem with the already existing models of");
								logger.info( opName + " operator. Verify that the existent models are not empty in folder");
								logger.info( "$ASAP_SERVER_HOME/asapLibrary/operators/" + opName + "/models.");
								logger.info( "A solution to this problem would be to delete the problematic models");
								logger.info( "or even delete the whole models folder and let ASAP server to build it");
								logger.info( "from scratch during its restarting.");
								eofe.printStackTrace();
							}
							catch( Exception exce){
								logger.info( "ERROR: There is a problem with the already existing models of");
								logger.info( opName + " operator. Verify that the existent models are correct.");
								logger.info( "A solution to this problem would be to delete the problematic models");
								logger.info( "or even delete the whole models folder and let ASAP server to build it");
								logger.info( "from scratch during its restarting.");
								exce.printStackTrace();
							}
						}
					}
				}
					else { //Dung edit to build model every loop
					System.out.println("Bulding model for metric: " + metricName + " for operator " + opName);
					logger.info("Bulding model for metric: " + metricName + " for operator " + opName);
					int i = 0;
                    if (inputSource != null && inputSource.equalsIgnoreCase("mongodb")) {
                    	logger.info("The datasource for metric " + metricName +
								" of operator " + opName + " is MongoDB");

                        this.initializeDatasource();
                        outPoints = dataSource.getOutputSpacePoints(e.getKey());
                    }
                    else {
						logger.info("The datasource for metric " + metricName +
								" of operator " + opName + " is CSV");

                        CSVFileManager file = new CSVFileManager();
                        file.setFilename(directory + "/data/" + e.getKey() + ".csv");
                        for (InputSpacePoint in : file.getInputSpacePoints()) {
                        	OutputSpacePoint out = file.getActualValue(in);
                            outPoints.add(out);
                        }
                    }
                    System.out.println("The datasource for metric " + metricName +
								" of operator " + opName + " is CSV");
                        System.out.println(outPoints);
                    for (Class<? extends Model> c : Benchmark.discoverModels()) {
						if (c.equals(UserFunction.class))
							continue;
						Model model = (Model) c.getConstructor().newInstance();
						if(outPoints==null || outPoints.size()<2){
							bestModel=null;
						}
						else if(outPoints.size()<=7){// Dung edit old is 7
							double error =0;
	                        for (OutputSpacePoint point : outPoints){
	                            model.feed(point, false);
	                        }
							try {
								model.train();
								error = ML.totalError(model);
							} catch (Exception e1) {
								logger.info("Exception in training: "+e1.getMessage());
								continue;
							}
							System.out.println(model.getClass()+" error: "+error);
							if (error < minTotalError){
								bestModel = model;
								minTotalError = error;
							}
						}
						else{
							double error =0;
							for (int j = 0; j < 10; j++) { //Dung edit, old is j <10
								ArrayList<OutputSpacePoint> trainPoints = new ArrayList<OutputSpacePoint>();
								ArrayList<OutputSpacePoint> testPoints = new ArrayList<OutputSpacePoint>();
								Random r = new Random();
								for (OutputSpacePoint point : outPoints){
									if(r.nextDouble()<=0.8)
										trainPoints.add(point);
									else
										testPoints.add(point);
		                        }
		                        for (OutputSpacePoint point : trainPoints){
		                            System.out.println(point);//Dung edit
						model.feed(point, false);
		                        }
								try {
									model.train();
									double terror = ML.totalRelError(model, testPoints);
									error+=terror;
								} catch (Exception e1) {
									logger.info("Exception in training: "+e1.getMessage());
									continue;
								}
							}
							System.out.println(model.getClass()+" error: "+error);
							if (error < minTotalError){
								for (OutputSpacePoint point : outPoints){
		                            model.feed(point, false);
		                        }
								try {
									model.train();
								} catch (Exception e1) {
									logger.info("Exception in training: "+e1.getMessage());
									continue;
								}
								bestModel = model;
								minTotalError = error;
							}
							//model.serialize(modelDir + "/" + e.getKey() + "_" + i + ".model");
							//performanceModels.add(model);
						}
						i++;
					}
                    if(bestModel!=null){
						modelDir.mkdir();
						String modelPath = modelDir + "/" + e.getKey() + "_" + i + ".model";
						bestModel.serialize(modelPath);
						performanceModels.add(bestModel);
                    }
				}//Dung edit to build model every loop
			} else {
				performanceModels.add((Model) Class.forName(modelClass).getConstructor().newInstance());
			}

			for (Model performanceModel : performanceModels) {
				performanceModel.setInputSpace(inputSpace);
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put(e.getKey(), e.getValue());
				performanceModel.setOutputSpace(temp);

				HashMap<String, String> conf = new HashMap<String, String>();
				optree.getNode("Optimization").toKeyValues("", conf);
				//System.out.println("sadfas: "+conf);
				performanceModel.configureClassifier(conf);
			}
			models.put(e.getKey(), performanceModels);
		}

		/* DELETE THIS
		System.out.println("Models for operator "+this.opName);
		for (Entry<String, List<Model>> entry : models.entrySet()) {
			System.out.println(entry.getKey());
			for (Model m : entry.getValue()) {
				System.out.println(m);
			}
		}*/
	}

    public void initializeDatasource(){
    	logger.info("Initializing datasource...");
		String collection = this.opName;//optree.getParameter("Optimization.inputSource.collection");
        String host = optree.getParameter("Optimization.inputSource.host");
        String db = optree.getParameter("Optimization.inputSource.db");
        logger.info("Col: "+collection+"\nDB: "+db+"\nHost: "+host+"\n");
        List<String> is = new ArrayList<String>();
        List<String> os = new ArrayList<String>();

		if (collection == null || host == null || db == null) {
			logger.info("NULL");
			return;
		}

        for (String k : inputSpace.keySet()) {
            is.add(k);
        }
        for (String k : outputSpace.keySet()) {
            os.add(k);
        }
        this.dataSource = new MongoDB(host, db, collection, is, os);
    }

	public void writeCSVfileUniformSampleOfModel(String variable, Double samplingRate, String filename, String delimiter, boolean addPredicted) throws Exception {

		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);

		BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(fos));
		getUniformSampleOfModel(variable, samplingRate, writter, delimiter, addPredicted);

		writter.close();

	}

	public void writeCSVfileUniformSampleOfModel(String variable, Double samplingRate, String filename, String delimiter) throws Exception {

		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);

		BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(fos));
		getUniformSampleOfModel(variable, samplingRate, writter, delimiter, false);

		writter.close();
	}
	protected void getUniformSampleOfModel(String variable, Double samplingRate, BufferedWriter writter, String delimiter, boolean addPredicted) throws Exception {

		List<Model> lm = models.get(variable);
		if(lm==null || lm.size()==0){

			HashMap<String, List<Double>> dim = new HashMap<String, List<Double>>();
			for (Entry<String, String> e : inputSpace.entrySet()) {
				writter.append(e.getKey() + delimiter);
				String[] limits = e.getValue().split(delimiter);
				List<Double> l = new ArrayList<Double>();
				Double min = Double.parseDouble(limits[1]);
				Double max = Double.parseDouble(limits[2]);
				if (limits[3].startsWith("l")) {
					Double step = 10.0;
					for (double i = min; i <= max; i *= step) {
						l.add(i);
						l.add(2*i);
						l.add(4*i);
						l.add(6*i);
						l.add(8*i);
					}
				} else {
					Double step = Double.parseDouble(limits[3]);
					for (double i = min; i <= max; i += step) {
						l.add(i);
					}
				}
				dim.put(e.getKey(), l);
			}
			int i = 0;
			writter.append(variable);
			if (addPredicted) {
				writter.append(delimiter + "model");
			}
			writter.newLine();
			
			Sampler s = (Sampler) new UniformSampler();
			s.setSamplingRate(samplingRate);
			s.setDimensionsWithRanges(dim);
			s.configureSampler();
			while (s.hasMore()) {
				InputSpacePoint nextSample = s.next();
				OutputSpacePoint op = new OutputSpacePoint();
				HashMap<String, Double> values = new HashMap<String, Double>();
				//System.out.println(nextSample);
				Double res = getMettric(variable, nextSample);
				values.put(variable, res);
				op.setInputSpacePoint(nextSample);
				op.setValues(values);
				//System.out.println(res);
				writter.append(op.toCSVString(delimiter));
				if (addPredicted) {
					writter.append(delimiter + "NoModel");
				}
				writter.newLine();
			}
		}
		else{
			Model m1 = lm.get(0);
			HashMap<String, List<Double>> dim = new HashMap<String, List<Double>>();
			for (Entry<String, String> e : m1.getInputSpace().entrySet()) {
				writter.append(e.getKey() + delimiter);
				String[] limits = e.getValue().split(delimiter);
				List<Double> l = new ArrayList<Double>();
				Double min = Double.parseDouble(limits[1]);
				Double max = Double.parseDouble(limits[2]);
				if (limits[3].startsWith("l")) {
					Double step = 10.0;
					for (double i = min; i <= max; i *= step) {
						l.add(i);
						l.add(2*i);
						l.add(4*i);
						l.add(6*i);
						l.add(8*i);
					}
				} else {
					Double step = Double.parseDouble(limits[3]);
					for (double i = min; i <= max; i += step) {
						l.add(i);
					}
				}
				dim.put(e.getKey(), l);
			}
			int i = 0;
			for (String k : m1.getOutputSpace().keySet()) {
				writter.append(k);
				i++;
				if (i < m1.getOutputSpace().size()) {
					writter.append(delimiter);
				}
			}
			if (addPredicted) {
				writter.append(delimiter + "model");
			}
			writter.newLine();
	
			//for (Model m : lm) {
				Model m = lm.get(0);
				//System.out.println(dim);
				Sampler s = (Sampler) new UniformSampler();
				s.setSamplingRate(samplingRate);
				s.setDimensionsWithRanges(dim);
				s.configureSampler();
				while (s.hasMore()) {
					InputSpacePoint nextSample = s.next();
					OutputSpacePoint op = new OutputSpacePoint();
					HashMap<String, Double> values = new HashMap<String, Double>();
					for (String k : m.getOutputSpace().keySet()) {
						values.put(k, null);
					}
					op.setValues(values);
					//System.out.println(nextSample);
					OutputSpacePoint res = m.getPoint(nextSample, op);
					//System.out.println(res);
					writter.append(res.toCSVString(delimiter));
					if (addPredicted) {
						writter.append(delimiter + m.getClass().getSimpleName());
					}
					writter.newLine();
				}
			//}
		}
	}

	public void add(String key, String value) {
		//Logger.getLogger(Operator.class.getName()).info("Adding key: "+key+" value: "+value);
		optree.add(key, value);
	}

	@Override
	public String toString() {
		String ret = opName + ": ";
		ret += optree.toString();
		return ret;
	}

	public String toKeyValues(String separator) {
		String ret = "";
		ret += optree.toKeyValues("", ret, separator);
		return ret;
	}


	public OperatorDescription toOperatorDescription() {
		OperatorDescription ret = new OperatorDescription(opName, "");
		optree.toOperatorDescription(ret);
		return ret;
	}

	public void readFromDir() throws Exception {
		//System.out.println("operator: "+opName);
		File f = new File(directory + "/description");
		//logger.info( "Does directory " + f + " exists? " + f.exists());
		if( !f.exists()){
			logger.info( "For abstract operator " + opName + " there is not any materialized operator.");
			logger.info( "Try to create one by creating the corresponding directory with all the required");
			logger.info( "files and subfolders into the appropriate asapLibrary directory.");
		}
		else{
			InputStream stream = new FileInputStream(f);
			Properties props = new Properties();
			props.load(stream);
			for (Entry<Object, Object> e : props.entrySet()) {
				add((String) e.getKey(), (String) e.getValue());
			}
			stream.close();
			configureModel();			
		}
		//this.performanceModel = AbstractWekaModel.readFromFile(directory+"/model");
	}

	public void readPropertiesFromString(String properties) throws IOException {
		InputStream stream = new ByteArrayInputStream(properties.getBytes());
		readPropertiesFromStream(stream);
		stream.close();
		logger.info( optree.toString());
		inputSpace = new HashMap<String, String>();
		outputSpace = new HashMap<String, String>();
		try{
			if(optree.getNode("Optimization.inputSpace")!=null)
				optree.getNode("Optimization.inputSpace").toKeyValues("", inputSpace);
			if(optree.getNode("Optimization.outputSpace")!=null)
				optree.getNode("Optimization.outputSpace").toKeyValues("", outputSpace);
		}
		catch( NullPointerException npe){
			System.out.println( "ERROR: From operator " + opName + "'s description file either"
								+ " Optimization.inputSpace or Optimization.outputSpace"
								+ " parameter or both are missing. Add them appropriately.");
			logger.info( "ERROR: From operator " + opName + "'s description file either"
								+ " Optimization.inputSpace or Optimization.outputSpace"
								+ " parameter or both are missing. Add them appropriately.");
			npe.printStackTrace();
		}
	}

	public void readPropertiesFromStream(InputStream stream) throws IOException {
		Properties props = new Properties();
		props.load(stream);
		for (Entry<Object, Object> e : props.entrySet()) {
			add((String) e.getKey(), (String) e.getValue());
		}
	}

	private void copyExecPath(Dataset d, String path) {
		String newPath = null;
		if (path != null) {
			//check which type of path is provided
			if (path.startsWith("$HDFS_OP_DIR")) {
				//the output file will be created inside operator's "local" folder at HDFS
				newPath = path.replace("$HDFS_OP_DIR", "$HDFS_DIR/" + opName);
			}
			else {
				if( path.startsWith( "$AS_IS")){
					//the output path will be created at the following path
					//hdfs://NameNode_Host:9000/specified_path
					newPath = path.replace( "$AS_IS", "");
				}
				else{
					//the output file will be created under operator's "local" folder at HDFS
					//but at the path specified
					newPath = opName + "/" + path;
				}
			}
			d.add("Execution.path", newPath);
		}
	}

	public void copyExecVariables(Dataset d, int position, List<WorkflowNode> inputs) {
		logger.info( "Copying execution parameters for" );
        logger.info( "dataset: " + d.datasetName);
        logger.info( "at position: " + position);
        logger.info( "and inputs: " + inputs);
        SpecTreeNode variables = optree.getNode("Execution.Output" + position);
		HashMap<String, String> val = new HashMap<String, String>();
		variables.toKeyValues("", val);
        logger.info( "Execution variables are: " + variables);
        try{
			for (Entry<String, String> e : val.entrySet()) {
				if (e.getKey().equals("path")) {
					copyExecPath(d, e.getValue());
				} else {
					String[] s = e.getValue().split("\\.");
					if (s[0].startsWith("In")) {
						int index = Integer.parseInt(s[0].substring((s[0].length() - 1)));
						//System.out.println("data index "+ index);
						WorkflowNode n = inputs.get(index);
						String v = "";
						if (n.isOperator)
							v = n.inputs.get(0).dataset.getParameter("Execution." + s[1]);
						else
							v = n.dataset.getParameter("Execution." + s[1]);
						if (v == null) {
							v = "_";
						}
						d.add("Execution." + e.getKey(), v);
					} else {
						d.add("Execution." + e.getKey(), e.getValue());
					}
				}
			}
        }
        catch( NullPointerException npe){
        	logger.info( "ERROR: There is a problem with Execution.Output properties for some");
        	logger.info( "description file( s).");
        }
	}

	public void outputFor(Dataset d, int position,
						  HashMap<String, Double> nextMetrics, List<WorkflowNode> inputs) {
		// TODO Auto-generated method stub
		d.datasetTree = optree.copyInputSubTree("Constraints.Output" + position);
		if (d.datasetTree == null)
			d.datasetTree = new SpecTree();

		try{
			copyExecVariables(d, position, inputs);
		}
		catch( NullPointerException npe){
        	logger.info( "ERROR: There is also a problem with Constraints.Output properties");
        	logger.info( " for the same description file( s).");
		}
		generateOptimizationMetrics(d, position, nextMetrics);
	}


	public void generateOptimizationMetrics(Dataset d, int position,
											HashMap<String, Double> nextMetrics) {
		for (String out : outputSpace.keySet()) {
			if (out.startsWith("Out" + position)) {
				String[] s = out.split("\\.");
				d.add("Optimization." + s[1], nextMetrics.get(out) + "");
			}
		}
	}


	public void outputFor(Dataset d, int position, List<WorkflowNode> inputs) throws Exception {
		//System.out.println("Generating output for pos: "+ position);
		d.datasetTree = optree.copyInputSubTree("Constraints.Output" + position);
		if (d.datasetTree == null)
			d.datasetTree = new SpecTree();

		copyExecVariables(d, position, inputs);
		generateOptimizationMetrics(d, position, inputs);
		/*int min = Integer.MAX_VALUE;
		for(WorkflowNode n :inputs){
			int temp = Integer.MAX_VALUE;
			if(!n.inputs.get(0).isOperator)
				temp= Integer.parseInt(n.inputs.get(0).dataset.getParameter("Optimization.uniqueKeys"));
			else
				 temp = Integer.parseInt(n.inputs.get(0).inputs.get(0).dataset.getParameter("Optimization.uniqueKeys"));
			if(temp<min){
				min=temp;
			}
		}
		d.datasetTree.add("Optimization.uniqueKeys", min+"");*/
	}

	public void generateOptimizationMetrics(Dataset d, int position, List<WorkflowNode> inputs) throws Exception {
		for (String out : outputSpace.keySet()) {
			if (out.startsWith("Out" + position)) {
				String[] s = out.split("\\.");
				d.add("Optimization." + s[1], getMettric(out, inputs) + "");
			}
		}

	}


	public void writeToPropertiesFile(String directory) throws Exception {
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		Properties props = new Properties();
		optree.writeToPropertiesFile("", props);
		File f = new File(directory + "/description");
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		OutputStream out = new FileOutputStream(f);
		props.store(out, "");
		out.close();
		writeModels(directory);
	}

	public void writeDescriptionToPropertiesFile(String directory) throws Exception {
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		Properties props = new Properties();
		optree.writeToPropertiesFile("", props);
		File f = new File(directory + "/description");
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		OutputStream out = new FileOutputStream(f);
		props.store(out, "");
		out.close();
	}

	public void writeModels(String directory) throws Exception {
		File mdir = new File(directory + "/models");
		if (mdir.exists()) {
			mdir.delete();
		}
		mdir.mkdir();
		for (Entry<String, List<Model>> e : models.entrySet()) {
			int i = 0;
			for (Model m : e.getValue()) {
				if (m.getClass().equals(UserFunction.class))
					continue;
				m.serialize(directory + "/models/" + e.getKey() + "_" + i + ".model");
				i++;
			}
		}
	}

	public String getInputSource(){
		return this.inputSource;
	}

	/*private Model selectModel(){
		Model model = models.va.get(0);
		if(!model.getClass().equals(gr.ntua.ece.cslab.panic.core.models.UserFunction.class)){
			for(Model m:models.get(metric)){

				if(inputSpace.size()>=2 && m.getClass().equals(gr.ntua.ece.cslab.panic.core.models.MLPerceptron.class)){
					model =m;
					break;
				}
				if(inputSpace.size()<2 && m.getClass().equals(gr.ntua.ece.cslab.panic.core.models.LinearRegression.class)){
					model =m;
					break;
				}
			}
		}
		logger.info("Model selected: "+ model.getClass());
		return model
	}*/
	public HashMap<String, Double> getOptimalPolicyCost(HashMap<String, Double> inputMetrics, List<WorkflowNode> inputs, String policy) throws Exception {
		logger.info("Input metrics: " + inputMetrics);
		HashMap<String, Double> retMetrics = new HashMap<String, Double>();
		//generate Input space point
		InputSpacePoint in = new InputSpacePoint();
		HashMap<String, Double> values = new HashMap<String, Double>();
		boolean missing =false;
		for (String inVar : inputSpace.keySet()) {
			//System.out.println("InVar: "+inVar);
			String[] s = inVar.split("\\.");

			/*
			System.out.println("inVar");
			for (String str : s){
				System.out.println(str);
			}
			System.out.println("Policy: "+policy);
			*/
			
			if (s[0].startsWith("In")) {
				int index = Integer.parseInt(s[0].substring((s[0].length() - 1)));
				String val = null;
				logger.info( "Operator inputs are: " + inputs);
				WorkflowNode n = inputs.get(index);
				//System.out.println("Index: "+index +" "+s[1]);

				Double v = null;
				if (!n.isOperator) {
					val = n.dataset.getParameter("Optimization." + s[1]);
					if(val==null){
						missing=true;
						//System.out.println("Null: "+s[0]);
						v=null;
					}
					else{
						v = Double.parseDouble(val);
						//System.out.println(v);
					}
				}
				values.put(inVar, v);
			} else {
				//System.out.println(optree.toString());
				String val = optree.getParameter("Optimization." +inVar );
				if(val==null){
//					System.out.println("Null: "+s[0]);
//					System.out.println("in value "+ 2.0);
					//values.put(inVar, 2.0);
					missing=true;
					values.put(inVar, null);
				}
				else{
					Double vv = Double.parseDouble(val);
					values.put(inVar, vv);
					System.out.println(inVar+" "+vv);
				}

			}
		}
		
		in.setValues(values);
		if(missing){
			OutputSpacePoint out = OptimizeMissingMetrics.findOptimalPointCheckAllSamples(models, in, policy, optree, this);
			retMetrics.putAll(out.getOutputPoints());
		}
		else{
			for(String metric : outputSpace.keySet()){
				Double v = getMettric(metric,inputs);
				retMetrics.put(metric, v);
			}
		}
		System.out.println("Output metrics: " + retMetrics);
		logger.info("Output metrics: " + retMetrics);
		
		
		for (Entry<String, Double> e : inputMetrics.entrySet()) {
			logger.info(e.getKey() +" "+ e.getValue());
			Double v = retMetrics.get(e.getKey());
			if(v==null)
				v=new Double(0);
			retMetrics.put(e.getKey(), e.getValue() + v);
		}
		logger.info("Output metrics added with input: " + retMetrics);
		return retMetrics;
	}


	public Double getMettric(String metric, InputSpacePoint in) throws Exception {
		logger.info("Getting mettric: " + metric + " from operator: " + opName);

		if(models.get(metric)==null || models.get(metric).size()==0){
			return new Double(0.0);
//			Random r = new Random();
//			return r.nextDouble() * 100;
		}
		Model model = models.get(metric).get(0);
		if (!model.getClass().equals(gr.ntua.ece.cslab.panic.core.models.UserFunction.class)) {
			for (Model m : models.get(metric)) {

				if (inputSpace.size() >= 2 && m.getClass().equals(gr.ntua.ece.cslab.panic.core.models.MLPerceptron.class)) {
					model = m;
					break;
				}
				if (inputSpace.size() < 2 && m.getClass().equals(gr.ntua.ece.cslab.panic.core.models.LinearRegression.class)) {
					model = m;
					break;
				}
			}
		}
		logger.info("Model selected: " + model.getClass());

		OutputSpacePoint op = new OutputSpacePoint();
		HashMap<String, Double> values = new HashMap<String, Double>();
		for (String k : model.getOutputSpace().keySet()) {
			values.put(k, null);
		}
		op.setValues(values);
		//System.out.println(in);
		OutputSpacePoint res = model.getPoint(in, op);
		//System.out.println(res);
		//System.out.println("return: " + res.getOutputPoints().get(metric));
		return res.getOutputPoints().get(metric);
	}
	
	public Double getMettric(String metric, List<WorkflowNode> inputs) throws Exception {
		System.out.println("Getting mettric: " + metric + " from operator: " + opName);
		logger.info("Getting mettric: " + metric + " from operator: " + opName);
		//System.out.println(metric);
		if(models.get(metric)==null || models.get(metric).size()==0){
			return new Double(0.0);
//			Random r = new Random();
//			return r.nextDouble() * 100;
		}
		Model model = models.get(metric).get(0);
		if (!model.getClass().equals(gr.ntua.ece.cslab.panic.core.models.UserFunction.class)) {
			for (Model m : models.get(metric)) {

				if (inputSpace.size() >= 2 && m.getClass().equals(gr.ntua.ece.cslab.panic.core.models.MLPerceptron.class)) {
					model = m;
					break;
				}
				if (inputSpace.size() < 2 && m.getClass().equals(gr.ntua.ece.cslab.panic.core.models.LinearRegression.class)) {
					model = m;
					break;
				}
			}
		}
		logger.info("Model selected: " + model.getClass());
		System.out.println("Model selected: " + model.getClass());
                //////Dung edit to put DREAM in this step
                if(model.getClass().equals(gr.ntua.ece.cslab.panic.core.models.LinearRegression.UserFunction)){
                    String fileName = directory + "/data/" + metric + ".csv";
                    double R_2_limit = 0.8;
                    double [][] realValue = readMatrix(fileName, 1);
                    int variables = realValue[0].length - 1;
                    int Max_line_estimate = estimateSizeOfMatrix(CsvFileReader.count(fileName)-1,variables,fileName,R_2_limit);
                    System.out.println("the maximum line for UserFunction is: " + Max_line_estimate);
                    System.out.println("la la la");
                }
                ////////////Dung edit is finish to put DREAM
		//System.out.println(opName);
		//System.out.println("inputs: "+inputs);

		InputSpacePoint in = new InputSpacePoint();
		HashMap<String, Double> values = new HashMap<String, Double>();
		for (String inVar : model.getInputSpace().keySet()) {
			//System.out.println("var: "+inVar);
			String[] s = inVar.split("\\.");
			if (s[0].startsWith("In")) {
				int index = Integer.parseInt(s[0].substring((s[0].length() - 1)));
				//System.out.println("data index "+ index);
				String val = null;
				WorkflowNode n = inputs.get(index);
				if (n.isOperator)
					val = n.inputs.get(0).dataset.getParameter("Optimization." + s[1]);
				else
					val = n.dataset.getParameter("Optimization." + s[1]);
				if (val == null) {
					val = "10.0";
				}
				Double maxVal = Double.parseDouble(inputSpace.get(inVar).split(",")[2]);

				Double v = Double.parseDouble(val);
				if (v > maxVal && !model.getClass().equals(gr.ntua.ece.cslab.panic.core.models.UserFunction.class)) {
					//System.out.println("found: "+v+" : "+maxVal);
					Random r = new Random();
					return 750 + r.nextDouble() * 500;
				}
				//System.out.println("in value "+ v);
				values.put(inVar, v);
			} else {
				//System.out.println("in value "+ 2.0);
				values.put(inVar, 2.0);

			}
		}
		in.setValues(values);

		OutputSpacePoint op = new OutputSpacePoint();
		values = new HashMap<String, Double>();
		for (String k : model.getOutputSpace().keySet()) {
			values.put(k, null);
		}
		op.setValues(values);
		//System.out.println(in);
		OutputSpacePoint res = model.getPoint(in, op);
		//System.out.println(res);
		//System.out.println("return: " + res.getOutputPoints().get(metric));
		return res.getOutputPoints().get(metric);

	}

	public Double getCost(List<WorkflowNode> inputs) throws NumberFormatException, EvaluationException {
		System.out.println("Getting cost for op: "+opName);
		logger.info("Compute cost Operator " + opName);
		logger.info("inputs: " + inputs);
		String value = getParameter("Optimization.execTime");
		logger.info("value " + value);
		Evaluator evaluator = new Evaluator();
		if (value.contains("$")) {
			int offset = 1;
			if (value.startsWith("\\$"))
				offset = 0;
			String[] variables = value.split("\\$");
			List<String> vars = new ArrayList<String>();
			for (int i = 0; i < variables.length; i += 1) {
				logger.info("split " + variables[i]);
			}
			for (int i = offset; i < variables.length; i += 2) {
				vars.add(variables[i]);
			}
			logger.info("Variables: " + vars);

			for (String var : vars) {
				String[] s = var.split("\\.");
				for (int i = 0; i < s.length; i += 1) {
					logger.info("split " + s[i]);
				}

				int inNum = Integer.parseInt(s[0]);
				WorkflowNode n = inputs.get(inNum);
				String val = null;
				if (n.isOperator)
					val = n.inputs.get(0).dataset.getParameter("Optimization." + s[1]);
				else
					val = n.dataset.getParameter("Optimization." + s[1]);
				if (val == null) {
					val = "10.0";
				}
				logger.info("Replace: " + "$" + var + "$  " + val);
				value = value.replace("$" + var + "$", val);
			}
			logger.info("Evaluate value " + value);

			logger.info("Cost: " + evaluator.evaluate(value));
			return Double.parseDouble(evaluator.evaluate(value));
		} else {
			logger.info("Cost: " + evaluator.evaluate(value));
			return Double.parseDouble(evaluator.evaluate(value));
		}
	}

	/*public Double getCost() {
		String value = getParameter("Optimization.execTime");
		return Double.parseDouble(value);
	}*/

	public String getParameter(String key) {
		return optree.getParameter(key);
	}


	public void deleteDiskData() {
		File file = new File(directory);
		Utils.deleteDirectory(file);
	}

	@Override
	protected Operator clone() throws CloneNotSupportedException {
		Operator ret = new Operator(opName, directory);
		ret.optree = optree.clone();
		return ret;
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}


	public String getEngine() {
		return optree.getParameter("Constraints.Engine");
	}


}
