/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.core.validator.statements;


import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class InsertIntoStatementTest extends BasicValidatorTest {


    @Test
    public void validateBasicOk(){
        String inputText = "INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateOk(inputText, "validateBasicOk");
    }
    
    @Test
    public void validateBasicNoColumns(){
        String inputText = "INSERT INTO demo.users VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateOk(inputText, "validateBasicNoColumns");
    }
    
    @Test
    public void validateBasicNoKs(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validateBasicNoKs");
    }

    @Test
    public void validateUnknownColumn(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validateUnknownColumn");
    }

    @Test
    public void validateBooleanColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, 'true', '');";
        validateFail(inputText, "validateBooleanColumnFail");
    }

    @Test
    public void validateIntegerColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', '10', true, '');";
        validateFail(inputText, "validateIntegerColumnFail");
    }

    @Test
    public void validateTextColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES (true, 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validateTextColumnFail");
    }

    @Test
    public void validateStratioColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase, stratio_lucene_index_1) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '', 'error');";
        validateFail(inputText, "validateStratioColumnFail");
    }

}
