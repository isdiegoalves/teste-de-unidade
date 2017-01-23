package br.diego.leilaoonline.leilao;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.leilao.model.Leilao;

public class LeilaoMatcher extends TypeSafeMatcher<Leilao> {

	private Lance lance;

	public LeilaoMatcher(Lance lance) {
		this.lance = lance;
	}
	
	public static Matcher<Leilao> temUmLance(Lance lance) {
        return new LeilaoMatcher(lance);
    }

	@Override
	public void describeTo(Description description) {
		 description.appendText("leilao com lance " + lance.getValor());
	}

	@Override
	protected boolean matchesSafely(Leilao item) {
		  return item.getLances().contains(lance);
	}

}
