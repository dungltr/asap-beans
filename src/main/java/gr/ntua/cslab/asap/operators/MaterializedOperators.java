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

import gr.ntua.cslab.asap.utils.ReadMatrixCSV;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaterializedOperators {
	private List<Operator> operators;
	
	
	
	public MaterializedOperators() {
		operators = new ArrayList<Operator>();
                String OperatorFolder = "/home/ubuntu/IReS-Platform/asap-platform/asap-server/target/asapLibrary/operators";
/*
		String nameOp = "Move_TPCH_Hive_Postgres";
                Operator move_tpch_hive_postgres = new Operator(nameOp,"/tmp");
                move_tpch_hive_postgres.add("Constraints.Input.number","1");
		move_tpch_hive_postgres.add("Constraints.Output.number","1");
		move_tpch_hive_postgres.add("Constraints.Input0.DataInfo.Attributes.number","1");
		move_tpch_hive_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","SQL");
//		move_tpch_hive_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
//               move_tpch_hive_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr3.type","Varchar");
//		move_tpch_hive_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr4.type","Varchar");
//		move_tpch_hive_postgres.add("Constraints.Input0.Engine.DB.Relational.MySQL.schema","...");
//		move_tpch_hive_postgres.add("Constraints.Input0.Engine.DB.Relational.MySQL.location","127.0.0.1");

		move_tpch_hive_postgres.add("Constraints.Output0.DataInfo.Attributes.number","1");
		move_tpch_hive_postgres.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","SQL");
//		move_tpch_hive_postgres.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","Varchar");
//		move_tpch_hive_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
//		move_tpch_hive_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
//		move_tpch_hive_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
//		System.out.println("\nDirectory:begin-------------------------");
		move_tpch_hive_postgres.add("Constraints.OpSpecification.Algorithm.name","move");
//		move_tpch_hive_postgres.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
                double[][] estimateValue = ReadMatrixCSV.readMatrix(OperatorFolder+"/"+nameOp+"/data/no_delay_training_estimate.csv",1);
		move_tpch_hive_postgres.add("Optimization.execTime", Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]));
		move_tpch_hive_postgres.add("Optimization.cost", Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]/10)); 
                operators.add(move_tpch_hive_postgres);
                
                nameOp = "Move_TPCH_Postgres_Hive";
                Operator move_tpch_postgres_hive = new Operator(nameOp,"/tmp");
                move_tpch_postgres_hive.add("Constraints.Input.number","1");
		move_tpch_postgres_hive.add("Constraints.Output.number","1");
		move_tpch_postgres_hive.add("Constraints.Input0.DataInfo.Attributes.number","1");
		move_tpch_postgres_hive.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","SQL");
//		move_tpch_postgres_hive.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
//                move_tpch_postgres_hive.add("Constraints.Input0.DataInfo.Attributes.Atr3.type","Varchar");
//		move_tpch_postgres_hive.add("Constraints.Input0.DataInfo.Attributes.Atr4.type","Varchar");
//		move_tpch_postgres_hive.add("Constraints.Input0.Engine.DB.Relational.MySQL.schema","...");
//		move_tpch_postgres_hive.add("Constraints.Input0.Engine.DB.Relational.MySQL.location","127.0.0.1");

		move_tpch_postgres_hive.add("Constraints.Output0.DataInfo.Attributes.number","1");
		move_tpch_postgres_hive.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","SQL");
//		move_tpch_postgres_hive.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
//		move_tpch_postgres_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
//		move_tpch_postgres_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
//		move_tpch_postgres_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		move_tpch_postgres_hive.add("Constraints.OpSpecification.Algorithm.name","move");
//		move_tpch_postgres_hive.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
                estimateValue = ReadMatrixCSV.readMatrix(OperatorFolder+"/"+nameOp+"/data/no_delay_training_estimate.csv",1);
		move_tpch_postgres_hive.add("Optimization.execTime", Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]));              
		move_tpch_postgres_hive.add("Optimization.cost", Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]/10));              
		operators.add(move_tpch_postgres_hive);
/*                
                nameOp = "Move_TPCH_Hive_Hive";
                Operator move_tpch_hive_hive = new Operator(nameOp,"/tmp");
                move_tpch_hive_hive.add("Constraints.Input.number","1");
		move_tpch_hive_hive.add("Constraints.Output.number","1");
		move_tpch_hive_hive.add("Constraints.Input0.DataInfo.Attributes.number","2");
		move_tpch_hive_hive.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","Varchar");
		move_tpch_hive_hive.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
		move_tpch_hive_hive.add("Constraints.Input0.Engine.DB.Relational.MySQL.schema","...");
		move_tpch_hive_hive.add("Constraints.Input0.Engine.DB.Relational.MySQL.location","127.0.0.1");

		move_tpch_hive_hive.add("Constraints.Output0.DataInfo.Attributes.number","2");
		move_tpch_hive_hive.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		move_tpch_hive_hive.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		move_tpch_hive_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		move_tpch_hive_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		move_tpch_hive_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		move_tpch_hive_hive.add("Constraints.OpSpecification.Algorithm.Move","");
		move_tpch_hive_hive.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
                estimateValue = ReadMatrixCSV.readMatrix(OperatorFolder+"/"+nameOp+"/data/no_delay_training_estimate.csv",1);
		move_tpch_hive_hive.add("Optimization.execTime", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]));              
		move_tpch_hive_hive.add("Optimization.cost", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]/10);              
		operators.add(move_tpch_hive_hive);
                
                nameOp = "Move_TPCH_Postgres_Postgres";
                Operator move_tpch_postgres_postgres = new Operator(nameOp,"/tmp");
                move_tpch_postgres_postgres.add("Constraints.Input.number","1");
		move_tpch_postgres_postgres.add("Constraints.Output.number","1");
		move_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.number","4");
		move_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","Varchar");
		move_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
                move_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr3.type","Varchar");
		move_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr4.type","Varchar");
		move_tpch_postgres_postgres.add("Constraints.Input0.Engine.DB.Relational.MySQL.schema","...");
		move_tpch_postgres_postgres.add("Constraints.Input0.Engine.DB.Relational.MySQL.location","127.0.0.1");

		move_tpch_postgres_postgres.add("Constraints.Output0.DataInfo.Attributes.number","2");
		move_tpch_postgres_postgres.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		move_tpch_postgres_postgres.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		move_tpch_postgres_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		move_tpch_postgres_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		move_tpch_postgres_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		move_tpch_postgres_postgres.add("Constraints.OpSpecification.Algorithm.Move","");
		move_tpch_postgres_postgres.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
                estimateValue = ReadMatrixCSV.readMatrix(OperatorFolder+"/"+nameOp+"/data/no_delay_training_estimate.csv",1);
		move_tpch_postgres_postgres.add("Optimization.execTime", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]));              
		move_tpch_postgres_postgres.add("Optimization.cost", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]/10);              
		operators.add(move_tpch_postgres_postgres);
                
                nameOp = "Join_TPCH_Hive_Hive";
                Operator join_tpch_hive_hive = new Operator(nameOp, "/tmp");
		join_tpch_hive_hive.add("Constraints.Input.number","2");
		join_tpch_hive_hive.add("Constraints.Output.number","1");
		join_tpch_hive_hive.add("Constraints.Input0.DataInfo.Attributes.number","2");
		join_tpch_hive_hive.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","Varchar");
		join_tpch_hive_hive.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
		join_tpch_hive_hive.add("Constraints.Input0.Engine.DB.NoSQL.HBase.key","Atr1");
		join_tpch_hive_hive.add("Constraints.Input0.Engine.DB.NoSQL.HBase.value","Atr2");
		join_tpch_hive_hive.add("Constraints.Input0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		join_tpch_hive_hive.add("Constraints.Input1.DataInfo.Attributes.number","2");
		join_tpch_hive_hive.add("Constraints.Input1.DataInfo.Attributes.Atr1.type","Varchar");
		join_tpch_hive_hive.add("Constraints.Input1.DataInfo.Attributes.Atr2.type","Varchar");
		join_tpch_hive_hive.add("Constraints.Input1.Engine.DB.Relational.MySQL.schema","...");
		join_tpch_hive_hive.add("Constraints.Input1.Engine.DB.Relational.MySQL.location","127.0.0.1");

		join_tpch_hive_hive.add("Constraints.Output0.DataInfo.Attributes.number","1");
		join_tpch_hive_hive.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","Varchar");
		join_tpch_hive_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		join_tpch_hive_hive.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		join_tpch_hive_hive.add("Constraints.OpSpecification.Algorithm.Join.JoinCondition","in1.atr1 = in2.atr2");
		join_tpch_hive_hive.add("Constraints.OpSpecification.Algorithm.Join.type", "Join");
		
		join_tpch_hive_hive.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
                estimateValue = ReadMatrixCSV.readMatrix(OperatorFolder+"/"+nameOp+"/data/no_delay_training_estimate.csv",1);
		join_tpch_hive_hive.add("Optimization.execTime", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]));              
		join_tpch_hive_hive.add("Optimization.cost", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]/10);              
		operators.add(join_tpch_hive_hive);
                
                nameOp = "Join_TPCH_Postgres_Postgres";
                Operator join_tpch_postgres_postgres = new Operator(nameOp, "/tmp");
		join_tpch_postgres_postgres.add("Constraints.Input.number","2");
		join_tpch_postgres_postgres.add("Constraints.Output.number","1");
		join_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.number","2");
		join_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","Varchar");
		join_tpch_postgres_postgres.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
		join_tpch_postgres_postgres.add("Constraints.Input0.Engine.DB.NoSQL.HBase.key","Atr1");
		join_tpch_postgres_postgres.add("Constraints.Input0.Engine.DB.NoSQL.HBase.value","Atr2");
		join_tpch_postgres_postgres.add("Constraints.Input0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		join_tpch_postgres_postgres.add("Constraints.Input1.DataInfo.Attributes.number","2");
		join_tpch_postgres_postgres.add("Constraints.Input1.DataInfo.Attributes.Atr1.type","Varchar");
		join_tpch_postgres_postgres.add("Constraints.Input1.DataInfo.Attributes.Atr2.type","Varchar");
		join_tpch_postgres_postgres.add("Constraints.Input1.Engine.DB.Relational.MySQL.schema","...");
		join_tpch_postgres_postgres.add("Constraints.Input1.Engine.DB.Relational.MySQL.location","127.0.0.1");

		join_tpch_postgres_postgres.add("Constraints.Output0.DataInfo.Attributes.number","1");
		join_tpch_postgres_postgres.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","Varchar");
		join_tpch_postgres_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		join_tpch_postgres_postgres.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		join_tpch_postgres_postgres.add("Constraints.OpSpecification.Algorithm.Join.JoinCondition","in1.atr1 = in2.atr2");
		join_tpch_postgres_postgres.add("Constraints.OpSpecification.Algorithm.Join.type", "Join");
		
		join_tpch_postgres_postgres.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
                estimateValue = ReadMatrixCSV.readMatrix(OperatorFolder+"/"+nameOp+"/data/no_delay_training_estimate.csv",1);
		join_tpch_postgres_postgres.add("Optimization.execTime", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]));              
		join_tpch_postgres_postgres.add("Optimization.cost", "1.0");//Double.toString(estimateValue[estimateValue.length-1][estimateValue[0].length-1]/10);              
		operators.add(join_tpch_postgres_postgres);
*/
/*		Operator op = new Operator("HBase_HashJoin", "/tmp");
		op.add("Constraints.Input.number","2");
		op.add("Constraints.Output.number","1");
		op.add("Constraints.Input0.DataInfo.Attributes.number","2");
		op.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","ByteWritable");
		op.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		op.add("Constraints.Input0.Engine.DB.NoSQL.HBase.key","Atr1");
		op.add("Constraints.Input0.Engine.DB.NoSQL.HBase.value","Atr2");
		op.add("Constraints.Input0.Engine.DB.NoSQL.HBase.location","127.0.0.1");

		op.add("Constraints.Input1.DataInfo.Attributes.number","2");
		op.add("Constraints.Input1.DataInfo.Attributes.Atr1.type","ByteWritable");
		op.add("Constraints.Input1.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		op.add("Constraints.Input1.Engine.DB.NoSQL.HBase.key","Atr1");
		op.add("Constraints.Input1.Engine.DB.NoSQL.HBase.value","Atr2");
		op.add("Constraints.Input1.Engine.DB.NoSQL.HBase.location","127.0.0.1");

		op.add("Constraints.Output0.DataInfo.Attributes.number","2");
		op.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		op.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		op.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		op.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		op.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		op.add("Constraints.OpSpecification.Algorithm.Join.JoinCondition","in1.atr1 = in2.atr2");
		op.add("Constraints.OpSpecification.Algorithm.Join.type", "HashJoin");

		op.add("Constraints.EngineSpecification.Distributed.MapReduce.masterLocation", "127.0.0.1");
		op.add("Optimization.execTime", "100.0");
		//op.add("Properties.MaintainTags", ".*");

		Operator op1 = new Operator("Java_SortMergeJoin", "/tmp");
		op1.add("Constraints.Input.number","2");
		op1.add("Constraints.Output.number","1");
		op1.add("Constraints.Input0.DataInfo.Attributes.number","2");
		op1.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","ByteWritable");
		op1.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		op1.add("Constraints.Input0.Engine.DB.NoSQL.HBase.key","Atr1");
		op1.add("Constraints.Input0.Engine.DB.NoSQL.HBase.value","Atr2");
		op1.add("Constraints.Input0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		op1.add("Constraints.Input1.DataInfo.Attributes.number","2");
		op1.add("Constraints.Input1.DataInfo.Attributes.Atr1.type","Varchar");
		op1.add("Constraints.Input1.DataInfo.Attributes.Atr2.type","Varchar");
		op1.add("Constraints.Input1.Engine.DB.Relational.MySQL.schema","...");
		op1.add("Constraints.Input1.Engine.DB.Relational.MySQL.location","127.0.0.1");


		op1.add("Constraints.Output0.DataInfo.Attributes.number","2");
		op1.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		op1.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		op1.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		op1.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		op1.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		op1.add("Constraints.OpSpecification.Algorithm.Join.JoinCondition","in1.atr1 = in2.atr2");
		op1.add("Constraints.OpSpecification.Algorithm.Join.type", "SortMergeJoin");
		
		op1.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
		op1.add("Optimization.execTime", "1000.0");
		
		Operator sort = new Operator("Sort", "/tmp");
		sort.add("Constraints.Input.number","1");
		sort.add("Constraints.Output.number","1");
		sort.add("Constraints.Input0.DataInfo.Attributes.number","2");
		sort.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","ByteWritable");
		sort.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		sort.add("Constraints.Input0.Engine.DB.NoSQL.HBase.key","Atr1");
		sort.add("Constraints.Input0.Engine.DB.NoSQL.HBase.value","Atr2");
		sort.add("Constraints.Input0.Engine.DB.NoSQL.HBase.location","127.0.0.1");

		sort.add("Constraints.Output0.DataInfo.Attributes.number","2");
		sort.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		sort.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		sort.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		sort.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		sort.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		sort.add("Constraints.OpSpecification.Algorithm.Sort.sortingOrder","in1.atr1");
		sort.add("Constraints.OpSpecification.Algorithm.Sort.type", "ExternalSort");
		sort.add("Constraints.EngineSpecification.Distributed.MapReduce.masterLocation", "127.0.0.1");
		sort.add("Optimization.execTime", "10.0");
		

		Operator sort2 = new Operator("Sort2", "/tmp");
		sort2.add("Constraints.Input.number","1");
		sort2.add("Constraints.Output.number","1");
		sort2.add("Constraints.Input0.DataInfo.Attributes.number","2");
		sort2.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","ByteWritable");
		sort2.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		sort2.add("Constraints.Input0.Engine.DB.NoSQL.HBase.key","Atr1");
		sort2.add("Constraints.Input0.Engine.DB.NoSQL.HBase.value","Atr2");
		sort2.add("Constraints.Input0.Engine.DB.NoSQL.HBase.location","127.0.0.1");

		sort2.add("Constraints.Output0.DataInfo.Attributes.number","2");
		sort2.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		sort2.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		sort2.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		sort2.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		sort2.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		sort2.add("Constraints.OpSpecification.Algorithm.Sort.sortingOrder","in1.atr1");
		sort2.add("Constraints.OpSpecification.Algorithm.Sort.type", "ExternalSort");
		sort2.add("Constraints.EngineSpecification.Distributed.MapReduce.masterLocation", "127.0.0.1");
		sort2.add("Optimization.execTime", "100.0");

		Operator move = new Operator("Move_MySQL_HBase", "/tmp");
		move.add("Constraints.Input.number","1");
		move.add("Constraints.Output.number","1");
		move.add("Constraints.Input0.DataInfo.Attributes.number","2");
		move.add("Constraints.Input0.DataInfo.Attributes.Atr1.type","Varchar");
		move.add("Constraints.Input0.DataInfo.Attributes.Atr2.type","Varchar");
		move.add("Constraints.Input0.Engine.DB.Relational.MySQL.schema","...");
		move.add("Constraints.Input0.Engine.DB.Relational.MySQL.location","127.0.0.1");

		move.add("Constraints.Output0.DataInfo.Attributes.number","2");
		move.add("Constraints.Output0.DataInfo.Attributes.Atr1.type","ByteWritable");
		move.add("Constraints.Output0.DataInfo.Attributes.Atr2.type","List<ByteWritable>");
		move.add("Constraints.Output0.Engine.DB.NoSQL.HBase.key","Atr1");
		move.add("Constraints.Output0.Engine.DB.NoSQL.HBase.value","Atr2");
		move.add("Constraints.Output0.Engine.DB.NoSQL.HBase.location","127.0.0.1");
		
		move.add("Constraints.OpSpecification.Algorithm.Move","");
		move.add("Constraints.EngineSpecification.Centralized.Java.location", "127.0.0.1");
		move.add("Optimization.execTime", "100.0");
		
		operators.add(op);
		operators.add(op1);
		operators.add(sort);
		operators.add(sort2);
		operators.add(move);
*/		
		
	}

	public static void main(String[] args) {
		new MaterializedOperators();
	}

	public List<Operator> getMatches(AbstractOperator abstractOperator){
		List<Operator> ret = new ArrayList<Operator>();
		for(Operator op : operators){
			if(abstractOperator.checkMatch(op))
				ret.add(op);
		}
		return ret;
	}

	public List<Operator> checkMove(Dataset from, Dataset to) {
		AbstractOperator abstractMove = new AbstractOperator("move");
		abstractMove.moveOperator(from,to);
		return getMatches(abstractMove);
	}
}
