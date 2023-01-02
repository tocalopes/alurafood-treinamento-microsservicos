package service;

import dto.PagamentoDto;
import enums.Status;
import jakarta.persistence.EntityNotFoundException;
import model.Pagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<PagamentoDto> listAll(Pageable pageableConfig){
        return repository
                .findAll(pageableConfig)
                .map(p -> modelMapper.map(p, PagamentoDto.class));
    }

    public PagamentoDto findById(Long id){
        PagamentoDto pagamento= repository.findById(id).map(p -> modelMapper.map(p,PagamentoDto.class))
                .orElseThrow(EntityNotFoundException::new);
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
}
