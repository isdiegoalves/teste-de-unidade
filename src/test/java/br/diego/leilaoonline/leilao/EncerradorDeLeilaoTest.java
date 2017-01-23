package br.diego.leilaoonline.leilao;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import br.diego.leilaoonline.infra.email.Carteiro;
import br.diego.leilaoonline.leilao.builder.CriadorDeLeilao;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.leilao.repository.LeilaoRepositorio;
import br.diego.leilaoonline.leilao.service.EncerradorDeLeilao;

public class EncerradorDeLeilaoTest {

	private CurrencyUnit real;
	private EncerradorDeLeilao encerrador;
	private LeilaoRepositorio repositorioLeilao;
	private Carteiro carteiro;

	@Before
	public void setUp() {
		real = Monetary.getCurrency("BRL");
		carteiro = mock(Carteiro.class);
		repositorioLeilao = mock(LeilaoRepositorio.class);
		encerrador = new EncerradorDeLeilao(repositorioLeilao, carteiro);
	}

	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAtras() {
		LocalDate antiga = LocalDate.of(1999, 1, 20);

		Leilao leilao1 = CriadorDeLeilao.para("TV OLED",   antiga, real).create();
		Leilao leilao2 = CriadorDeLeilao.para("Geladeira", antiga, real).create();

		List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);
		when(repositorioLeilao.correntes()).thenReturn(leiloesAntigos);

		encerrador.encerra();

		assertThat(leilao1.isEncerrado(), is(true));
		assertThat(leilao2.isEncerrado(), is(true));
		assertThat(encerrador.getTotalEncerrados(), equalTo(2));
	}
	
	@Test
	public void naoDeveEncerrarLeiloesQueComecaramOntem() {
		LocalDate ontem = LocalDate.now().minusDays(1);

		Leilao leilao1 = CriadorDeLeilao.para("TV OLED",   ontem, real).create();
		Leilao leilao2 = CriadorDeLeilao.para("Geladeira", ontem, real).create();

		when(repositorioLeilao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		
		encerrador.encerra();

		assertThat(leilao1.isEncerrado(), is(false));
		assertThat(leilao2.isEncerrado(), is(false));
		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
		
		verify(repositorioLeilao, never()).atualiza(leilao1);
		verify(repositorioLeilao, never()).atualiza(leilao2);
	}
	
	@Test
	public void deveIgnorarLeiloesNaoCriados() {
		when(repositorioLeilao.correntes()).thenReturn(new ArrayList<>());
		
		encerrador.encerra();

		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
	}
	
	@Test
    public void deveAtualizarLeiloesEncerrados() {

        Leilao leilao1 = CriadorDeLeilao
        		.para("TV de plasma", LocalDate.of(1999, Month.JANUARY, 20), real)
        		.create();
        
        List<Leilao> leiloesAntigos = Arrays.asList(leilao1);
		when(repositorioLeilao.correntes()).thenReturn(leiloesAntigos);
        
        encerrador.encerra();

        // verificando que o metodo atualiza foi realmente invocado!
        verify(repositorioLeilao, times(1)).atualiza(leilao1);
    }
	
	@Test
    public void deveEnviarEmailAposPersistirLeilaoEncerrado() {

        Leilao leilao1 = CriadorDeLeilao
        		.para("TV de plasma", LocalDate.of(1999, Month.JANUARY, 20), real)
        		.create();

        List<Leilao> leiloesAntigos = Arrays.asList(leilao1);
		when(repositorioLeilao.correntes()).thenReturn(leiloesAntigos);

        encerrador.encerra();

        //testando pra ter certeza que e-mail eh enviado apos atualizar leilao
        InOrder inOrder = inOrder(repositorioLeilao, carteiro);
        inOrder.verify(repositorioLeilao, times(1)).atualiza(leilao1);
        inOrder.verify(carteiro, times(1)).enviar(leilao1);
    }
	
	@Test
	public void deveContinuarAExecucaoMesmoQuandoDaoFalha() {

		LocalDate antigo = LocalDate.of(1999, Month.JANUARY, 20);
		Leilao leilao1 = CriadorDeLeilao.para("TV OLED",   antigo, real).create();
		Leilao leilao2 = CriadorDeLeilao.para("Geladeira", antigo, real).create();
		
		when(repositorioLeilao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		doThrow(new RuntimeException()).when(repositorioLeilao).atualiza(leilao1);	

		encerrador.encerra();

		verify(repositorioLeilao, times(1)).atualiza(leilao2);
		verify(carteiro, times(1)).enviar(leilao2);
	}
	
	@Test
	public void deveContinuarAExecucaoMesmoQuandoCarteiroFalha() {

		LocalDate antigo = LocalDate.of(1999, Month.JANUARY, 20);
		Leilao leilao1 = CriadorDeLeilao.para("TV OLED",   antigo, real).create();
		Leilao leilao2 = CriadorDeLeilao.para("Geladeira", antigo, real).create();
		
		when(repositorioLeilao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		doThrow(new RuntimeException()).when(carteiro).enviar(leilao1);

		encerrador.encerra();

		verify(repositorioLeilao, times(1)).atualiza(leilao1);
		verify(repositorioLeilao, times(1)).atualiza(leilao2);
		verify(carteiro, times(1)).enviar(leilao2);
	}
	
	@Test
	public void naoDeveEnviarEmailQuandoTodasAsAtualizacoesNoDaoFalha() {

		LocalDate antigo = LocalDate.of(1999, Month.JANUARY, 20);
		Leilao leilao1 = CriadorDeLeilao.para("TV OLED",   antigo, real).create();
		Leilao leilao2 = CriadorDeLeilao.para("Geladeira", antigo, real).create();
		
		when(repositorioLeilao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		doThrow(new RuntimeException()).when(repositorioLeilao).atualiza(any(Leilao.class));

		encerrador.encerra();

		verify(carteiro, never()).enviar(any(Leilao.class));
	}
}
