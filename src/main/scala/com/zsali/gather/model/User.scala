package com.zsali.gather.model

import java.{time => jt}

case class User(id: String,
                firstName: String,
                lastName: String,
                birthday: jt.LocalDate,
                email: String)

case class UserReq(firstName: String,
                   lastName: String,
                   birthday: String,
                   email: String)