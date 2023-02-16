package com.slowerror.locationreminders.data.mapper

interface Mapper<E, D> {

    fun mapToDomain(type: E): D

    fun mapToData(type: D): E
}