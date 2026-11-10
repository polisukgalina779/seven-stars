package com.Vugfeve7.Hidran.domain.usecase

import com.Vugfeve7.Hidran.domain.model.Room

class AwardStarsUseCase {

    operator fun invoke(room: Room, won: Int): Int = when {
        won >= room.goalThreeStars -> 3
        won >= room.goalTwoStars -> 2
        won >= room.goalOneStar -> 1
        else -> 0
    }
}
