package com.docubox.data.mapper

import com.docubox.data.local.models.User
import com.docubox.data.remote.models.UserDto
import javax.inject.Inject

// Class to map single and multiple users from local data to remote dto (data transfer object) and vice versa
fun User.toRemote(): UserDto = UserDto(
    id = this.id,
    userEmail = this.userEmail,
    userName = this.userName,
    token = this.token,
    rootDirectory = listOf(this.rootDirectory)
)

fun UserDto.toLocal(): User = User(
    id = this.id,
    userEmail = this.userEmail,
    userName = this.userName,
    token = this.token,
    rootDirectory = this.rootDirectory.getOrNull(0) ?: ""
)

fun List<User>.toRemote(): List<UserDto> = this.map { it.toRemote() }

fun List<UserDto>.toLocal(): List<User> = this.map { it.toLocal() }
