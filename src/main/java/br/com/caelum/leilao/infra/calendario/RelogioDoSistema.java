package br.com.caelum.leilao.infra.calendario;

import static java.time.LocalDate.now;

import java.time.LocalDate;

public class RelogioDoSistema implements Relogio {

	@Override
	public LocalDate hoje() {
		
		return now();
	}

}
