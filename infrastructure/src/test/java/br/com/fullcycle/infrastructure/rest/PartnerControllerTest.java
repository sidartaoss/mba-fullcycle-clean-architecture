package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.dtos.NewPartnerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PartnerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void setUp() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreate() throws Exception {

        final var partner = NewPartnerDTO.with(
                "John Doe", "29.168.815/0001-66", "john.doe@gmail.com");

        final var result = this.mvc.perform(
                        MockMvcRequestBuilders.post("/partners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(partner))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andReturn().getResponse().getContentAsByteArray();

        var actualResponse = mapper.readValue(result, CreatePartnerUseCase.Output.class);
        Assertions.assertEquals(partner.name(), actualResponse.name());
        Assertions.assertEquals(partner.cnpj(), actualResponse.cnpj());
        Assertions.assertEquals(partner.email(), actualResponse.email());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {

        final var expectedName = "John Doe";
        final var expectedCnpj = "29.168.815/0001-66";
        final var partner1 = NewPartnerDTO.with(
                expectedName, expectedCnpj, "john.doe@gmail.com");

        // Cria o primeiro parceiro
        this.mvc.perform(
                        MockMvcRequestBuilders.post("/partners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(partner1))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andReturn().getResponse().getContentAsByteArray();

        final var partner2 = NewPartnerDTO.with(expectedName, expectedCnpj, "john2@gmail.com");

        // Tenta criar o segundo parceiro com o mesmo CNPJ
        this.mvc.perform(
                        MockMvcRequestBuilders.post("/partners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(partner2))
                )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Partner already exists"));
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@gmail.com";
        final var partner1 = NewPartnerDTO.with(
                expectedName, "29.168.815/0001-66", expectedEmail);

        // Cria o primeiro parceiro
        this.mvc.perform(
                        MockMvcRequestBuilders.post("/partners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(partner1))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andReturn().getResponse().getContentAsByteArray();

        final var partner2 = NewPartnerDTO.with(
                expectedName, "21.188.756/0001-86", expectedEmail);

        // Tenta criar o segundo parceiro com o mesmo CNPJ
        this.mvc.perform(
                        MockMvcRequestBuilders.post("/partners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(partner2))
                )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Partner already exists"));
    }

    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGet() throws Exception {

        final var partner = NewPartnerDTO.with(
                "John Doe", "26.369.870/0001-26", "john.doe@gmail.com");

        final var createResult = this.mvc.perform(
                        MockMvcRequestBuilders.post("/partners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(partner))
                )
                .andReturn().getResponse().getContentAsByteArray();

        var partnerId = mapper.readValue(createResult, CreatePartnerUseCase.Output.class).id();

        final var result = this.mvc.perform(
                        MockMvcRequestBuilders.get("/partners/{id}", partnerId)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        var actualResponse = mapper.readValue(result, GetPartnerByIdUseCase.Output.class);
        Assertions.assertEquals(partnerId, actualResponse.id());
        Assertions.assertEquals(partner.name(), actualResponse.name());
        Assertions.assertEquals(partner.cnpj(), actualResponse.cnpj());
        Assertions.assertEquals(partner.email(), actualResponse.email());
    }
}
