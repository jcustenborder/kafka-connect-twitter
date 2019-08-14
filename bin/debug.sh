#!/usr/bin/env bash
#
# Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

export DEBUG_SUSPEND_FLAG='y'
export KAFKA_DEBUG='y'
export JAVA_DEBUG_PORT='5006'
set -e

mvn clean package

connect-standalone config/connect-avro-docker.properties /Users/jeremy/source/opensource/kafka-connect/kafka-connect-twitter/config/TwitterSourceConnector.properties