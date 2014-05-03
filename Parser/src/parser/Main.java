package parser;

public class Main {
	
	public static void main(String[] args) {
		
		System.out.println("Executando parser...\n");
		
		if(ParserExcel.lerDadosPlanilha("conceito_enade.xlsx") == true) {
			if(ParserExcel.gerarArquivoSQL(ParserExcel.getNomeArquivoSQL()) == true)
				System.out.println("Parser concluido.\n");
			else
				System.out.println("Erro no parser.\n");
		}
		else
			System.out.println("Erro no parser.\n");
		
		
	}

}
