package br.sc.weg.sid.model.service.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AtualizaScoreDemandaScheduled {

    @Scheduled(cron = "0 0 0 * * *") // Executa todos os dias à meia-noite
    public void atualizarScoreDiariamente() {
        // Lógica para atualizar o score das demandas diariamente
    }

}
