package com.docubox.data.modes.mapper

import com.docubox.data.modes.local.User
import com.docubox.data.modes.remote.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() : Mapper<User, UserDto> {
    override fun toRemote(local: User): UserDto = UserDto(
        id = local.id,
        userEmail = local.userEmail,
        userName = local.userName,
        token = local.token
    )

    override fun toLocal(remote: UserDto): User = User(
        id = remote.id,
        userEmail = remote.userEmail,
        userName = remote.userName,
        token = remote.token
    )

    override fun toRemote(local: List<User>): List<UserDto> = local.map { toRemote(it) }

    override fun toLocal(remote: List<UserDto>): List<User> = remote.map { toLocal(it) }
}
