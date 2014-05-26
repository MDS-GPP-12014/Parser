package teste.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.ParserExcel;

public class TestParserExcel {

	@Test
	public void testLerDadosPlanilha() {
		assertEquals(true, ParserExcel.lerDadosPlanilha("planilha_teste/conceito_enade.xlsx"));
		assertEquals(false, ParserExcel.lerDadosPlanilha("nomeErrado"));
	}

	@Test
	public void testGerarArquivoSQL() {
		assertEquals(true, ParserExcel.gerarArquivoSQL(ParserExcel.getNomeArquivoSQL()));
		assertEquals(false, ParserExcel.gerarArquivoSQL("_**\\NomeErrado//"));
	}
	
	@Test
	public void testGetNomeArquivoSQL() {
		assertEquals("dados.sql", ParserExcel.getNomeArquivoSQL());
	}
	
}
