//package br.sc.weg.sid.utils;
//
//import br.sc.weg.sid.controller.DemandaController;
//import br.sc.weg.sid.model.entities.Beneficio;
//import br.sc.weg.sid.model.entities.BusinessUnity;
//import br.sc.weg.sid.model.entities.Demanda;
//import br.sc.weg.sid.model.entities.Usuario;
//import br.sc.weg.sid.model.enums.Cargo;
//import br.sc.weg.sid.model.enums.Moeda;
//import br.sc.weg.sid.model.enums.StatusDemanda;
//import br.sc.weg.sid.model.enums.TipoBeneficio;
//import br.sc.weg.sid.repository.BusinessUnityRepository;
//import br.sc.weg.sid.repository.DemandaRepository;
//import br.sc.weg.sid.repository.UsuarioRepository;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@AllArgsConstructor
//@NoArgsConstructor
//public class BancoUtils {
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//    @Autowired
//    private DemandaRepository demandaRepository;
//
//    @Autowired
//    private DemandaController demandaController;
//
//    @Autowired
//    private BusinessUnityRepository businessUnityRepository;
//
//
//    @PostConstruct
//    public void popularBanco() {
//
//        BusinessUnity businessUnity = new BusinessUnity();
//        businessUnity.setSiglaBusinessUnity("WDS");
//        businessUnity.setNomeBusinessUnity("WEG Digital");
//        businessUnity = businessUnityRepository.save(businessUnity);
//
//        Usuario solicitante = new Usuario();
//        solicitante.setNumeroCadastroUsuario(72130);
//        solicitante.setNomeUsuario("Michael Jordan");
//        solicitante.setDepartamentoUsuario(businessUnity);
//        solicitante.setBusinessUnity("WEG Equipamentos Elétricos S/A");
//        solicitante.setCargoUsuario(Cargo.SOLICITANTE);
//        solicitante.setEmailUsuario("mj.bulls@gamil.com");
//        solicitante.setSenhaUsuario("123");
//        solicitante = usuarioRepository.save(solicitante);
//
//        Usuario analista = new Usuario();
//        analista.setNumeroCadastroUsuario(72131);
//        analista.setNomeUsuario("Kobe Bryant");
//        analista.setDepartamentoUsuario(businessUnity);
//        analista.setBusinessUnity("WEG Equipamentos Elétricos S/A");
//        analista.setCargoUsuario(Cargo.ANALISTA);
//        analista.setEmailUsuario("kb.lakers@gamil.com");
//        analista.setSenhaUsuario("123");
//        analista = usuarioRepository.save(analista);
//
//        Usuario gerente = new Usuario();
//        gerente.setNumeroCadastroUsuario(72132);
//        gerente.setNomeUsuario("Lebron James");
//        gerente.setDepartamentoUsuario(businessUnity);
//        gerente.setBusinessUnity("WEG Equipamentos Elétricos S/A");
//        gerente.setCargoUsuario(Cargo.GERENTE);
//        gerente.setEmailUsuario("lbj.lakers@gamil.com");
//        gerente.setSenhaUsuario("123");
//        gerente = usuarioRepository.save(gerente);
//
//
//        Usuario gestorTI = new Usuario();
//        gestorTI.setNumeroCadastroUsuario(72133);
//        gestorTI.setNomeUsuario("Magic Johnson");
//        gestorTI.setDepartamentoUsuario(businessUnity);
//        gestorTI.setBusinessUnity("WEG Equipamentos Elétricos S/A");
//        gestorTI.setCargoUsuario(Cargo.GESTOR_TI);
//        gestorTI.setEmailUsuario("mgj.lakers@gamil.com");
//        gestorTI.setSenhaUsuario("123");
//        gestorTI = usuarioRepository.save(gestorTI);
//
//        List<Beneficio> listaBeneficios = new ArrayList<>();
//
//        Beneficio beneficioR = new Beneficio();
//        beneficioR.setIdFront(1);
//        beneficioR.setMoedaBeneficio(Moeda.EURO);
//        beneficioR.setValorBeneficio(2000.0);
//        beneficioR.setTipoBeneficio(TipoBeneficio.REAL);
//        beneficioR.setMemoriaCalculoBeneficio("doidera");
//        beneficioR.setMemoriaCalculoBeneficioHTML("<html> <p> doidera </p> </html>");
//
//        Beneficio beneficioP = new Beneficio();
//        beneficioP.setIdFront(2);
//        beneficioP.setMoedaBeneficio(Moeda.EURO);
//        beneficioP.setValorBeneficio(2500.0);
//        beneficioP.setTipoBeneficio(TipoBeneficio.POTENCIAL);
//        beneficioP.setMemoriaCalculoBeneficio("doidera");
//        beneficioP.setMemoriaCalculoBeneficioHTML("<html> <p> doidera </p> </html>");
//
//        Beneficio beneficioQ = new Beneficio();
//        beneficioQ.setIdFront(3);
//        beneficioQ.setTipoBeneficio(TipoBeneficio.QUALITATIVO);
//        beneficioQ.setMemoriaCalculoBeneficio("doidera");
//        beneficioQ.setMemoriaCalculoBeneficioHTML("<html> <p> doidera </p> </html>");
//
//        listaBeneficios.add(beneficioR);
//        listaBeneficios.add(beneficioP);
//        listaBeneficios.add(beneficioQ);
//
//
//        Demanda demanda = new Demanda();
//        demanda.setTituloDemanda("Sala de brinquedos");
//        demanda.setSituacaoAtualDemanda("Brinquedo é bom!");
//        demanda.setDescricaoQualitativoDemanda("sim");
//        demanda.setFrequenciaUsoDemanda("Muito usada");
//        demanda.setSolicitanteDemanda(solicitante);
//        demanda.setAnalistaResponsavelDemanda(analista);
//        demanda.setGerenteDaAreaDemanda(gerente);
//        demanda.setGestorResponsavelDemanda(gestorTI);
//        demanda.setBeneficiosDemanda(listaBeneficios);
//        demanda.setStatusDemanda(StatusDemanda.RASCUNHO);
//        demanda = demandaRepository.save(demanda);
//
//        demandaController.atualizarStatusDemanda(demanda.getIdDemanda(), StatusDemanda.ABERTA);
//
//
//    }
//}
