package com.docubox.data.modes.mapper

// An interface to map data and data list from local to remote and vice versa
interface Mapper<Local, Remote> {

    fun toRemote(local: Local): Remote

    fun toLocal(remote: Remote): Local

    fun toRemote(local: List<Local>): List<Remote>

    fun toLocal(remote: List<Remote>): List<Local>
}
