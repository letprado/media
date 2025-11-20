package com.project.media.init;

import com.project.media.entity.Sintoma;
import com.project.media.entity.Sugestao;
import com.project.media.repository.SintomaRepository;
import com.project.media.repository.SugestaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@Profile({"dev", "default"})
public class DataInitializerExpandido implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializerExpandido.class);

    @Autowired
    private SintomaRepository sintomaRepository;

    @Autowired
    private SugestaoRepository sugestaoRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (verificarSeExistemDados()) {
                logger.info("Dados já existem no banco, pulando inicialização.");
                return;
            }
            
            logger.info("Inicializando dados expandidos de exemplo...");
            initializeData();
            logger.info("Dados expandidos inicializados com sucesso!");
            
        } catch (Exception e) {
            logger.warn("Erro ao inicializar dados (possivelmente tabelas não existem): {}", e.getMessage());
            logger.info("Execute o script oracle-setup.sql primeiro!");
        }
    }

    @Transactional(timeout = 2, readOnly = true)
    public boolean verificarSeExistemDados() {
        return sintomaRepository.count() > 0;
    }

    private void initializeData() {
        List<Sintoma> sintomas = salvarSintomas();
        List<Sugestao> sugestoes = salvarSugestoes(sintomas);
        associarEsalvarRelacionamentos(sintomas, sugestoes);
    }

    @Transactional(timeout = 10)
    public List<Sintoma> salvarSintomas() {
        List<Sintoma> sintomas = createSintomas();
        return sintomaRepository.saveAll(sintomas);
    }

    @Transactional(timeout = 10)
    public List<Sugestao> salvarSugestoes(List<Sintoma> sintomas) {
        List<Sugestao> sugestoes = createSugestoes(sintomas);
        return sugestaoRepository.saveAll(sugestoes);
    }

    @Transactional(timeout = 15)
    public void associarEsalvarRelacionamentos(List<Sintoma> sintomas, List<Sugestao> sugestoes) {
        associarSintomasSupestoes(sintomas, sugestoes);
        sugestaoRepository.saveAll(sugestoes);
    }

    private List<Sintoma> createSintomas() {
        return Arrays.asList(
            // Sintomas Respiratórios (12)
            new Sintoma("febre", "Temperatura corporal elevada acima de 37.5°C", "respiratorio"),
            new Sintoma("tosse", "Tosse seca ou com secreção", "respiratorio"),
            new Sintoma("tosse seca", "Tosse sem produção de catarro", "respiratorio"),
            new Sintoma("tosse com catarro", "Tosse acompanhada de secreção", "respiratorio"),
            new Sintoma("dor no peito", "Dor ou desconforto na região torácica", "respiratorio"),
            new Sintoma("falta de ar", "Dificuldade para respirar ou respiração ofegante", "respiratorio"),
            new Sintoma("dor de garganta", "Dor ou irritação na garganta", "respiratorio"),
            new Sintoma("coriza", "Secreção nasal abundante", "respiratorio"),
            new Sintoma("espirros", "Espirros frequentes", "respiratorio"),
            new Sintoma("congestão nasal", "Nariz entupido ou bloqueado", "respiratorio"),
            new Sintoma("rouquidão", "Voz rouca ou alterada", "respiratorio"),
            new Sintoma("dificuldade para engolir", "Dor ou dificuldade ao engolir", "respiratorio"),

            // Sintomas Gastrointestinais (15)
            new Sintoma("dor abdominal", "Dor na região do abdômen", "gastrointestinal"),
            new Sintoma("dor de estômago", "Dor na região gástrica", "gastrointestinal"),
            new Sintoma("náusea", "Sensação de enjoo", "gastrointestinal"),
            new Sintoma("vômito", "Eliminação do conteúdo estomacal pela boca", "gastrointestinal"),
            new Sintoma("vômitos frequentes", "Vômitos recorrentes", "gastrointestinal"),
            new Sintoma("diarreia", "Evacuações frequentes e líquidas", "gastrointestinal"),
            new Sintoma("prisão de ventre", "Dificuldade para evacuar", "gastrointestinal"),
            new Sintoma("constipação", "Falta de evacuações regulares", "gastrointestinal"),
            new Sintoma("azia", "Queimação no estômago ou esôfago", "gastrointestinal"),
            new Sintoma("refluxo", "Retorno do conteúdo gástrico", "gastrointestinal"),
            new Sintoma("inchaço abdominal", "Distensão ou aumento do abdômen", "gastrointestinal"),
            new Sintoma("flatulência", "Excesso de gases intestinais", "gastrointestinal"),
            new Sintoma("perda de apetite", "Falta de desejo de comer", "gastrointestinal"),
            new Sintoma("sede excessiva", "Necessidade constante de beber água", "gastrointestinal"),
            new Sintoma("sangue nas fezes", "Presença de sangue nas evacuações", "gastrointestinal"),

            // Sintomas Neurológicos (12)
            new Sintoma("dor de cabeça", "Cefaleia ou enxaqueca", "neurologico"),
            new Sintoma("enxaqueca", "Dor de cabeça intensa e pulsante", "neurologico"),
            new Sintoma("tontura", "Sensação de desequilíbrio ou vertigem", "neurologico"),
            new Sintoma("vertigem", "Sensação de rotação ou movimento", "neurologico"),
            new Sintoma("confusão mental", "Dificuldade de concentração ou pensamento confuso", "neurologico"),
            new Sintoma("dormência", "Perda de sensibilidade em alguma parte do corpo", "neurologico"),
            new Sintoma("formigamento", "Sensação de formigamento ou picadas", "neurologico"),
            new Sintoma("convulsão", "Contrações involuntárias dos músculos", "neurologico"),
            new Sintoma("desmaio", "Perda temporária de consciência", "neurologico"),
            new Sintoma("perda de memória", "Dificuldade para lembrar informações", "neurologico"),
            new Sintoma("visão embaçada", "Visão turva ou pouco clara", "neurologico"),
            new Sintoma("dificuldade para falar", "Problemas na fala ou articulação", "neurologico"),

            // Sintomas Cardiovasculares (10)
            new Sintoma("palpitação", "Batimentos cardíacos irregulares ou acelerados", "cardiovascular"),
            new Sintoma("taquicardia", "Batimentos cardíacos muito rápidos", "cardiovascular"),
            new Sintoma("dor no braço esquerdo", "Dor que irradia do peito para o braço", "cardiovascular"),
            new Sintoma("pressão alta", "Hipertensão arterial", "cardiovascular"),
            new Sintoma("pressão baixa", "Hipotensão arterial", "cardiovascular"),
            new Sintoma("inchaço nas pernas", "Edema ou inchaço nos membros inferiores", "cardiovascular"),
            new Sintoma("cansaço ao fazer esforço", "Fadiga durante atividades físicas", "cardiovascular"),
            new Sintoma("suor frio", "Sudorese com sensação de frio", "cardiovascular"),
            new Sintoma("desconforto no peito", "Sensação de aperto ou compressão no peito", "cardiovascular"),
            new Sintoma("dor no maxilar", "Dor que irradia para a mandíbula", "cardiovascular"),

            // Sintomas Musculoesqueléticos (10)
            new Sintoma("dor nas costas", "Dor na região dorsal ou lombar", "musculoesqueletico"),
            new Sintoma("dor lombar", "Dor na parte inferior das costas", "musculoesqueletico"),
            new Sintoma("dor nas articulações", "Dor nas juntas ou articulações", "musculoesqueletico"),
            new Sintoma("rigidez muscular", "Músculos rígidos ou tensos", "musculoesqueletico"),
            new Sintoma("dor no pescoço", "Dor na região cervical", "musculoesqueletico"),
            new Sintoma("limitação de movimento", "Dificuldade para mover uma parte do corpo", "musculoesqueletico"),
            new Sintoma("fraqueza muscular", "Perda de força muscular", "musculoesqueletico"),
            new Sintoma("cãibra", "Contração muscular involuntária e dolorosa", "musculoesqueletico"),
            new Sintoma("dor nos ombros", "Dor na região dos ombros", "musculoesqueletico"),
            new Sintoma("dor nos joelhos", "Dor na região dos joelhos", "musculoesqueletico"),

            // Sintomas Dermatológicos (8)
            new Sintoma("erupção cutânea", "Manchas ou lesões na pele", "dermatologico"),
            new Sintoma("coceira", "Prurido ou sensação de coceira", "dermatologico"),
            new Sintoma("vermelhidão na pele", "Pele avermelhada", "dermatologico"),
            new Sintoma("bolhas", "Lesões com líquido na pele", "dermatologico"),
            new Sintoma("descamação", "Pele descamando ou rachada", "dermatologico"),
            new Sintoma("ferida que não cicatriza", "Lesão que não melhora", "dermatologico"),
            new Sintoma("manchas na pele", "Alterações na coloração da pele", "dermatologico"),
            new Sintoma("urticária", "Lesões vermelhas e inchadas na pele", "dermatologico"),

            // Sintomas Gerais (12)
            new Sintoma("fadiga", "Cansaço extremo ou fraqueza", "geral"),
            new Sintoma("cansaço excessivo", "Fadiga intensa e persistente", "geral"),
            new Sintoma("perda de peso", "Diminuição não intencional do peso corporal", "geral"),
            new Sintoma("ganho de peso", "Aumento de peso não intencional", "geral"),
            new Sintoma("sudorese", "Suor excessivo", "geral"),
            new Sintoma("sudorese noturna", "Suor durante o sono", "geral"),
            new Sintoma("calafrios", "Sensação de frio com tremores", "geral"),
            new Sintoma("inchaço", "Edema ou aumento de volume em alguma região", "geral"),
            new Sintoma("gânglios inchados", "Linfonodos aumentados", "geral"),
            new Sintoma("mal estar geral", "Sensação geral de desconforto", "geral"),
            new Sintoma("insônia", "Dificuldade para dormir", "geral"),
            new Sintoma("sonolência excessiva", "Necessidade excessiva de dormir", "geral"),

            // Sintomas Urológicos (6)
            new Sintoma("ardor ao urinar", "Dor ou queimação ao urinar", "urologico"),
            new Sintoma("vontade frequente de urinar", "Necessidade constante de urinar", "urologico"),
            new Sintoma("sangue na urina", "Presença de sangue na urina", "urologico"),
            new Sintoma("dificuldade para urinar", "Problemas ao esvaziar a bexiga", "urologico"),
            new Sintoma("incontinência urinária", "Perda involuntária de urina", "urologico"),
            new Sintoma("dor lombar com ardência", "Dor nas costas com ardor ao urinar", "urologico"),

            // Sintomas Oftalmológicos (5)
            new Sintoma("dor nos olhos", "Dor ou desconforto nos olhos", "oftalmologico"),
            new Sintoma("olhos vermelhos", "Vermelhidão nos olhos", "oftalmologico"),
            new Sintoma("sensibilidade à luz", "Fotofobia ou dificuldade com luz", "oftalmologico"),
            new Sintoma("lacrimejamento", "Olhos lacrimejando excessivamente", "oftalmologico"),
            new Sintoma("visão dupla", "Percepção de duas imagens", "oftalmologico"),

            // Sintomas Otorrinolaringológicos (5)
            new Sintoma("dor de ouvido", "Dor ou desconforto nos ouvidos", "otorrino"),
            new Sintoma("zumbido", "Ruído constante nos ouvidos", "otorrino"),
            new Sintoma("perda auditiva", "Dificuldade para ouvir", "otorrino"),
            new Sintoma("tontura com zumbido", "Vertigem acompanhada de zumbido", "otorrino"),
            new Sintoma("secreção no ouvido", "Saída de líquido pelos ouvidos", "otorrino")
        );
    }

    private List<Sugestao> createSugestoes(List<Sintoma> sintomas) {
        // 1. Emergência Cardíaca
        Sugestao s1 = new Sugestao(
            "EMERGÊNCIA: Atendimento cardíaco imediato",
            "Os sintomas informados podem indicar um evento cardiovascular grave como infarto do miocárdio. " +
            "É fundamental procurar atendimento médico de URGÊNCIA IMEDIATA. Cada minuto conta nestes casos.",
            "URGENTE",
            5
        );
        s1.setRecomendacoes(
            "1. Chame SAMU (192) IMEDIATAMENTE\n" +
            "2. Não dirija - peça para alguém levar ou aguarde o SAMU\n" +
            "3. Mantenha-se em repouso absoluto\n" +
            "4. Não ingira alimentos ou bebidas\n" +
            "5. Tenha em mãos: RG, CPF e cartão do plano de saúde"
        );

        // 2. Sintomas Respiratórios Graves
        Sugestao s2 = new Sugestao(
            "URGENTE: Dificuldade respiratória",
            "A combinação de sintomas respiratórios com falta de ar pode indicar infecção grave, pneumonia, " +
            "asma severa ou outras condições respiratórias que necessitam avaliação médica urgente.",
            "URGENTE",
            5
        );
        s2.setRecomendacoes(
            "1. Procure o pronto-socorro mais próximo\n" +
            "2. Se a falta de ar piorar, chame SAMU (192)\n" +
            "3. Evite esforços físicos\n" +
            "4. Mantenha-se sentado em posição confortável\n" +
            "5. Leve documento de identidade e cartão do plano"
        );

        // 3. Infecção Respiratória
        Sugestao s3 = new Sugestao(
            "Consulta médica para infecção respiratória",
            "Os sintomas indicam possível infecção respiratória (gripe, resfriado, sinusite, bronquite). " +
            "Uma avaliação médica é recomendada para diagnóstico preciso e tratamento adequado.",
            "CONSULTA",
            3
        );
        s3.setRecomendacoes(
            "1. Procure unidade básica de saúde ou médico em até 48 horas\n" +
            "2. Mantenha repouso e hidratação adequada\n" +
            "3. Use máscara para evitar transmitir\n" +
            "4. Evite contato próximo com outras pessoas\n" +
            "5. Monitore temperatura corporal regularmente"
        );

        // 4. Gastrointestinal Agudo
        Sugestao s4 = new Sugestao(
            "Avaliação urgente de sintomas gastrointestinais",
            "Sintomas gastrointestinais intensos com vômitos ou diarreia podem levar à desidratação grave. " +
            "Avaliação médica é necessária para determinar a causa e tratamento adequado.",
            "URGENTE",
            4
        );
        s4.setRecomendacoes(
            "1. Procure atendimento médico nas próximas horas\n" +
            "2. Mantenha hidratação: beba água, soro caseiro ou isotônicos\n" +
            "3. Evite alimentos sólidos até melhorar\n" +
            "4. Se houver sangue nas fezes ou vômitos, vá ao pronto-socorro\n" +
            "5. Monitore sinais de desidratação: boca seca, urina escassa"
        );

        // 5. Gastrointestinal Leve
        Sugestao s5 = new Sugestao(
            "Cuidados para sintomas gastrointestinais leves",
            "Sintomas gastrointestinais leves podem ser tratados com medidas de autocuidado. " +
            "Monitore a evolução e procure ajuda médica se persistirem ou piorarem.",
            "OBSERVACAO",
            2
        );
        s5.setRecomendacoes(
            "1. Faça dieta leve: banana, arroz, torrada, maçã (BRAT)\n" +
            "2. Mantenha hidratação adequada\n" +
            "3. Evite alimentos gordurosos, condimentados e lácteos\n" +
            "4. Se persistir por mais de 2-3 dias, procure médico\n" +
            "5. Evite medicamentos sem orientação médica"
        );

        // 6. Emergência Neurológica
        Sugestao s6 = new Sugestao(
            "EMERGÊNCIA: Avaliação neurológica urgente",
            "Sintomas neurológicos como convulsão, desmaio, confusão mental severa ou perda de sensibilidade " +
            "podem indicar condições neurológicas graves que necessitam avaliação médica imediata.",
            "URGENTE",
            5
        );
        s6.setRecomendacoes(
            "1. Chame SAMU (192) IMEDIATAMENTE\n" +
            "2. Se houver convulsão: deite a pessoa de lado, proteja a cabeça\n" +
            "3. NÃO coloque nada na boca durante convulsão\n" +
            "4. Mantenha a pessoa calma e em ambiente seguro\n" +
            "5. Anote quando os sintomas começaram"
        );

        // 7. Neurológico - Consulta
        Sugestao s7 = new Sugestao(
            "Consulta neurológica necessária",
            "Sintomas neurológicos como dor de cabeça intensa, tontura, dormência ou alterações visuais " +
            "necessitam de avaliação médica especializada para diagnóstico adequado.",
            "CONSULTA",
            4
        );
        s7.setRecomendacoes(
            "1. Procure atendimento médico nas próximas horas\n" +
            "2. Anote quando os sintomas começaram e características\n" +
            "3. Liste todos os medicamentos que está tomando\n" +
            "4. Evite dirigir se houver tontura ou alterações visuais\n" +
            "5. Informe sobre histórico de problemas neurológicos"
        );

        // 8. Cefaleia/Migrânea
        Sugestao s8 = new Sugestao(
            "Consulta para dor de cabeça persistente",
            "Dor de cabeça frequente ou intensa deve ser avaliada por médico para identificar a causa " +
            "e determinar o melhor tratamento. Enxaquecas podem ser incapacitantes se não tratadas adequadamente.",
            "CONSULTA",
            3
        );
        s8.setRecomendacoes(
            "1. Procure médico em até uma semana\n" +
            "2. Mantenha um diário dos episódios: quando ocorrem, duração, intensidade\n" +
            "3. Identifique possíveis gatilhos: alimentos, estresse, sono\n" +
            "4. Evite automedicação excessiva\n" +
            "5. Se dor for súbita e muito intensa, vá ao pronto-socorro"
        );

        // 9. Sintomas Gerais
        Sugestao s9 = new Sugestao(
            "Avaliação médica para sintomas gerais persistentes",
            "Sintomas como fadiga excessiva, perda de peso não intencional, febre persistente ou sudorese " +
            "podem indicar várias condições. Uma avaliação médica completa é recomendada.",
            "CONSULTA",
            3
        );
        s9.setRecomendacoes(
            "1. Procure atendimento médico em até uma semana\n" +
            "2. Mantenha registro dos sintomas: quando começaram, frequência, intensidade\n" +
            "3. Verifique se há padrões: horários, atividades relacionadas\n" +
            "4. Informe sobre histórico familiar de doenças\n" +
            "5. Mantenha alimentação balanceada e sono adequado"
        );

        // 10. Autocuidado - Sintomas Leves
        Sugestao s10 = new Sugestao(
            "Autocuidado para sintomas leves",
            "Os sintomas informados são comuns e geralmente podem ser tratados com medidas simples de autocuidado. " +
            "Monitore a evolução e procure ajuda médica se piorarem ou persistirem.",
            "AUTOCUIDADO",
            1
        );
        s10.setRecomendacoes(
            "1. Mantenha repouso adequado\n" +
            "2. Beba bastante líquido (água, sucos naturais)\n" +
            "3. Alimente-se de forma balanceada\n" +
            "4. Use medicamentos de venda livre com moderação e seguindo bula\n" +
            "5. Se sintomas persistirem por mais de 3-5 dias ou piorarem, procure médico"
        );

        // 11. Emergência - Múltiplos Sintomas Graves
        Sugestao s11 = new Sugestao(
            "EMERGÊNCIA: Múltiplos sintomas graves",
            "A combinação de sintomas graves indica possível emergência médica. " +
            "Não hesite em procurar atendimento imediato no pronto-socorro ou chamar o SAMU.",
            "URGENTE",
            5
        );
        s11.setRecomendacoes(
            "1. Chame SAMU (192) ou vá imediatamente ao pronto-socorro\n" +
            "2. Não dirija - peça para alguém levar ou use ambulância\n" +
            "3. Não tome medicamentos por conta própria\n" +
            "4. Mantenha-se acompanhado\n" +
            "5. Leve: RG, CPF, cartão do plano de saúde, lista de medicamentos"
        );

        // 12. Infecção Urinária
        Sugestao s12 = new Sugestao(
            "Consulta para possível infecção urinária",
            "Os sintomas indicam possível infecção do trato urinário. É importante procurar atendimento médico " +
            "para diagnóstico e tratamento com antibióticos, se necessário.",
            "CONSULTA",
            3
        );
        s12.setRecomendacoes(
            "1. Procure médico em até 48 horas\n" +
            "2. Aumente ingestão de água para ajudar a eliminar bactérias\n" +
            "3. Evite cafeína, álcool e alimentos muito condimentados\n" +
            "4. Se houver febre ou dor lombar, procure pronto-socorro\n" +
            "5. Mulheres: esvazie bexiga após relações sexuais"
        );

        // 13. Alergia/Dermatite
        Sugestao s13 = new Sugestao(
            "Avaliação dermatológica",
            "Erupções cutâneas, coceira ou alterações na pele podem ter várias causas. " +
            "Uma avaliação médica ajuda a identificar se é alergia, infecção ou outra condição.",
            "CONSULTA",
            2
        );
        s13.setRecomendacoes(
            "1. Procure médico ou dermatologista em até uma semana\n" +
            "2. Evite coçar para prevenir infecção\n" +
            "3. Use roupas de algodão e evite produtos irritantes\n" +
            "4. Aplique compressa fria para aliviar coceira\n" +
            "5. Se houver dificuldade respiratória, vá ao pronto-socorro (pode ser anafilaxia)"
        );

        // 14. Problemas Musculoesqueléticos
        Sugestao s14 = new Sugestao(
            "Consulta para dores musculoesqueléticas",
            "Dores nas costas, articulações ou músculos podem ter várias causas. " +
            "Avaliação médica ajuda a determinar se é lesão, inflamação ou outra condição.",
            "CONSULTA",
            2
        );
        s14.setRecomendacoes(
            "1. Procure médico em até uma semana\n" +
            "2. Aplique compressa fria nas primeiras 48 horas, depois calor\n" +
            "3. Evite movimentos que aumentem a dor\n" +
            "4. Mantenha postura adequada\n" +
            "5. Se dor for muito intensa ou após trauma, procure pronto-socorro"
        );

        // 15. Problemas Oftalmológicos
        Sugestao s15 = new Sugestao(
            "Consulta oftalmológica urgente",
            "Alterações visuais, dor nos olhos ou vermelhidão podem indicar problemas oculares que " +
            "necessitam avaliação oftalmológica. Algumas condições oculares podem ser graves se não tratadas.",
            "CONSULTA",
            4
        );
        s15.setRecomendacoes(
            "1. Procure oftalmologista nas próximas horas\n" +
            "2. Não use colírios sem prescrição médica\n" +
            "3. Evite coçar os olhos\n" +
            "4. Use óculos escuros se houver sensibilidade à luz\n" +
            "5. Se houver perda súbita de visão, vá ao pronto-socorro imediatamente"
        );

        return Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15);
    }

    private void associarSintomasSupestoes(List<Sintoma> sintomas, List<Sugestao> sugestoes) {
        // 1. Emergência Cardíaca
        sugestoes.get(0).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor no peito"),
            findSintomaByNome(sintomas, "dor no braço esquerdo"),
            findSintomaByNome(sintomas, "palpitação"),
            findSintomaByNome(sintomas, "taquicardia"),
            findSintomaByNome(sintomas, "falta de ar"),
            findSintomaByNome(sintomas, "suor frio"),
            findSintomaByNome(sintomas, "desconforto no peito"),
            findSintomaByNome(sintomas, "dor no maxilar")
        )));

        // 2. Sintomas Respiratórios Graves
        sugestoes.get(1).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "falta de ar"),
            findSintomaByNome(sintomas, "dor no peito"),
            findSintomaByNome(sintomas, "febre"),
            findSintomaByNome(sintomas, "tosse"),
            findSintomaByNome(sintomas, "tosse com catarro")
        )));

        // 3. Infecção Respiratória
        sugestoes.get(2).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "febre"),
            findSintomaByNome(sintomas, "tosse"),
            findSintomaByNome(sintomas, "tosse seca"),
            findSintomaByNome(sintomas, "tosse com catarro"),
            findSintomaByNome(sintomas, "dor de garganta"),
            findSintomaByNome(sintomas, "coriza"),
            findSintomaByNome(sintomas, "congestão nasal"),
            findSintomaByNome(sintomas, "espirros")
        )));

        // 4. Gastrointestinal Agudo
        sugestoes.get(3).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor abdominal"),
            findSintomaByNome(sintomas, "náusea"),
            findSintomaByNome(sintomas, "vômito"),
            findSintomaByNome(sintomas, "vômitos frequentes"),
            findSintomaByNome(sintomas, "diarreia"),
            findSintomaByNome(sintomas, "sangue nas fezes")
        )));

        // 5. Gastrointestinal Leve
        sugestoes.get(4).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor de estômago"),
            findSintomaByNome(sintomas, "azia"),
            findSintomaByNome(sintomas, "refluxo"),
            findSintomaByNome(sintomas, "inchaço abdominal"),
            findSintomaByNome(sintomas, "flatulência")
        )));

        // 6. Emergência Neurológica
        sugestoes.get(5).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "convulsão"),
            findSintomaByNome(sintomas, "desmaio"),
            findSintomaByNome(sintomas, "confusão mental"),
            findSintomaByNome(sintomas, "dormência"),
            findSintomaByNome(sintomas, "perda de memória"),
            findSintomaByNome(sintomas, "dificuldade para falar")
        )));

        // 7. Neurológico - Consulta
        sugestoes.get(6).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor de cabeça"),
            findSintomaByNome(sintomas, "enxaqueca"),
            findSintomaByNome(sintomas, "tontura"),
            findSintomaByNome(sintomas, "vertigem"),
            findSintomaByNome(sintomas, "visão embaçada"),
            findSintomaByNome(sintomas, "formigamento")
        )));

        // 8. Cefaleia/Migrânea
        sugestoes.get(7).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor de cabeça"),
            findSintomaByNome(sintomas, "enxaqueca"),
            findSintomaByNome(sintomas, "sensibilidade à luz")
        )));

        // 9. Sintomas Gerais
        sugestoes.get(8).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "fadiga"),
            findSintomaByNome(sintomas, "cansaço excessivo"),
            findSintomaByNome(sintomas, "perda de peso"),
            findSintomaByNome(sintomas, "sudorese"),
            findSintomaByNome(sintomas, "sudorese noturna"),
            findSintomaByNome(sintomas, "febre"),
            findSintomaByNome(sintomas, "gânglios inchados")
        )));

        // 10. Autocuidado
        sugestoes.get(9).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor de garganta"),
            findSintomaByNome(sintomas, "coriza"),
            findSintomaByNome(sintomas, "espirros"),
            findSintomaByNome(sintomas, "mal estar geral")
        )));

        // 11. Emergência - Múltiplos
        sugestoes.get(10).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "febre"),
            findSintomaByNome(sintomas, "convulsão"),
            findSintomaByNome(sintomas, "confusão mental"),
            findSintomaByNome(sintomas, "dor no peito"),
            findSintomaByNome(sintomas, "falta de ar")
        )));

        // 12. Infecção Urinária
        sugestoes.get(11).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "ardor ao urinar"),
            findSintomaByNome(sintomas, "vontade frequente de urinar"),
            findSintomaByNome(sintomas, "sangue na urina"),
            findSintomaByNome(sintomas, "dor lombar com ardência")
        )));

        // 13. Alergia/Dermatite
        sugestoes.get(12).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "erupção cutânea"),
            findSintomaByNome(sintomas, "coceira"),
            findSintomaByNome(sintomas, "vermelhidão na pele"),
            findSintomaByNome(sintomas, "urticária")
        )));

        // 14. Problemas Musculoesqueléticos
        sugestoes.get(13).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor nas costas"),
            findSintomaByNome(sintomas, "dor lombar"),
            findSintomaByNome(sintomas, "dor nas articulações"),
            findSintomaByNome(sintomas, "dor no pescoço"),
            findSintomaByNome(sintomas, "rigidez muscular")
        )));

        // 15. Problemas Oftalmológicos
        sugestoes.get(14).setSintomas(new HashSet<>(Arrays.asList(
            findSintomaByNome(sintomas, "dor nos olhos"),
            findSintomaByNome(sintomas, "olhos vermelhos"),
            findSintomaByNome(sintomas, "sensibilidade à luz"),
            findSintomaByNome(sintomas, "visão embaçada"),
            findSintomaByNome(sintomas, "visão dupla")
        )));
    }

    private Sintoma findSintomaByNome(List<Sintoma> sintomas, String nome) {
        return sintomas.stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sintoma não encontrado: " + nome));
    }
}


