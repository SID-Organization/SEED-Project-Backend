package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.BusBeneficiadas;
import br.sc.weg.sid.repository.BusBeneficiadasRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BusBeneficiadasService {

    @Autowired
    BusBeneficiadasRepository busBeneficiadasRepository;

    public List<BusBeneficiadas> findAll() {
        return busBeneficiadasRepository.findAll();
    }

    public <S extends BusBeneficiadas> S save(S entity) {
        return busBeneficiadasRepository.save(entity);
    }

    public Optional<BusBeneficiadas> findById(Integer integer) {
        return busBeneficiadasRepository.findById(integer);
    }

    public void deleteById(Integer integer) {
        busBeneficiadasRepository.deleteById(integer);
    }
}
