package br.com.alurafood.pagamentos.HttpClient;

import br.com.alurafood.pagamentos.dto.ItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("pedidos-ms")
public interface PedidosClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/pedidos/{id}/pago")
    void updatePedido(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/pedidos/itens/{id}")
    ResponseEntity<List<ItemDto>> getItensPedido(@PathVariable Long id);


}
