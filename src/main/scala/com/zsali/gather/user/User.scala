package com.zsali.gather.user

import java.{time => jt}

case class User(firstName: String,
                lastName: String,
                birthday: jt.LocalDate,
                email: String,
                createdAt: String,
                updatedAt: String)