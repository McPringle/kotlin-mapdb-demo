/*
 * Kotlin MapDB Demo
 * Copyright (C) 2018 Marcus Fihlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ch.fihlon.demo.kotlin.mapdb

import org.mapdb.DBMaker
import org.mapdb.HTreeMap
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*


fun main(args: Array<String>) {
    // create a file path for the current working directory
    val userDir = System.getProperty("user.dir")
    val dbPath = Paths.get(userDir, "demo.db")
    val dbFile = dbPath.toFile()
    println("Using persistence store: $dbFile")

    // create a file storage
    val db = DBMaker.fileDB(dbFile)
        .fileMmapEnableIfSupported()
        .transactionEnable()
        .closeOnJvmShutdown()
        .make()

    // create a map which is backed up by the file storage
    val persons = db.hashMap("persons").createOrOpen() as HTreeMap<UUID, Person>

    // create an object and add it to the map
    val p = Person(UUID.randomUUID(), "Random", "Person", LocalDateTime.now())
    persons[p.personId] = p

    // transaction support, don't forget to commit…
    db.commit()

    // print all persons
    persons.forEach(System.out::println)
}
