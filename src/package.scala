/**********************************************************************************************\
* Rapture JSON Library                                                                         *
* Version 1.0.7                                                                                *
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

trait VeryLowPriorityPackage {
  implicit def jsonExtractorMacro[T <: Product]: Extractor[T, Json] =
    macro JsonMacros.jsonExtractorMacro[T]
  
  implicit def jsonBufferExtractorMacro[T <: Product]: Extractor[T, JsonBuffer] =
    macro JsonMacros.jsonBufferExtractorMacro[T]
  
  implicit def jsonSerializerMacro[T <: Product](implicit ast: JsonAst): Serializer[T, Json] =
    macro JsonMacros.jsonSerializerMacro[T]
  
  implicit def jsonBufferSerializerMacro[T <: Product](implicit ast: JsonBufferAst):
      Serializer[T, JsonBuffer] = macro JsonMacros.jsonBufferSerializerMacro[T]
}

trait LowPriorityPackage extends VeryLowPriorityPackage

object `package` extends Serializers with Extractors with LowPriorityPackage {

  implicit def jsonCastExtractor[T: JsonCastExtractor](implicit ast: JsonAst):
      Extractor[T, JsonDataType[_, _ <: JsonAst]] =
    new Extractor[T, JsonDataType[_, _ <: JsonAst]] {
      def construct(value: JsonDataType[_, _ <: JsonAst], ast2: DataAst): T =
        ast2 match {
          case ast2: JsonAst =>
            if(ast == ast2) value.$normalize.asInstanceOf[T]
            else jsonSerializer.serialize(Json.construct(VCell(value.$normalize),
                Vector())(ast2)).asInstanceOf[T]
          case _ => ???
        }
    }

  implicit class DynamicWorkaround(json: Json) {
    def self: Json = json.selectDynamic("json")
  }

  implicit def jsonStrings(sc: StringContext)(implicit parser: Parser[String, JsonAst]) =
    new JsonStrings(sc)
  
  implicit def jsonBufferStrings(sc: StringContext)(implicit parser: Parser[String,
      JsonBufferAst]) = new JsonBufferStrings(sc)
}

