package parser;

//Classes Java necess�rias para Leitura de arquivos e Itera��o de Dados
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//Libs POI para ler .xlsx
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParserExcel {
	
	private static List<String>nomeCurso = new ArrayList<String>();//1
	private static List<Integer>codIES = new ArrayList<Integer>();//2
	private static List<String>nomeIES = new ArrayList<String>();//3
	private static List<String>tipoIES = new ArrayList<String>();//4
	private static List<String>orgAcade = new ArrayList<String>();//5
	private static List<String>municipio = new ArrayList<String>();//7
	private static List<String>uf = new ArrayList<String>();//9
	private static List<Integer>numEstudCurso = new ArrayList<Integer>();//11
	private static List<Integer>numEstudInsc = new ArrayList<Integer>();//12
	private static List<Float>conceitoEnade = new ArrayList<Float>();//17
	
	private static List<Integer>codIESAux = new ArrayList<Integer>();
	
	private static String nomeArquivoSQL = "dados.sql";
	
	public static String getNomeArquivoSQL() {
		return ParserExcel.nomeArquivoSQL;
	}
	
	public static boolean lerDadosPlanilha(String fileName) {
	
		try {
			
			FileInputStream fis = new FileInputStream(fileName);
			
			//Variavel Workbook para xlsx FileInputStream;
			Workbook workbook = new XSSFWorkbook(fis);
			
			//Numero de planilhas no arquivo .xlsx;
			int numPlanilhas = workbook.getNumberOfSheets();
			
			//Loop sobre todas as planilhas do arquivo .xlsx;
			for(int i = 0; i < numPlanilhas; i++) {
				
				Sheet planilha = workbook.getSheetAt(i);
				
				//Iterando sobre as linhas de uma planilha;
				Iterator<Row> linhaIterator = planilha.iterator();
				
				//Come�ando da linha 3 da planilha;
				linhaIterator.next();
				linhaIterator.next();
				
				while(linhaIterator.hasNext()) {
					
					Row linha = linhaIterator.next();
					
					int posicaoColuna = 0;//Auxiliar para saber num da coluna;
					
					//Iterando sobre as colunas de uma linha;
					Iterator<Cell>celulaIterator = linha.cellIterator();
					while(celulaIterator.hasNext()) {
						
						Cell celula = celulaIterator.next();
						
						//Posicao coluna == 1, 2, 3, 4, 5, 7, 9, 11,12, 17
						switch(posicaoColuna) {
						
						case 1:
							if(celula.getCellType() == Cell.CELL_TYPE_STRING)
								nomeCurso.add(celula.getStringCellValue());
							else
								nomeCurso.add(".");
							break;
						case 2:
							codIES.add((int)celula.getNumericCellValue());
							break;
						case 3:
							if(celula.getCellType() == Cell.CELL_TYPE_STRING)
								nomeIES.add(celula.getStringCellValue());
							else
								nomeIES.add(".");
							break;
						case 4:
							if(celula.getCellType() == Cell.CELL_TYPE_STRING)
								tipoIES.add(celula.getStringCellValue());
							else
								tipoIES.add(".");
							break;
						case 5:
							if(celula.getCellType() == Cell.CELL_TYPE_STRING)
								orgAcade.add(celula.getStringCellValue());
							else
								orgAcade.add(".");
							break;
						case 7:
							if(celula.getCellType() == Cell.CELL_TYPE_STRING)
								municipio.add(celula.getStringCellValue());
							else
								municipio.add(".");
							break;
						case 9:
							if(celula.getCellType() == Cell.CELL_TYPE_STRING)
								uf.add(celula.getStringCellValue());
							else
								uf.add(".");
							break;
						case 11:
							if(celula.getCellType() == Cell.CELL_TYPE_NUMERIC)
								numEstudCurso.add((int)celula.getNumericCellValue());
							else
								numEstudCurso.add(0);
							break;
						case 12:
							if(celula.getCellType() == Cell.CELL_TYPE_NUMERIC)
								numEstudInsc.add((int)celula.getNumericCellValue());
							else
								numEstudInsc.add(0);
							break;
						case 17:
							if(celula.getCellType() == Cell.CELL_TYPE_NUMERIC)
								conceitoEnade.add((float)celula.getNumericCellValue());
							else
								conceitoEnade.add((float)0);
							break;
						default:
							break;
						}//Fim do switch().
						posicaoColuna++;
					}//Fim do while().
					
				}//Fim do while().
				
			}//Fim do for();
			
			//Fechando o FileInputStream fis;
			fis.close();
			
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}//Fim do lerDadosPlanilha().
	
	public static boolean gerarArquivoSQL(String fileNameOut) {
		
		String createTableInstituicao = "CREATE TABLE instituicao(cod_ies INTEGER PRIMARY KEY NOT NULL, "+
										"org_academica TEXT, uf TEXT, nome_ies TEXT, tipo TEXT);"+"\n";
		
		String createTableCurso = "CREATE TABLE curso(instituicao_cod_ies INTEGER NOT NULL, "+
								  "num_estud_curso INTEGER, num_estud_insc INTEGER, "+
								  "nome_curso TEXT, municipio TEXT, conceito_enade REAL);"+"\n";
		
		String insertInstituicaoSQL = "INSERT INTO instituicao (cod_ies, org_academica, uf, "+
						   			"nome_ies, tipo) VALUES (%d, '%s', '%s', '%s', '%s');"+"\n";
		
		String insertCursoSQL = "INSERT INTO curso (instituicao_cod_ies, num_estud_curso, "+
							"num_estud_insc, nome_curso, municipio, conceito_enade) "+ 
							"VALUES (%d, %d, %d, '%s', '%s', %.3f);"+"\n"; 
		
		try {
			PrintWriter writer = new PrintWriter(fileNameOut);
			
			//Escrevendo CREATE TABLE de instituicao;
			writer.printf(createTableInstituicao);
			
			//Escrevendo CREATE TABLE de curso;
			writer.printf(createTableCurso);
			
			for(int i = 0; i < codIES.size(); i++) {
				
				//Escrevendo INSERT na tabela INSTITUICAO;
				int cod_ies = codIES.get(i);
				if(!codIESAux.contains(cod_ies)) {
					
					codIESAux.add(cod_ies);
					String org_academica = orgAcade.get(i);
					String ufIES = uf.get(i);
					String nome = nomeIES.get(i);
					if(nome.contains("'")) {
						String string[] = nome.split("'");
						nome = string[0]+string[1];
					}
					String tipo = tipoIES.get(i);
					
					writer.printf(insertInstituicaoSQL, cod_ies, org_academica, ufIES, nome, tipo);
					
				}
			}//Fim do for();
			
			for(int i = 0; i < codIES.size(); i++) {
				
				//Escrevendo INSERT na tabela CURSO.
				int instituicao_cod_ies = codIES.get(i);
				int num_estud_curso = numEstudCurso.get(i);
				int num_estud_insc = numEstudInsc.get(i);
				String nome = nomeCurso.get(i);
				String municipioCurso = municipio.get(i);
				if(municipioCurso.contains("'")) {
					String string[] = municipioCurso.split("'");
					municipioCurso = string[0]+string[1];
				}
				float conceito_enade = conceitoEnade.get(i);
				
				Locale local = new Locale("en");
				
				writer.format(local, insertCursoSQL, instituicao_cod_ies, 
							  num_estud_curso, num_estud_insc, nome, municipioCurso,
							  conceito_enade);
			}//Fim do for();
			
			writer.close();		
				
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
			
		return true;

	}//Fim do gerarArquivoSQL();
	
}//Fim da classe;
