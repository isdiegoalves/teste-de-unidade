package br.com.caelum.servico;

import static br.com.caelum.leilao.dominio.Usuario.usuario;
import static java.time.DayOfWeek.MONDAY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Avaliador;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.infra.calendario.Relogio;
import br.com.caelum.leilao.infra.dao.LeilaoRepositorio;
import br.com.caelum.leilao.infra.pagamento.PagamentoRepositorio;
import br.com.caelum.leilao.servico.GeradorDePagamento;

public class GeradorDePagamentoTest {
	
	private LeilaoRepositorio leiloes;
	private PagamentoRepositorio pagamentos;
	private CurrencyUnit real;
	private Usuario maria;
	private Usuario jose;
	private Avaliador avaliador;
	private Relogio relogio;

	@Before
	public void setUp() {
		avaliador = new Avaliador();
		maria = usuario("Maria Pereira");
		jose = usuario("José da Silva");
		real = Monetary.getCurrency("BRL");
		leiloes 	= mock(LeilaoRepositorio.class);
		pagamentos 	= mock(PagamentoRepositorio.class);
		relogio = mock(Relogio.class);
	}

    @Test
    public void deveGerarPagamentoParaUmLeilaoEncerrado() {
		Leilao leilao = CriadorDeLeilao
            .para("Playstation", LocalDate.of(1999, Month.JANUARY, 20), real)
            .lance(jose,  Money.of(2000, real))
            .lance(maria, Money.of(2500, real))
            .create();

        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));

		GeradorDePagamento gerador = new GeradorDePagamento(leiloes, pagamentos, avaliador);
        gerador.gerar();

        ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salvar(argumento.capture());
        Pagamento pagamentoGerado = argumento.getValue();
		assertThat(pagamentoGerado.getValor(), equalTo(avaliador.getMaiorLance()));
    }
    
    @Test
    public void deveEmpurrarParaOProximoDiaUtil() {

    	LocalDate data = LocalDate.of(1999, Month.JANUARY, 20);
		LocalDate sabado_23_JAN = data.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
		Leilao leilao = CriadorDeLeilao
    			.para("Playstation", sabado_23_JAN, real)
    			.lance(jose,  Money.of(2000, real))
                .lance(maria, Money.of(2500, real))
    			.create();

    	when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));
    	// ensinamos o mock a dizer que "hoje" é sabado!
    	when(relogio.hoje()).thenReturn(sabado_23_JAN);

    	GeradorDePagamento gerador = new GeradorDePagamento(leiloes, pagamentos, avaliador,  relogio);
    	gerador.gerar();

    	ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
    	verify(pagamentos).salvar(argumento.capture());
    	Pagamento pagamentoGerado = argumento.getValue();

    	assertThat(pagamentoGerado.getData().getDayOfWeek(), equalTo(MONDAY));
    	assertThat(pagamentoGerado.getData().getDayOfMonth(), equalTo(25));
    }
}