package br.com.fullcycle.infrastructure.dtos;

import java.util.UUID;

public record NewEventDTO(
        String name,
        String date,
        int totalSpots,
        UUID partnerId
) {

}
