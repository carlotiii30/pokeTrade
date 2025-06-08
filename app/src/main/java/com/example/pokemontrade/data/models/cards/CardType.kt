package com.example.pokemontrade.data.models.cards

enum class CardType {
    AGUA,
    FUEGO,
    PLANTA,
    ELECTRICO,
    PSIQUICO,
    LUCHA,
    DRAGON,
    OSCURO,
    HADA,
    ACERO,
    NORMAL
}

fun CardType.toBackendValue(): String {
    return when (this) {
        CardType.AGUA -> "water"
        CardType.FUEGO -> "fire"
        CardType.PLANTA -> "grass"
        CardType.ELECTRICO -> "electric"
        CardType.PSIQUICO -> "psychic"
        CardType.LUCHA -> "fighting"
        CardType.DRAGON -> "dragon"
        CardType.OSCURO -> "darkness"
        CardType.HADA -> "fairy"
        CardType.ACERO -> "metal"
        CardType.NORMAL -> "normal"
    }
}

