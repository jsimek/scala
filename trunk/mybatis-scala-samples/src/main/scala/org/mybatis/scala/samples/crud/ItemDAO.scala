/*
 * Copyright 2011 The myBatis Team
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

package org.mybatis.scala.samples.crud

import org.mybatis.scala.mapping._
import org.mybatis.scala.session.Session

object ItemDAO {

  val ItemResultMap = new ResultMap[Item] {
    id ( column = "id_", property = "id" )
    result ( column = "description_", property = "description" )
  }

  val selectSql  =
    <xsql>
      SELECT *
      FROM item
    </xsql>

  val findAll = new SelectList[Nothing,Item] {
    resultMap = ItemResultMap
    def xsql = selectSql
  }

  val findByDescription = new SelectList[String,Item] {
    resultMap = ItemResultMap
    def xsql =
      <xsql>
        {selectSql}
        WHERE description_ LIKE #{{description}}
        ORDER BY description_
      </xsql>
  }

  val findById = new SelectOne[Int,Item] {
    resultMap = ItemResultMap
    def xsql =
      <xsql>
        {selectSql}
        WHERE id_ = #{{id}}
      </xsql>
  }

  val insert = new Insert[Item] {
    def xsql =
      <xsql>
        INSERT INTO item(description_)
        VALUES (#{{description}})
      </xsql>
  }

  val update = new Update[Item] {
    def xsql =
      <xsql>
        UPDATE item
        SET description_ = #{{description}}
        WHERE id_ = #{{id}}
      </xsql>
  }

  val deleteSql =
    <xsql>
      DELETE FROM item
      WHERE id_ = #{{id}}
    </xsql>

  val delete = new Delete[Item] {
    def xsql = deleteSql
  }

  val deleteById = new Delete[Int] {
    def xsql = deleteSql
  }

  val dropTable = new Update[Nothing] {
    def xsql =
      <xsql>
        DROP TABLE IF EXISTS item
      </xsql>
  }

  val createTable = new Update[Nothing] {
    def xsql =
      <xsql>
        CREATE TABLE item (
          id_ serial,
          description_ varchar(255) not null,
          primary key (id_)
        )
      </xsql>
  }

  def initdb(implicit s : Session) = {
    dropTable()
    createTable()
  }

  def bind = Seq(delete,update,insert,findById,findByDescription,deleteById,findAll,createTable,dropTable)

}