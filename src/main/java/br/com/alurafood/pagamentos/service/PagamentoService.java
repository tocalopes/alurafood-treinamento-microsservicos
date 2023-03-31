package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.HttpClient.PedidosClient;
import br.com.alurafood.pagamentos.dto.PagamentoDto;
import br.com.alurafood.pagamentos.enums.Status;
import jakarta.persistence.EntityNotFoundException;
import br.com.alurafood.pagamentos.model.Pagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;

import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidosClient pedidosClient;
    public Page<PagamentoDto> listAll(Pageable pageableConfig){
        return repository
                .findAll(pageableConfig)
                .map(p -> modelMapper.map(p, PagamentoDto.class));
    }

    public PagamentoDto findById(Long id){
        PagamentoDto pagamento= repository.findById(id).map(p -> modelMapper.map(p,PagamentoDto.class))
                .orElseThrow(EntityNotFoundException::new);
        pagamento.setItens(pedidosClient.getItensPedido(pagamento.getPedidoId()).getBody());
        return pagamento;
    }

    public PagamentoDto create(PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        return modelMapper.map(repository.save(pagamento),PagamentoDto.class);
    }

    public  PagamentoDto update(Long id, PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto,Pagamento.class);
        pagamento.setId(id);
        return modelMapper.map(repository.save(pagamento),PagamentoDto.class);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public void confirmPagamento(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);
        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();
        }
        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedidosClient.updatePedido(pagamento.get().getPedidoId());
    }

    public void updateStatus(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);
        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();
        }
        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());
    }
}
