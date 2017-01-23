package br.diego.leilaoonline.infra.relogio.control;

import static java.time.LocalDate.now;

import java.time.LocalDate;

import br.diego.leilaoonline.infra.relogio.boundary.Relogio;

public class RelogioDoSistema implements Relogio {

	@Override
	public LocalDate hoje() {
		
		return now();
	}

}
