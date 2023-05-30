package br.sc.weg.sid.utils;

import br.sc.weg.sid.controller.DemandaController;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.Cargo;
import br.sc.weg.sid.model.enums.Moeda;
import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.enums.TipoBeneficio;
import br.sc.weg.sid.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class BancoUtils {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DemandaRepository demandaRepository;

    @Autowired
    private BusinessUnityRepository businessUnityRepository;

    @Autowired
    private SecaoTIResponsavelRepository secaoTIResponsavelRepository;

    @Autowired
    private BeneficioRepository beneficioRepository;

    @Autowired
    private ComissaoRepository comissaoRepository;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private DemandaController demandaController;


    @PostConstruct
    public void popularBanco() {

        List<SecaoTIResponsavel> listaSecaoTI = secaoTIResponsavelRepository.findAll();
        List<Comissao> listaComissao = comissaoRepository.findAll();
        List<Forum> listaForum = forumRepository.findAll();
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        List<BusinessUnity> listaBusinessUnity = businessUnityRepository.findAll();
        List<Beneficio> listaBeneficios = beneficioRepository.findAll();
        List<Demanda> listaDemanda = demandaRepository.findAll();

        if (listaSecaoTI.isEmpty()) {
            SecaoTIResponsavel secaoTIResponsavel = new SecaoTIResponsavel();
            secaoTIResponsavel.setNomeSecaoTIResponsavel("Seção Desenvolvimento Sistemas de Vendas e E-commerce");
            secaoTIResponsavel.setSiglaSecaoTIResponsavel("SVE");
            secaoTIResponsavelRepository.save(secaoTIResponsavel);

            SecaoTIResponsavel secaoTIResponsavel2 = new SecaoTIResponsavel();
            secaoTIResponsavel2.setNomeSecaoTIResponsavel("Seção Desenvolvimento Sistemas de manufatura");
            secaoTIResponsavel2.setSiglaSecaoTIResponsavel("SIM");
            secaoTIResponsavelRepository.save(secaoTIResponsavel2);

            SecaoTIResponsavel secaoTIResponsavel3 = new SecaoTIResponsavel();
            secaoTIResponsavel3.setNomeSecaoTIResponsavel("Seção Desenvolvimento Sistemas de Engenhar");
            secaoTIResponsavel3.setSiglaSecaoTIResponsavel("SIE");
            secaoTIResponsavelRepository.save(secaoTIResponsavel3);

            SecaoTIResponsavel secaoTIResponsavel4 = new SecaoTIResponsavel();
            secaoTIResponsavel4.setNomeSecaoTIResponsavel("Setor Desenvolvimento Plataforma Orchestra");
            secaoTIResponsavel4.setSiglaSecaoTIResponsavel("SDO");
            secaoTIResponsavelRepository.save(secaoTIResponsavel4);

            SecaoTIResponsavel secaoTIResponsavel5 = new SecaoTIResponsavel();
            secaoTIResponsavel5.setNomeSecaoTIResponsavel("Seção Desenvolvimento Sistemas Corporativos");
            secaoTIResponsavel5.setSiglaSecaoTIResponsavel("SCO");
            secaoTIResponsavelRepository.save(secaoTIResponsavel5);

            SecaoTIResponsavel secaoTIResponsavel6 = new SecaoTIResponsavel();
            secaoTIResponsavel6.setNomeSecaoTIResponsavel("Seção Projetos de TI");
            secaoTIResponsavel6.setSiglaSecaoTIResponsavel("PTI");
            secaoTIResponsavelRepository.save(secaoTIResponsavel6);

            SecaoTIResponsavel secaoTIResponsavel7 = new SecaoTIResponsavel();
            secaoTIResponsavel7.setNomeSecaoTIResponsavel("Seção Arquitetura e Governança de Dados");
            secaoTIResponsavel7.setSiglaSecaoTIResponsavel("AGD");
            secaoTIResponsavelRepository.save(secaoTIResponsavel7);

            SecaoTIResponsavel secaoTIResponsavel8 = new SecaoTIResponsavel();
            secaoTIResponsavel8.setNomeSecaoTIResponsavel("Seção Desenvolvimento Tecnologias Digitais");
            secaoTIResponsavel8.setSiglaSecaoTIResponsavel("STD");
            secaoTIResponsavelRepository.save(secaoTIResponsavel8);

            SecaoTIResponsavel secaoTIResponsavel9 = new SecaoTIResponsavel();
            secaoTIResponsavel9.setNomeSecaoTIResponsavel("Seção Tecnologia de Infraestrutura");
            secaoTIResponsavel9.setSiglaSecaoTIResponsavel("TIN");
            secaoTIResponsavelRepository.save(secaoTIResponsavel9);

            SecaoTIResponsavel secaoTIResponsavel10 = new SecaoTIResponsavel();
            secaoTIResponsavel10.setNomeSecaoTIResponsavel("Secao Suporte Global Infraestrutura");
            secaoTIResponsavel10.setSiglaSecaoTIResponsavel("SGI");
            secaoTIResponsavelRepository.save(secaoTIResponsavel10);

            SecaoTIResponsavel secaoTIResponsavel11 = new SecaoTIResponsavel();
            secaoTIResponsavel11.setNomeSecaoTIResponsavel("Secao Seguranca da Informacao e Riscos TI");
            secaoTIResponsavel11.setSiglaSecaoTIResponsavel("SEG");
            secaoTIResponsavelRepository.save(secaoTIResponsavel11);

            SecaoTIResponsavel secaoTIResponsavel12 = new SecaoTIResponsavel();
            secaoTIResponsavel12.setNomeSecaoTIResponsavel("Atendimento e serviços TI – América do Sul");
            secaoTIResponsavel12.setSiglaSecaoTIResponsavel("AAS");
            secaoTIResponsavelRepository.save(secaoTIResponsavel12);
        }

        Comissao comissao1 = new Comissao();
        if (listaComissao.isEmpty()) {
            comissao1.setNomeComissao("Comissão de Processos de Vendas e Desenvolvimento de produtos");
            comissao1.setSiglaComissao("CPVM");
            comissaoRepository.save(comissao1);

            Comissao comissao2 = new Comissao();
            comissao2.setNomeComissao("Comissão de Processos da Cadeia Integrada");
            comissao2.setSiglaComissao("CPGCI");
            comissaoRepository.save(comissao2);

            Comissao comissao3 = new Comissao();
            comissao3.setNomeComissao("Comissão de Processos de Gestão de Projetos");
            comissao3.setSiglaComissao("CPGPR");
            comissaoRepository.save(comissao3);

            Comissao comissao4 = new Comissao();
            comissao4.setNomeComissao("Comitê de Gestão de Processos de Negócio");
            comissao4.setSiglaComissao("CGPN");
            comissaoRepository.save(comissao4);

            Comissao comissao5 = new Comissao();
            comissao5.setNomeComissao("Comitê de TI");
            comissao5.setSiglaComissao("CTI");
            comissaoRepository.save(comissao5);

            Comissao comissao6 = new Comissao();
            comissao6.setNomeComissao("Comitê WEG Business Services");
            comissao6.setSiglaComissao("CWBS");
            comissaoRepository.save(comissao6);

            Comissao comissao7 = new Comissao();
            comissao7.setNomeComissao("Diretoria de TI");
            comissao7.setSiglaComissao("DTI");
            comissaoRepository.save(comissao7);
        }

        BusinessUnity businessUnity1 = new BusinessUnity();
        BusinessUnity businessUnity2 = new BusinessUnity();
        BusinessUnity businessUnity3 = new BusinessUnity();
        BusinessUnity businessUnity4 = new BusinessUnity();
        if (listaBusinessUnity.isEmpty()) {
            businessUnity1.setSiglaBusinessUnity("WMO-I");
            businessUnity1.setNomeBusinessUnity("WEG Motores Industrial");
            businessUnity1 = businessUnityRepository.save(businessUnity1);


            businessUnity2.setSiglaBusinessUnity("WMO-C");
            businessUnity2.setNomeBusinessUnity("WEG Motores Comercial");
            businessUnity2 = businessUnityRepository.save(businessUnity2);

            businessUnity3.setSiglaBusinessUnity("WEN");
            businessUnity3.setNomeBusinessUnity("WEG Energia");
            businessUnity3 = businessUnityRepository.save(businessUnity3);


            businessUnity4.setSiglaBusinessUnity("WAU");
            businessUnity4.setNomeBusinessUnity("WEG Automação");
            businessUnity4 = businessUnityRepository.save(businessUnity4);

            BusinessUnity businessUnity5 = new BusinessUnity();
            businessUnity5.setSiglaBusinessUnity("WDS");
            businessUnity5.setNomeBusinessUnity("WEG Digital e Sistemas");
            businessUnity5 = businessUnityRepository.save(businessUnity5);

            BusinessUnity businessUnity6 = new BusinessUnity();
            businessUnity6.setSiglaBusinessUnity("WDC");
            businessUnity6.setNomeBusinessUnity("WEG Drives e Controls");
            businessUnity6 = businessUnityRepository.save(businessUnity6);

            BusinessUnity businessUnity7 = new BusinessUnity();
            businessUnity7.setSiglaBusinessUnity("WTI");
            businessUnity7.setNomeBusinessUnity("WEG Tintas");
            businessUnity7 = businessUnityRepository.save(businessUnity7);

            BusinessUnity businessUnity8 = new BusinessUnity();
            businessUnity8.setSiglaBusinessUnity("WTD");
            businessUnity8.setNomeBusinessUnity("WEG Transmissão e Distribuição");
            businessUnity8 = businessUnityRepository.save(businessUnity8);
        }

        Usuario solicitante = new Usuario();
        Usuario analista = new Usuario();
        Usuario gerente = new Usuario();
        Usuario gestorTI = new Usuario();

        if (listaUsuarios.isEmpty()) {
            solicitante.setNumeroCadastroUsuario(72130);
            solicitante.setNomeUsuario("Michael Jordan");
            solicitante.setDepartamentoUsuario(businessUnity1);
            solicitante.setBusinessUnity("WEG Equipamentos Elétricos S/A");
            solicitante.setCargoUsuario(Cargo.SOLICITANTE);
            solicitante.setEmailUsuario("mj.bulls@gamil.com");
            solicitante.setSenhaUsuario("123");
            solicitante = usuarioRepository.save(solicitante);

            analista.setNumeroCadastroUsuario(72131);
            analista.setNomeUsuario("Kobe Bryant");
            analista.setDepartamentoUsuario(businessUnity2);
            analista.setBusinessUnity("WEG Equipamentos Elétricos S/A");
            analista.setCargoUsuario(Cargo.ANALISTA);
            analista.setEmailUsuario("kb.lakers@gamil.com");
            analista.setSenhaUsuario("123");
            analista = usuarioRepository.save(analista);

            gerente.setNumeroCadastroUsuario(72132);
            gerente.setNomeUsuario("Lebron James");
            gerente.setDepartamentoUsuario(businessUnity3);
            gerente.setBusinessUnity("WEG Equipamentos Elétricos S/A");
            gerente.setCargoUsuario(Cargo.GERENTE);
            gerente.setEmailUsuario("lbj.lakers@gamil.com");
            gerente.setSenhaUsuario("123");
            gerente = usuarioRepository.save(gerente);

            gestorTI.setNumeroCadastroUsuario(72133);
            gestorTI.setNomeUsuario("Magic Johnson");
            gestorTI.setDepartamentoUsuario(businessUnity4);
            gestorTI.setBusinessUnity("WEG Equipamentos Elétricos S/A");
            gestorTI.setCargoUsuario(Cargo.GESTOR_TI);
            gestorTI.setEmailUsuario("mgj.lakers@gamil.com");
            gestorTI.setSenhaUsuario("123");
            gestorTI = usuarioRepository.save(gestorTI);
        }

        if(listaForum.isEmpty()){
            Forum forum = new Forum();
            forum.setAnalistaResponsavelForum(analista);
            forum.setComissaoForum(comissao1);
            forum.setUsuariosForum(Arrays.asList(solicitante, analista, gerente, gestorTI));
            forumRepository.save(forum);
        }


        List<Beneficio> listaBeneficiosDemanda = new ArrayList<>();
        if (listaBeneficios.isEmpty()) {
            Beneficio beneficioR = new Beneficio();
            beneficioR.setIdFront(1);
            beneficioR.setMoedaBeneficio(Moeda.EURO);
            beneficioR.setValorBeneficio(2000.0);
            beneficioR.setTipoBeneficio(TipoBeneficio.REAL);
            beneficioR.setMemoriaCalculoBeneficio("doidera");
            beneficioR.setMemoriaCalculoBeneficioHTML("<html> <p> doidera </p> </html>");

            Beneficio beneficioP = new Beneficio();
            beneficioP.setIdFront(2);
            beneficioP.setMoedaBeneficio(Moeda.EURO);
            beneficioP.setValorBeneficio(2500.0);
            beneficioP.setTipoBeneficio(TipoBeneficio.POTENCIAL);
            beneficioP.setMemoriaCalculoBeneficio("doidera");
            beneficioP.setMemoriaCalculoBeneficioHTML("<html> <p> doidera </p> </html>");

            Beneficio beneficioQ = new Beneficio();
            beneficioQ.setIdFront(3);
            beneficioQ.setTipoBeneficio(TipoBeneficio.QUALITATIVO);
            beneficioQ.setMemoriaCalculoBeneficio("doidera");
            beneficioQ.setMemoriaCalculoBeneficioHTML("<html> <p> doidera </p> </html>");

            listaBeneficiosDemanda.add(beneficioR);
            listaBeneficiosDemanda.add(beneficioP);
            listaBeneficiosDemanda.add(beneficioQ);
        }

        if (listaDemanda.isEmpty()) {
            Demanda demanda = new Demanda();
            demanda.setTituloDemanda("Sala de brinquedos");
            demanda.setSituacaoAtualDemanda("Brinquedo é bom!");
            demanda.setDescricaoQualitativoDemanda("sim");
            demanda.setFrequenciaUsoDemanda("Muito usada");
            demanda.setSolicitanteDemanda(solicitante);
            demanda.setAnalistaResponsavelDemanda(analista);
            demanda.setGerenteDaAreaDemanda(gerente);
            demanda.setGestorResponsavelDemanda(gestorTI);
            demanda.setBeneficiosDemanda(listaBeneficios);
            demanda.setStatusDemanda(StatusDemanda.RASCUNHO);
            demanda = demandaRepository.save(demanda);

            demandaController.atualizarStatusDemanda(demanda.getIdDemanda(), StatusDemanda.ABERTA);
        }
    }
}
