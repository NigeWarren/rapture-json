/**********************************************************************************************\
* Rapture JSON Library                                                                         *
* Version 0.9.0                                                                                *
*                                                                                              *
* The primary distribution site is                                                             *
*                                                                                              *
*   http://rapture.io/                                                                         *
*                                                                                              *
* Copyright 2010-2014 Jon Pretty, Propensive Ltd.                                              *
*                                                                                              *
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file    *
* except in compliance with the License. You may obtain a copy of the License at               *
*                                                                                              *
*   http://www.apache.org/licenses/LICENSE-2.0                                                 *
*                                                                                              *
* Unless required by applicable law or agreed to in writing, software distributed under the    *
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,    *
* either express or implied. See the License for the specific language governing permissions   *
* and limitations under the License.                                                           *
\**********************************************************************************************/
package rapture.json

import rapture.core._
import rapture.data._

import language.higherKinds
import language.experimental.macros

object `package` extends Serializers with Extractors {

  implicit def jsonExtractorMacro[T <: Product]: Extractor[T, Json] =
    macro JsonMacros.jsonExtractorMacro[T]
  
  implicit def jsonBufferExtractorMacro[T <: Product]: Extractor[T, JsonBuffer] =
    macro JsonMacros.jsonBufferExtractorMacro[T]
  
  implicit def serializerMacro[T <: Product](implicit ast: JsonAst): Serializer[T] =
    macro Macros.serializerMacro[T]
  
  implicit def jsonStrings(sc: StringContext)(implicit parser: Parser[String, JsonAst]) =
    new JsonStrings(sc)

}
