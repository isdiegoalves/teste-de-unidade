package br.diego.leilaoonline.infra.relogio.internal;

import static java.time.LocalDate.now;

import java.time.LocalDate;

import br.diego.leilaoonline.infra.relogio.Relogio;

public class RelogioDoSistema implements Relogio {

	@Override
	public LocalDate hoje() {
		
		return now();
	}

}
