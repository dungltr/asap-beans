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


package gr.ntua.cslab.asap.utils;

import gr.ntua.ece.cslab.panic.core.containers.beans.InputSpacePoint;
import gr.ntua.ece.cslab.panic.core.containers.beans.OutputSpacePoint;

import java.util.ArrayList;

/**
 * Created by victor on 10/14/15.
 */
public interface DataSource {
   /**
    * --- getOutputSpacePoints ---
 * @param metric 
    * @return An ArrayList of output space points to be given as input to PANIC models for training
    */
   ArrayList<OutputSpacePoint> getOutputSpacePoints(String metric);

ArrayList<OutputSpacePoint> getOutputSpacePoints();
}
