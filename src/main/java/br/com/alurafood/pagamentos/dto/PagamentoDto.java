package br.com.alurafood.pagamentos.dto;

import br.com.alurafood.pagamentos.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.alurafood.pagamentos.model.Pagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDto {
    private Long id;
    private BigDecimal valor;
    private String nome;
    private String numero;
    private String expiracao;
    private String codigo;
    private Status status;
    private Long formaDePagamentoId;
    private Long pedidoId;

    private List<ItemDto> itens = new ArrayList<>();

    PagamentoDto(Pagamento pagamento){

    }

}
