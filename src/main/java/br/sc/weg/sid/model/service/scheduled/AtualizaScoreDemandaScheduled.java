package br.sc.weg.sid.model.service.scheduled;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.utils.DemandaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AtualizaScoreDemandaScheduled {

    @Autowired
    DemandaService demandaService;

    // Executa todos os dias à 00:00:00
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void atualizarScoreDiariamente() {

        List<Demanda> demandas = demandaService.findAll();
        DemandaUtil demandaUtil = new DemandaUtil();
        demandas.forEach(demanda -> {
            Optional<Demanda> demandaParaAtualizar = demandaService.findById(demanda.getIdDemanda());
            demandaService.save(demandaUtil.atualizaScoreDiario(demandaParaAtualizar.get()));
        });
    }

}
