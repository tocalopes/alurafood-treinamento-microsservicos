package br.com.alurafood.pagamentos.Controller;

import br.com.alurafood.pagamentos.service.PagamentoService;
import br.com.alurafood.pagamentos.dto.PagamentoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public Page<PagamentoDto> listAll(@PageableDefault(size = 10) Pageable pageable){
        return service.listAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDto> findById(@PathVariable @NotNull Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoDto> create(@RequestBody @Valid PagamentoDto dto, UriComponentsBuilder uriBuilder){
        PagamentoDto pagamento = service.create(dto);
        URI address = uriBuilder.path("/pagamento/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(address).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDto> create(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto, UriComponentsBuilder uriBuilder){
        PagamentoDto pagamento = service.update(id,dto);
        URI address = uriBuilder.path("/pagamento/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(address).body(pagamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDto> delete(@PathVariable @NotNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "updatePedido", fallbackMethod = "authorizedPagamentoWithPendentIntegration")
    public void confirmPagamento(@PathVariable @NotNull Long id){
        service.confirmPagamento(id);
    }

    public void authorizedPagamentoWithPendentIntegration(Long id, Exception e){
        service.updateStatus(id);
    }

}
