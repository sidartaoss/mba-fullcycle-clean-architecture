package br.com.fullcycle.infrastructure.dtos;

public record SubscribeDTO(
        String customerId,
        String eventId
) {
    public static SubscribeDTO with(final String aCustomerId) {
        return new SubscribeDTO(aCustomerId, null);
    }
}
