package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.PropostasLog;
import br.sc.weg.sid.repository.PropostaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropostaLogService {

    @Autowired
    PropostaLogRepository propostaLogRepository;

    public void save(PropostasLog propostasLog) {
        propostaLogRepository.save(propostasLog);
    }
}
